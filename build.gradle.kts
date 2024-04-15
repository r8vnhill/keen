/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */
plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.compose") apply false
}

repositories {
    mavenCentral()
}

allprojects {
    group = "cl.ravenhill.keen"
    version = extra["keen.version"] as String
}

subprojects {
    repositories {
        mavenCentral()
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}
