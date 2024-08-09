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
                // Add Kotest libraries to the commonTest source set
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(libs.kotest.property)
            }
        }
    }
}