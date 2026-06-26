package com.github.speak2me.kcp.tracing.gradle

import io.github.speak2me.kcp.tracing.BuildConfig
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

internal class TracingGradlePlugin : KotlinCompilerPluginSupportPlugin {

  override fun apply(target: Project) {
    target.extensions.create("tracing", TracingExtension::class.java)
  }

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
    kotlinCompilation.target.project.plugins.hasPlugin(TracingGradlePlugin::class.java)

  override fun getCompilerPluginId(): String = BuildConfig.COMPILER_PLUGIN_ID

  override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
    groupId = BuildConfig.COMPILER_PLUGIN_GROUP,
    artifactId = BuildConfig.COMPILER_PLUGIN_NAME,
    version = BuildConfig.COMPILER_PLUGIN_VERSION,
  )

  override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project

    val extension = project.extensions.findByType(TracingExtension::class.java)
    if (extension != null && extension.annotation.isPresent) {
      return project.provider {
        listOf(
          SubpluginOption("annotation", extension.annotation.get()),
        )
      }
    }

    kotlinCompilation.dependencies {
      implementation(project.addDependency())
    }
    if (kotlinCompilation.implementationConfigurationName == "metadataCompilationImplementation") {
      project.dependencies.add("commonMainImplementation", project.addDependency())
    }

    return project.provider { emptyList() }
  }

  private fun Project.addDependency(): Any {
    val isInternalBuild = providers.gradleProperty("tracing.internal")
      .getOrElse("false")
      .toBoolean()

    return if (isInternalBuild) {
      project(":annotations")
    } else {
      BuildConfig.ANNOTATIONS_LIBRARY_COORDINATES
    }
  }
}
