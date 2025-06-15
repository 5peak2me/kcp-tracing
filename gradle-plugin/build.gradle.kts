plugins {
  `java-gradle-plugin`
//  alias(libs.plugins.kotlin.jvm)
  kotlin("jvm")
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.binary.compatibility)
  alias(libs.plugins.maven.publish)
}

buildConfig {
  packageName(group.toString().replace("5peak2me", "speak2me"))

  buildConfigField("String", "COMPILER_PLUGIN_ID", "\"${rootProject.group}\"")

  val pluginProject = projects.compilerPlugin
  buildConfigField("String", "COMPILER_PLUGIN_GROUP", "\"${pluginProject.group}\"")
  buildConfigField("String", "COMPILER_PLUGIN_NAME", "\"${pluginProject.name}\"")
  buildConfigField("String", "COMPILER_PLUGIN_VERSION", "\"${pluginProject.version}\"")

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
      id = group.toString()
      displayName = "TracingGradlePlugin"
      description = "This is the description of TracingGradlePlugin"
      implementationClass = "com.github.speak2me.kcp.tracing.gradle.TracingGradlePlugin"
      tags.set(listOf("kcp", "kotlin", "compiler", "plugin", "tracing"))
    }
  }
}

dependencies {
  compileOnly(kotlin("gradle-plugin-api"))
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(libs.versions.jdkVersion.get().toInt())
  explicitApi()
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
