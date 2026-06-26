plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.buildconfig) apply false
  alias(libs.plugins.maven.publish) apply false
  alias(libs.plugins.kotlin.binary.compatibility) apply false
}

allprojects {
  group = "com.github.5peak2me.kcp.tracing"
  version = "1.0.0"
}
