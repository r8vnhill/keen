/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import utils.configureGradleDaemonJvm

plugins {
    id("keen.base")
    alias(libs.plugins.kotlinBinaryCompatibilityValidator)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
}

configureGradleDaemonJvm(
    project = project,
    updateDaemonJvm = tasks.updateDaemonJvm,
    gradleDaemonJvmVersion = libs.versions.gradleDaemonJvm.map { JavaVersion.toVersion(it) },
)

apiValidation {
    ignoredProjects += listOf("test-utils", "examples")
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
    outputDirectory.set(layout.buildDirectory.dir("dokka/markdown"))
}

tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(layout.buildDirectory.dir("dokka/markdown"))

    dokkaSourceSets.configureEach {
        includes.from("$rootDir/README.md")
    }
}

// Configure the multi-module documentation task
tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(layout.buildDirectory.dir("dokka/markdown"))

    // Configure source sets for multi-module documentation
    // Note: Multi-module tasks generally aggregate other module's documentation.
    // Directly including source sets in this task is usually not required.
}
