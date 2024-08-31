/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    id("keen.jvm")
    id("keen.js")
}

kotlin {
    js {
        binaries.executable()
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":keen-core"))
                implementation(project(":keen-genetics"))
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}
