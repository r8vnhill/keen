/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

rootProject.name = "keen"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    plugins {
        kotlin("jvm") version extra["kotlin.version"] as String
        id("io.gitlab.arturbosch.detekt") version extra["detekt.version"] as String
        id("org.jetbrains.compose") version extra["compose.version"] as String
        id("org.jetbrains.dokka") version extra["dokka.version"] as String
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
include("plugin-conventions")
include(":keen-core", ":keen-genetics", ":test-utils", ":examples", ":benchmarks", )
