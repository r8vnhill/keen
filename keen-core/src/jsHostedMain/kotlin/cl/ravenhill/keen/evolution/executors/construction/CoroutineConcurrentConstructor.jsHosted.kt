/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

/**
 * The number of available processors, specifically for JavaScript environments.
 *
 * The `numProcessors` property retrieves the assumed number of processors (or CPU cores) for environments where this
 * concept is not natively supported, such as JavaScript. Since JavaScript does not provide a way to determine the
 * actual number of processors available, this property returns a default value to represent the number of processors
 * for concurrency purposes.
 *
 * @return The assumed number of processors, defaulting to 4 in JavaScript environments.
 */
internal actual val numProcessors: Int
    get() {
        // JavaScript doesn't have a concept of processors, so we return a default value.
        return 4 // Assume 4 as a reasonable default for concurrency.
    }
