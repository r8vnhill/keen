plugins {
    // Apply the Kotlin DSL plugin. This plugin enables the use of Kotlin-based Gradle build scripts, providing better
    // IDE support and type-safe access to Gradle APIs.
    `kotlin-dsl`
}

dependencies {
    // Add the Kotlin Gradle plugin dependency. This plugin is required to write and configure Kotlin-based Gradle build
    // scripts.
    implementation(libs.kotlin.gradle.plugin)
    // Add the DevPublish plugin dependency. This plugin facilitates publishing Gradle plugins to a repository and
    // managing plugin versions.
    implementation(libs.devPublish.plugin)
}

tasks.withType<AbstractArchiveTask>().configureEach {
    // Configure all tasks of type AbstractArchiveTask, which includes tasks like JAR or ZIP tasks used to create
    // archives.

    // Disable preserving file timestamps in the archive. This ensures that all files in the archive have the same
    // timestamp, which helps in creating reproducible builds. More details can be found here:
    // https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
    isPreserveFileTimestamps = false

    // Ensure files within the archive are ordered consistently. By setting this to true, files are added to the archive
    // in a fixed order, which is crucial for reproducible builds and helps in debugging issues related to file
    // ordering.
    isReproducibleFileOrder = true
}
