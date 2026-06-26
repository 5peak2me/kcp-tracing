# kcp-tracing [![Version](https://img.shields.io/gradle-plugin-portal/v/io.github.5peak2me.kcp.tracing.svg?logo=gradle)](https://plugins.gradle.org/plugin/io.github.5peak2me.kcp.tracing)

![Build](https://github.com/5peak2me/kcp-tracing/workflows/Build/badge.svg)
[![Kotlin](https://img.shields.io/badge/dynamic/toml?url=https://raw.githubusercontent.com/5peak2me/kcp-tracing/main/gradle/libs.versions.toml&query=%24.versions.kotlin&label=Kotlin&color=blue&logo=kotlin)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/dynamic/toml?url=https://raw.githubusercontent.com/5peak2me/kcp-tracing/main/gradle/libs.versions.toml&query=%24.versions.agp&label=AGP&color=blue&logo=android)](https://developer.android.com/build/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/dynamic/regex?url=https://raw.githubusercontent.com/5peak2me/kcp-tracing/main/gradle/wrapper/gradle-wrapper.properties&search=gradle-([0-9.]%2B)-(?:bin|all).zip&replace=$1&label=Gradle&color=blue&logo=gradle)](https://gradle.org)
[![Configuration Cache](https://img.shields.io/badge/Configuration%20Cache-supported-brightgreen.svg)](https://docs.gradle.org/current/userguide/configuration_cache.html)

<!-- Plugin description -->
This Fancy Kotlin Compiler Plugin is going to be your implementation of the brilliant ideas that you have.
<!-- Plugin description end -->

## Installation

You can add this plugin to your top-level build script using the following configuration:

### `plugins` block:

```groovy
plugins {
  id "io.github.5peak2me.kcp.tracing" version "0.0.1"
}
```
or via the

### `buildscript` block:
```groovy
apply plugin: "io.github.5peak2me.kcp.tracing"

buildscript {
  repositories {
    gradlePluginPortal()
  }

  dependencies {
    classpath "io.github.5peak2me.kcp.tracing:gradle-plugin:0.0.1"
  }
}
```

---
Plugin based on the [Kotlin Compiler Plugin Template][template].

[template]: https://github.com/5peak2me/kotlin-compiler-plugin-template
[docs:plugin-description]: https://github.com/5peak2me/kotlin-compiler-plugin-template/README.md
