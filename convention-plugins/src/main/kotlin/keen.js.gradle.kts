/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("keen.kotlin")
}

kotlin {
    // Configure the Kotlin Multiplatform project for JavaScript targets.
    js {
        // Enable browser target for JavaScript.
        browser()
        // Enable Node.js target for JavaScript.
        nodejs()
    }

    // Configure the Kotlin Multiplatform project for WebAssembly (WASM) with JavaScript interop.
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        // Enable browser target for WASM with JavaScript interop.
        browser()
        // Enable Node.js target for WASM with JavaScript interop.
        nodejs()
    }

    // Configure the Kotlin Multiplatform project for WebAssembly (WASM) with WASI (WebAssembly System Interface).
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmWasi()

    // Apply a hierarchy template for organizing source sets and targets.
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        // Define a group named "common" for common configurations.
        group("common") {
            // Define a subgroup named "jsHosted" for JavaScript-hosted targets.
            group("jsHosted") {
                // Include JavaScript targets in the jsHosted group.
                withJs()
                // Include WASM with JavaScript interop targets in the jsHosted group.
                withWasm()  // FIXME with Kotlin 2.0.0: KT-63417 – to be split into `withWasmJs` and `withWasmWasi`
            }
        }
    }
}
