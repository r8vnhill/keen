/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    id("keen.base")
    kotlin("multiplatform")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    sourceSets.all {
        languageSettings {
            optIn("cl.ravenhill.keen.ExperimentalKeen")
        }
    }
}
