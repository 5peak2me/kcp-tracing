@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  kotlin("multiplatform")
  alias(libs.plugins.kotlin.binary.compatibility)
  alias(libs.plugins.maven.publish)
}

group = "io.github.5peak2me.kcp.tracing"
version = "1.0.0"

kotlin {
  jvmToolchain(libs.versions.jdkVersion.get().toInt())

  explicitApi()

  androidNativeArm32()
  androidNativeArm64()
  androidNativeX64()
  androidNativeX86()

  iosArm64()
  iosSimulatorArm64()
  iosX64()

  js().nodejs()

  jvm()

  linuxArm64()
  linuxX64()

  macosArm64()
  macosX64()

  mingwX64()

  tvosArm64()
  tvosSimulatorArm64()
  tvosX64()

  wasmJs().nodejs()
  wasmWasi().nodejs()

  watchosArm32()
  watchosArm64()
  watchosDeviceArm64()
  watchosSimulatorArm64()
  watchosX64()

  applyDefaultHierarchyTemplate()
}

mavenPublishing {
  publishToMavenCentral()
  signAllPublications()
  configure(KotlinMultiplatform(androidVariantsToPublish = listOf("release")))

  pom {
    name.set("annotations")
    description.set("The default annotations dependency and uses")
    inceptionYear.set("2025")
    url.set("https://github.com/5peak2me/kcp-tracing/")
    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }
    developers {
      developer {
        id.set("5pea2me")
        name.set("J!nl!n")
        url.set("https://github.com/5peak2me/")
      }
    }
    scm {
      url.set("https://github.com/5peak2me/kcp-tracing")
      connection.set("scm:git:git://github.com/5peak2me/kcp-tracing.git")
      developerConnection.set("scm:git:ssh://git@github.com/5peak2me/kcp-tracing.git")
    }
  }
}
