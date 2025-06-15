pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    mavenCentral()
  }
}

rootProject.name = "kcp-tracing"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":annotations")

//include(":gradle-plugin")
include(":compiler-plugin")
