# kcp-tracing2

![Build](https://github.com/5peak2me/kcp-tracing2/workflows/Build/badge.svg)
[![Version](https://img.shields.io/gradle-plugin-portal/v/PLUGIN_ID.svg)](https://plugins.gradle.org/plugin/PLUGIN_ID)

## Template ToDo list
- [x] Create a new [Kotlin Compiler Plugin Template][template] project.
- [ ] Get familiar with the [template documentation][template].
- [ ] Adjust the [group](./gradle.properties) and [version](./gradle.properties), as well as the [implementationClass](./gradle-plugin/build.gradle.kts) in [gradle-plugin](./gradle-plugin/b).
- [ ] Adjust the [packgae name](/src/main/resources/META-INF/plugin.xml) in [gradle-plugin](./gradle-plugin)、[compiler-plugin](./compiler-plugin) and [annotations](./annotations) or [api](./api).
- [ ] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the `PLUGIN_ID` in the above README badges. You can obtain it once the plugin is published to [Gradle Plugin Portal](https://plugins.gradle.org/).
- [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [Kotlin Compiler Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
This Fancy Kotlin Compiler Plugin is going to be your implementation of the brilliant ideas that you have.
<!-- Plugin description end -->

## Installation

You can add this plugin to your top-level build script using the following configuration:

### `plugins` block:

```groovy
plugins {
  id "io.github.5peak2me.kcp.tracing2" version "$version"
}
```
or via the

### `buildscript` block:
```groovy
apply plugin: "io.github.5peak2me.kcp.tracing2"

buildscript {
  repositories {
    gradlePluginPortal()
  }

  dependencies {
    classpath "io.github.5peak2me.kcp.tracing2:gradle-plugin:$version"
  }
}
```

---
Plugin based on the [Kotlin Compiler Plugin Template][template].

[template]: https://github.com/5peak2me/kotlin-compiler-plugin-template
[docs:plugin-description]: https://github.com/5peak2me/kotlin-compiler-plugin-template/README.md
