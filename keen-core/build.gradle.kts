
plugins {
    id("keen.jvm")
    id("keen.js")
    id("keen.native")
    alias(libs.plugins.compose)
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(libs.jakt)
            }
        }

        // Configure the commonTest source set directly
        getByName("commonTest") {
            dependencies {
                // Add Kotest libraries to the commonTest source set
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(libs.kotest.property)
            }
        }

        getByName("jvmMain") {
            dependencies {
                api(compose.desktop.common)
            }
        }

        getByName("jvmTest") {
            dependencies {
                // Add Kotlin reflection library to the commonTest source set
                implementation(kotlin("reflect"))
                implementation(libs.kotest.runner.junit5)
            }
        }

        getByName("jsMain") {
            dependencies {
                api(compose.html.core)
            }
        }
    }
}

//kotlin {
//    jvm()
////    androidTarget {
////        publishLibraryVariants("release")
////        @OptIn(ExperimentalKotlinGradlePluginApi::class)
////        compilerOptions {
////            jvmTarget.set(JvmTarget.JVM_1_8)
////        }
////    }
//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()
//    linuxX64()
//
//    sourceSets {
////        val commonMain by getting {
////            dependencies {
////                //put your multiplatform dependencies here
////            }
////        }
////        val commonTest by getting {
////            dependencies {
////                implementation(libs.kotlin.test)
////            }
////        }
//    }
//}
//
////android {
////    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
////    compileSdk = libs.versions.android.compileSdk.get().toInt()
////    defaultConfig {
////        minSdk = libs.versions.android.minSdk.get().toInt()
////    }
////}
