import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("keen.base")
    kotlin("multiplatform")
}


tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
    }
    sourceSets.configureEach {
        languageSettings {
            optIn("cl.ravenhill.keen.ExperimentalKeen")
            optIn("io.kotest.common.ExperimentalKotest")
        }
    }
}
