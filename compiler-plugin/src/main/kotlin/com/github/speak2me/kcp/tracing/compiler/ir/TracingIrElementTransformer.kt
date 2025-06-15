/*
 * Copyright © 2024 J!nl!n™ Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.speak2me.kcp.tracing.compiler.ir

import com.github.speak2me.kcp.tracing.compiler.internal.currentThreadFun
import com.github.speak2me.kcp.tracing.compiler.internal.ofCallable
import com.github.speak2me.kcp.tracing.compiler.internal.ofClass
import com.github.speak2me.kcp.tracing.compiler.internal.threadClass
import com.github.speak2me.kcp.tracing.compiler.internal.threadNameFunc
import com.github.speak2me.kcp.tracing.compiler.internal.toCallableId
import com.github.speak2me.kcp.tracing.compiler.internal.toClassId
import com.github.speak2me.kcp.tracing.compiler.internal.typeAnyNullable
import com.github.speak2me.kcp.tracing.compiler.internal.typeThrowable
import com.github.speak2me.kcp.tracing.compiler.internal.typeUnit
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irCatch
import org.jetbrains.kotlin.backend.common.lower.irThrow
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.buildVariable
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.ir.expressions.impl.IrTryImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.fileEntry
import org.jetbrains.kotlin.ir.util.getAnnotationArgumentValue
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.util.Logger

internal class TracingIrElementTransformer(
  private val pluginContext: IrPluginContext,
  annotation: String,
  private val logger: Logger,
) : IrElementTransformerVoidWithContext() {

  private val annotationFqName = FqName(annotation)

  // log
  @OptIn(UnsafeDuringIrConstructionAPI::class)
  private val funLog =
    pluginContext.ofCallable("kotlin.io.println".toCallableId()) {
      val parameters = owner.valueParameters
      parameters.size == 1 && parameters[0].type == pluginContext.typeAnyNullable
    }

  private val classMonotonic =
    pluginContext.ofClass("TimeSource.Monotonic".toClassId("kotlin.time"))

  private val funMarkNow =
    pluginContext.ofCallable(
      CallableId(KOTLIN_TIME_FQNAME, FqName("TimeSource"), Name.identifier("markNow")),
//      "kotlin.time.TimeSource.markNow".toCallableId()
    )

  private val funElapsedNow =
    pluginContext.ofCallable(
      CallableId(KOTLIN_TIME_FQNAME, FqName("TimeMark"), Name.identifier("elapsedNow")),
//      "kotlin.time.TimeMark.markNow".toCallableId()
    )

  override fun visitFunctionNew(declaration: IrFunction): IrStatement {
    val body = declaration.body
    if (body != null && declaration.hasAnnotation(annotationFqName)) {
//      logger.warning(declaration.dump())
      declaration.body = body.irTracing(declaration)
//      logger.warning(declaration.dump())
    }
    return super.visitFunctionNew(declaration)
  }

  private fun IrBody.irTracing(function: IrFunction): IrBlockBody {
    val showT = function.getAnnotationArgumentValue<Boolean>(annotationFqName, "thread") ?: true

    return DeclarationIrBuilder(pluginContext, function.symbol).irBlockBody {
      val thread =
        if (showT) irTemporary(irCall(pluginContext.threadClass().currentThreadFun())) else null

      +irTracingEnter(function, thread)

      val startTime = irTemporary(
        irCall(funMarkNow).also { call ->
          call.dispatchReceiver = irGetObject(classMonotonic)
        },
      )

      val tryBlock = irBlock(resultType = function.returnType) {
        for (statement in statements) +statement
        if (function.returnType == pluginContext.typeUnit) +irTracingExit(function, startTime)
      }.transform(TracingReturnTransformer(function, startTime), null)

      val throwable = buildVariable(
        scope.getLocalDeclarationParent(),
        startOffset,
        endOffset,
        IrDeclarationOrigin.CATCH_PARAMETER,
        Name.identifier("t"),
        pluginContext.typeThrowable,
      )

      +IrTryImpl(startOffset, endOffset, tryBlock.type).also { irTry ->
        irTry.tryResult = tryBlock
        irTry.catches += irCatch(
          throwable,
          irBlock {
            +irTracingExit(function, startTime, irGet(throwable))
            +irThrow(irGet(throwable))
          },
        )
      }
    }
  }

  private inner class TracingReturnTransformer(
    private val function: IrFunction,
    private val startTime: IrVariable,
  ) : IrElementTransformerVoidWithContext() {
    override fun visitReturn(expression: IrReturn): IrExpression {
      if (expression.returnTargetSymbol != function.symbol) return super.visitReturn(expression)

      return DeclarationIrBuilder(pluginContext, function.symbol).irBlock {
        if (expression.value.type == pluginContext.typeUnit) {
          +irTracingExit(function, startTime)
          +expression
        } else {
          val result = irTemporary(expression.value)
          +irTracingExit(function, startTime, irGet(result))
          +expression.apply {
            value = irGet(result)
          }
        }
      }
    }
  }

  private fun IrBuilderWithScope.irTracingExit(
    function: IrFunction,
    startTime: IrValueDeclaration,
    result: IrExpression? = null,
  ): List<IrCall> {
    val concat = irConcat()
    concat.addArgument(irString("⇠ ${function.name} [⌛"))
    concat.addArgument(
      irCall(funElapsedNow).also { call ->
        call.dispatchReceiver = irGet(startTime)
      },
    )
    val showR = function.getAnnotationArgumentValue<Boolean>(annotationFqName, "return") ?: false
    if (result != null && showR) {
      concat.addArgument(irString("] = "))
      concat.addArgument(result)
    } else {
      concat.addArgument(irString("]"))
    }

    return buildList {
      add(
        irCall(funLog).also { call ->
          call.putValueArgument(0, concat)
        },
      )
    }
  }

  private fun IrBuilderWithScope.irTracingEnter(
    function: IrFunction,
    thread: IrValueDeclaration? = null,
  ): List<IrCall> {
    val concat = irConcat()
    concat.addArgument(irString("⇢ ${function.name}("))
    val showP = function.getAnnotationArgumentValue<Boolean>(annotationFqName, "parameter") ?: false
    if (showP) {
      for ((index, valueParameter) in function.valueParameters.withIndex()) {
        if (index > 0) concat.addArgument(irString(", "))
        concat.addArgument(irString("${valueParameter.name}="))
        concat.addArgument(irGet(valueParameter))
      }
    }
    concat.addArgument(irString(")"))

    if (thread != null) {
      concat.addArgument(irString(" on Thread: ["))
      concat.addArgument(
        irCall(pluginContext.threadNameFunc()).also {
          it.dispatchReceiver = irGet(thread)
        },
      )
      concat.addArgument(irString("]"))
    }

    val filename = function.fileEntry.name.substringAfterLast("/")
    val sourceRange = function.fileEntry.getSourceRangeInfo(function.startOffset, function.endOffset)
    val line = sourceRange.startLineNumber + 1
    concat.addArgument(irString(" ($filename:$line)"))

    return buildList {
      add(
        irCall(funLog).also { call ->
          call.putValueArgument(0, concat)
        },
      )
    }
  }

  private companion object {
    @Suppress("SpellCheckingInspection")
    private val KOTLIN_TIME_FQNAME = FqName("kotlin.time")
  }
}
