plugins {
  `java-gradle-plugin`
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.binary.compatibility)
  alias(libs.plugins.maven.publish)
}

buildConfig {
  packageName(group.toString().replace("5peak2me", "speak2me"))

  buildConfigField("String", "COMPILER_PLUGIN_ID", "\"${group}\"")

  val compiler = projects.compilerPlugin
  buildConfigField("String", "COMPILER_PLUGIN_GROUP", "\"${compiler.group}\"")
  buildConfigField("String", "COMPILER_PLUGIN_NAME", "\"${compiler.name}\"")
  buildConfigField("String", "COMPILER_PLUGIN_VERSION", "\"${compiler.version}\"")

  val annotations = projects.annotations
  buildConfigField(
    type = "String",
    name = "ANNOTATIONS_LIBRARY_COORDINATES",
    expression = "\"${annotations.group}:${annotations.name}:${annotations.version}\""
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
