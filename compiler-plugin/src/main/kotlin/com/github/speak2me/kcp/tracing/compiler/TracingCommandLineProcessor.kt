package com.github.speak2me.kcp.tracing.compiler

import io.github.speak2me.kcp.tracing.BuildConfig
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

internal const val KEY_ANNOTATION_NAME = "annotation"
internal val KEY_ANNOTATION = CompilerConfigurationKey<String>(KEY_ANNOTATION_NAME)

internal class TracingCommandLineProcessor : CommandLineProcessor {

  override val pluginId: String
    get() = BuildConfig.COMPILER_PLUGIN_ID

  override val pluginOptions: Collection<AbstractCliOption> = listOf(
    CliOption(
      optionName = KEY_ANNOTATION_NAME,
      valueDescription = "<fqname>",
      description = "Method mark annotation name.",
      required = false,
    ),
  )

  override fun processOption(
    option: AbstractCliOption,
    value: String,
    configuration: CompilerConfiguration,
  ) {
    return when (option.optionName) {
      "annotation" -> configuration.put(KEY_ANNOTATION, value)
      else -> error("Unexpected config option: '${option.optionName}'")
    }
  }

}
