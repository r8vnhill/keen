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
tasks.named<DokkaMultiModuleTask>("dokkaGfmMultiModule") {
    outputDirectory.set(layout.buildDirectory.dir("dokka/markdown"))
}

tasks.named<DokkaMultiModuleTask>("dokkaJekyllMultiModule") {
    outputDirectory.set(layout.buildDirectory.dir("dokka/jekyll"))
}
