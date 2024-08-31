plugins {
    id("keen.jvm")
    id("keen.js")
//    id("keen.native")
//    id("keen.publishing")
//    id("keen.watchos")
//    id("keen.android")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.jakt)
                api(libs.arrow.core)
                api(libs.arrow.fx.coroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":test-utils"))
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(libs.kotest.property)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.jline)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }

        getByName("jsHostedMain") {
            dependencies {
                implementation(npm("terminal-size", "4.0.0"))
            }
        }
    }
}
