package com.github.speak2me.kcp.tracing.annotations

/**
 * Annotation class `Tracing` is used to measure and log the execution of annotated elements.
 *
 * @property parameter Indicates whether the parameters of the annotated element should be logged.
 * @property return Indicates whether the return value of the annotated element should be logged.
 * @property thread Indicates whether the executing thread of the annotated element should be logged.
 *
 * This annotation can be applied to classes, constructors, functions, expressions, and files.
 * The information is retained in the source code but not in the compiled class files.
 *
 * @since Kotlin 1.9
 */
@SinceKotlin("1.9")
@Target(
  AnnotationTarget.CLASS,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.EXPRESSION,
  AnnotationTarget.FILE,
)
@Retention(AnnotationRetention.SOURCE)
public annotation class Tracing(
  val parameter: Boolean = false,
  val `return`: Boolean = false,
  val thread: Boolean = true,
)
