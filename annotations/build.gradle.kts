@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  kotlin("multiplatform")
  alias(libs.plugins.kotlin.binary.compatibility)
  alias(libs.plugins.maven.publish)
}

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
  // sources publishing is always enabled by the Kotlin Multiplatform plugin
  configure(KotlinMultiplatform(androidVariantsToPublish = listOf("release")))
}
