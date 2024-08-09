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
        getByName("commonMain") {
            dependencies {
                implementation(project(":keen-core"))
            }
        }

        // Configure the commonTest source set directly
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils"))
                // Add Kotest libraries to the commonTest source set
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(libs.kotest.property)
            }
        }

        getByName("jvmTest") {
            dependencies {
                // Add Kotlin reflection library to the commonTest source set
                implementation(kotlin("reflect"))
                implementation(libs.kotest.runner.junit5)
            }
        }
    }
}
