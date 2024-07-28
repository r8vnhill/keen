/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

rootProject.name = "keen"
include(":keen-core", ":keen-genetics", ":examples", ":benchmarks")

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
