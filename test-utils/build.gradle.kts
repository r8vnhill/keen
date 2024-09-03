/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    id("keen.jvm")
    id("keen.js")
}

kotlin {
    sourceSets {
        // Configure the commonTest source set directly
        getByName("commonMain") {
            dependencies {
                implementation(project(":keen-core"))
                implementation(libs.jakt)
                // Add Kotest libraries to the commonTest source set as API dependencies so they can be used by all
                // modules that depend on this one
                api(libs.kotest.arrow.core)
                api(libs.kotest.assertions.core)
                api(libs.kotest.framework.datatest)
                api(libs.kotest.framework.engine)
                api(libs.kotest.property)
                api(libs.kotest.property.arrow)
            }
        }
    }
}