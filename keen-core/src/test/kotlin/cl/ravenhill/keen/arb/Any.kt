/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.string

/**
 * Creates an arbitrary (Arb) instance that can generate values of the standard primitive types or String.
 *
 * This function provides a way to create a versatile arbitrary generator capable of producing values of various
 * common types. It combines several basic arbitrary generators, such as those for strings, integers, doubles,
 * floats, booleans, characters, bytes, and shorts. This is particularly useful in testing or simulation scenarios
 * where a wide range of random values is needed.
 *
 * ## Generated Types:
 * - String
 * - Int
 * - Double
 * - Float
 * - Boolean
 * - Char
 * - Byte
 * - Short
 *
 * ## Usage:
 * ```kotlin
 * // Create an arbitrary instance that generates any type
 * val anyArb = Arb.any()
 *
 * // Generate a random value of any of the supported types
 * val randomValue = anyArb.bind()
 * ```
 * In this example, `Arb.any()` is used to create an arbitrary instance that can generate random values of various
 * types. The `bind` method is then used to generate a single random value.
 *
 * @return An `Arb<Any>` instance capable of generating random values of various types.
 */
fun Arb.Companion.any(): Arb<Any> = choice(string(), int(), double(), float(), boolean(), char(), byte(), short())
