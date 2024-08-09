/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

// Apply the base plugin to the project. The base plugin provides basic tasks and configurations that are common to all
// projects, such as the 'clean' and 'assemble' tasks.
plugins {
    base
}

// Configure all tasks of type AbstractArchiveTask. AbstractArchiveTask is a base class for tasks that create archive
// files, such as ZIP, JAR, or TAR files.
tasks.withType<AbstractArchiveTask>().configureEach {
    // Ensure that the file timestamps are not preserved in the archive. By not preserving file timestamps, all files in
    // the archive will have the same timestamp, which helps in creating reproducible builds. More info:
    // https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
    isPreserveFileTimestamps = false

    // Ensure that the files within the archive are ordered consistently.
    // By setting isReproducibleFileOrder to true, the files will always be added to the archive in the same order,
    // regardless of the order they are found on the filesystem. This is crucial for creating reproducible builds.
    isReproducibleFileOrder = true
}
