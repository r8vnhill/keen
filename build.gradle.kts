/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

val dokkaVersion = extra["dokka.version"] as String

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenCentral()
}

allprojects {
    group = "cl.ravenhill.keen"
    version = extra["keen.version"] as String
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://jitpack.io") {
            name = "jitpack"
        }
    }

    java {
        withSourcesJar()
        withJavadocJar()
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
