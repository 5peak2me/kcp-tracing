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
package com.github.speak2me.kcp.tracing.compiler.internal

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

internal fun String.toClassId(packageName: String): ClassId {
  return ClassId(FqName(packageName), FqName(this), false)
}

internal fun String.toCallableId(): CallableId {
  val parts = split(".")
  val methodName = parts.last()
  val fqName =
    if (parts.size == 1) {
      FqName("")
    } else {
      FqName((parts - methodName).joinToString("."))
    }
  return CallableId(fqName, Name.identifier(methodName))
}

private val ClassThread = ClassId(FqName("java.lang"), FqName("Thread"), false)

internal fun IrPluginContext.threadClass() = referenceClass(ClassThread)!!

@OptIn(UnsafeDuringIrConstructionAPI::class)
internal fun IrClassSymbol.currentThreadFun(): IrSimpleFunctionSymbol {
  return functions.single {
    it.owner.name.asString() == "currentThread"
  }
}

internal fun IrPluginContext.threadNameFunc() =
  referenceFunctions(CallableId(classId = ClassThread, Name.identifier("getName"))).single()

internal inline val IrPluginContext.typeAnyNullable get() = irBuiltIns.anyNType
internal inline val IrPluginContext.typeThrowable get() = irBuiltIns.throwableType
internal inline val IrPluginContext.typeUnit get() = irBuiltIns.unitType

internal fun IrPluginContext.ofClass(classId: ClassId) =
  referenceClass(classId) ?: error("Failed to resolve $classId")

internal fun IrPluginContext.ofCallable(
  callableId: CallableId,
  predicate: (IrSimpleFunctionSymbol.() -> Boolean) = { true },
) = referenceFunctions(callableId).single(predicate)
