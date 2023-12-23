/*
 * Copyright (c) 2023, Ignacio Slater M.
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
    // Configures the JVM toolchain to use version 8 of the JDK
    jvmToolchain(17)
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}
