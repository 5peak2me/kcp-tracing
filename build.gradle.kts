plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.buildconfig) apply false
  alias(libs.plugins.maven.publish) apply false
  alias(libs.plugins.kotlin.binary.compatibility) apply false
}
