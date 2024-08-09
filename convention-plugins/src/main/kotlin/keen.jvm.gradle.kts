/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import org.jetbrains.kotlin.gradle.plugin.KotlinTargetWithTests.Companion.DEFAULT_TEST_RUN_NAME
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import utils.SystemPropertiesArgumentProvider
import utils.asInt
import utils.jdkRelease
import utils.jvmTarget
import kotlin.jvm.optionals.getOrElse


plugins {
    id("keen.kotlin")
}
// Configure the Kotlin Multiplatform project to include the JVM target.
kotlin {
    jvm {
        // Enable Java interoperability for the JVM target.
        withJava()
    }
}

// Extension function to find and provide a JavaLanguageVersion from the version catalog.
// The function takes a version name as a parameter and returns a provider for the JavaLanguageVersion.
private fun VersionCatalog.findJvmVersion(name: String): Provider<JavaLanguageVersion> = provider {
    // Attempt to find the version in the version catalog by name.
    val version = findVersion(name)
        .getOrElse {
            // If the version is not found, throw an error with a descriptive message.
            error("Missing '$name' version in libs.versions.toml")
        }
    // Return a JavaLanguageVersion based on the required version found in the catalog.
    JavaLanguageVersion.of(version.requiredVersion)
}

// Get the version catalog named "libs".
val versionCatalog: VersionCatalog = versionCatalogs.named("libs")

/** The minimum Java version that Kotest supports. */
val jvmMinTargetVersion = versionCatalog.findJvmVersion("jvmMinTarget")

/** The maximum Java version that Kotest supports. */
val jvmMaxTargetVersion = versionCatalog.findJvmVersion("jvmMaxTarget")

/** The Java version used for compilation. */
val jvmCompilerVersion = versionCatalog.findJvmVersion("jvmCompiler")

// Configure the Kotlin JVM toolchain to use the specified compiler version.
kotlin {
    jvmToolchain {
        languageVersion = jvmCompilerVersion
    }
}

// Configure all tasks of type KotlinJvmCompile.
tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        // Set the JDK release version to the minimum target version.
        jdkRelease(jvmMinTargetVersion)
        // Set the JVM target version to the minimum target version.
        jvmTarget = jvmMinTargetVersion.jvmTarget()
    }
}

// Configure all tasks of type JavaCompile.
tasks.withType<JavaCompile>().configureEach {
    // Set the JDK release version to the minimum target version.
    options.release = jvmMinTargetVersion.asInt()
}

// Configure all tasks of type Test.
tasks.withType<Test>().configureEach {
    // Add a system properties argument provider to the JVM argument providers.
    jvmArgumentProviders.add(
        SystemPropertiesArgumentProvider.SystemPropertiesArgumentProvider(
            javaLauncher.map { "testJavaLauncherVersion" to it.metadata.languageVersion.asInt().toString() }
        )
    )
}

// Configure the Kotlin Multiplatform project to include the JVM target and test runs.
kotlin {
    jvm {
        // Configure the default test run for the JVM target.
        testRuns.named(DEFAULT_TEST_RUN_NAME) {
            executionTask.configure {
                // Set the Java launcher for the default test run to use the minimum target JVM version.
                javaLauncher = javaToolchains.launcherFor { languageVersion = jvmMinTargetVersion }
            }
        }
        // Create a new test run configuration named "maxJdk".
        val maxJdk by testRuns.creating {
            executionTask.configure {
                // Set the Java launcher for the maxJdk test run to use the maximum target JVM version.
                javaLauncher = javaToolchains.launcherFor { languageVersion = jvmMaxTargetVersion }
            }
        }
        // Ensure that the "check" task depends on the execution of the maxJdk test run.
        tasks.check {
            dependsOn(maxJdk.executionTask)
        }
    }
}
