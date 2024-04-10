/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.prog.Environment
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string

/**
 * Generates an arbitrary [Environment]<[T]> instance, simulating a programming environment with variable mappings.
 *
 * This extension function for [Arb.Companion] is part of the Keen library's testing tools. It creates an arbitrary
 * generator for `Environment<T>` instances. In this context, an `Environment<T>` represents a programming
 * environment where each variable (of type `T`) is associated with an integer index, mimicking memory addresses
 * or variable storage in a programming context.
 *
 * ## Usage:
 * - Ideal for testing scenarios that require the simulation of programming environments with diverse and
 *   randomized variable mappings.
 * - Useful in property-based testing where the robustness of algorithms or functions is evaluated against
 *   various randomly generated programming environments.
 *
 * ## Example:
 * ```kotlin
 * val environmentGen = Arb.environment<Int>(memory = Arb.map(Arb.int(), Arb.int()))
 * val randomEnvironment = environmentGen.bind() // Generates a random Environment<Int> instance with variable mappings
 * ```
 *
 * ## Experimental API Notice:
 * The function is marked with `@OptIn(ExperimentalKeen::class)`, indicating the use of experimental features
 * from the Keen library. Users should be aware that this API may be subject to change and should be used
 * cautiously in production environments.
 *
 * @param T The type of variables stored in the environment.
 * @param memory Optional parameter to provide an initial set of variable mappings. If omitted, the environment starts
 *   empty.
 * @param name Arbitrary names for the environment, useful in scenarios where environments need to be distinguished.
 * @return An arbitrary generator for `Environment<T>` instances.
 */
@OptIn(ExperimentalKeen::class)
fun <T> Arb.Companion.environment(memory: Arb<Map<Int, T>>?, name: Arb<String> = string()) = arbitrary {
    Environment<T>(name.bind()).apply {
        memory?.bind()?.forEach { (key, value) -> this += key to value }
    }
}
