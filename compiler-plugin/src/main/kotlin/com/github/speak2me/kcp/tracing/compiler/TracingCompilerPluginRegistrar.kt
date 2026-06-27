package com.github.speak2me.kcp.tracing.compiler

import com.github.speak2me.kcp.tracing.compiler.ir.TracingIrGenerationExtension
import io.github.speak2me.kcp.tracing.BuildConfig
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.getLogger
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

internal class TracingCompilerPluginRegistrar: CompilerPluginRegistrar() {

  override val pluginId: String
    get() = BuildConfig.COMPILER_PLUGIN_ID

  override val supportsK2: Boolean
    get() = true

  override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
    val logger = configuration.getLogger()
    val annotation = configuration.get(KEY_ANNOTATION, "com.github.speak2me.kcp.tracing.annotations.Tracing")
    IrGenerationExtension.registerExtension(TracingIrGenerationExtension(logger, annotation))
  }

}
