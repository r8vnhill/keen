/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    // Apply the Keen base plugin. This plugin provides fundamental configurations and tasks that are common to all
    // projects.
    id("keen.base")
    // Apply the Kotlin Binary Compatibility Validator plugin. This plugin helps to validate binary compatibility
    // between different versions of Kotlin libraries.
    alias(libs.plugins.kotlin.binaryCompatibilityValidator)
    // Apply the Detekt plugin for static code analysis in Kotlin. Detekt helps in identifying code smells and enforcing
    // code quality rules.
    alias(libs.plugins.detekt)
    // Apply the Compose plugin for Jetpack Compose. The `apply(false)` indicates that the plugin is declared but not
    // applied to this project. This allows it to be used in subprojects or applied conditionally.
    alias(libs.plugins.compose).apply(false)
}
