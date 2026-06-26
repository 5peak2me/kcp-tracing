import org.gradle.plugin.compatibility.compatibility

plugins {
  `java-gradle-plugin`
  `java-test-fixtures`
  kotlin("jvm")
  kotlin("kapt")
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.binary.compatibility)
  alias(libs.plugins.plugin.publish)
}

buildConfig {
  packageName(group.toString().replace("5peak2me", "speak2me"))

  buildConfigField("String", "COMPILER_PLUGIN_ID", "\"${project.group}\"")

  buildConfigField("String", "COMPILER_PLUGIN_GROUP", "\"${project.group}\"")
  buildConfigField("String", "COMPILER_PLUGIN_NAME", "\"${project.name}\"")
  buildConfigField("String", "COMPILER_PLUGIN_VERSION", "\"${project.version}\"")

  val annotationsProject = projects.annotations
  buildConfigField(
    type = "String",
    name = "ANNOTATIONS_LIBRARY_COORDINATES",
    expression = "\"${annotationsProject.group}:${annotationsProject.name}:${annotationsProject.version}\"",
  )
}

gradlePlugin {
  website.set("https://daijinlin.com/kcp-tracing")
  vcsUrl.set("https://github.com/5peak2me/kcp-tracing")
  plugins {
    register("SimplePlugin") {
      id = "io.github.5peak2me.kcp.tracing"
      displayName = "TracingGradlePlugin"
      description = "This is the description of TracingGradlePlugin"
      implementationClass = "com.github.speak2me.kcp.tracing.gradle.TracingGradlePlugin"
      tags.set(listOf("kcp", "kotlin", "compiler", "plugin", "tracing"))

      compatibility {
        features {
          configurationCache = true
        }
      }
    }
  }
}

dependencies {
  compileOnly(kotlin("compiler"))

  compileOnly(kotlin("gradle-plugin-api"))

  testImplementation(kotlin("test"))

  testRuntimeOnly(libs.junit)
  testRuntimeOnly(kotlin("reflect"))
  testRuntimeOnly(kotlin("test"))
  testRuntimeOnly(kotlin("script-runtime"))
  testRuntimeOnly(kotlin("annotations-jvm"))
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(libs.versions.jdkVersion.get().toInt())
  explicitApi()
  sourceSets.configureEach {
    languageSettings {
      optIn("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
    }
  }
}

publishing {
  publications {
    register("release", MavenPublication::class) {
      from(components["java"])
      group = project.group
      version = project.version.toString()
    }
  }
}
