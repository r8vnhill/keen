import utils.configureGradleDaemonJvm

plugins {
   id("keen.base")
   alias(libs.plugins.kotlinBinaryCompatibilityValidator)
}

configureGradleDaemonJvm(
   project = project,
   updateDaemonJvm = tasks.updateDaemonJvm,
   gradleDaemonJvmVersion = libs.versions.gradleDaemonJvm.map { JavaVersion.toVersion(it) },
)

apiValidation {
   ignoredProjects += listOf("test-utils")
}