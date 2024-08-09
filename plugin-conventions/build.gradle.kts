/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.devPublish.plugin)
}

tasks.withType<AbstractArchiveTask>().configureEach {
    // https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
