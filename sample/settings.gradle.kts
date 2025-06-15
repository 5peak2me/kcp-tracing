enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
//        mavenLocal()
    }
}

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
      google()
      mavenCentral()
//      mavenLocal()
  }
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

rootProject.name = "sample"
include(":androidApp")
include(":shared")

includeBuild("..")
