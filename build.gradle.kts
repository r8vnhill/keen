/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import utils.configureGradleDaemonJvm

plugins {
    id("keen.base")
    alias(libs.plugins.kotlinBinaryCompatibilityValidator)
    alias(libs.plugins.detekt)
}

configureGradleDaemonJvm(
    project = project,
    updateDaemonJvm = tasks.updateDaemonJvm,
    gradleDaemonJvmVersion = libs.versions.gradleDaemonJvm.map { JavaVersion.toVersion(it) },
)

apiValidation {
    ignoredProjects += listOf("test-utils")
}
