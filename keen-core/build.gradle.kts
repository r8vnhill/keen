import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("keen.jvm")
    id("keen.js")
    alias(libs.plugins.dokka)
//    id("keen.native")
//    id("keen.publishing")
//    id("keen.watchos")
//    id("keen.android")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.jakt)
                api(libs.arrow.core)
                api(libs.arrow.fx.coroutines)
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(project(":test-utils"))
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(libs.kotest.property)
            }
        }

        getByName("jvmMain") {
            dependencies {
                implementation(libs.jline)
                implementation(libs.xchart)
            }
        }

        getByName("jvmTest") {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }

        getByName("jsHostedMain") {
            dependencies {
                implementation(npm("terminal-size", "4.0.0"))
                implementation(npm("chart.js", "4.4.4"))
                implementation(npm("chartjs-node-canvas", "4.1.6"))
                implementation(npm("canvas", "2.11.2"))
                implementation(libs.kotlinx.html)
            }
        }
    }
}

tasks.named<DokkaTask>("dokkaGfm") {
    outputDirectory.set(layout.buildDirectory.dir("dokka/markdown"))
}

