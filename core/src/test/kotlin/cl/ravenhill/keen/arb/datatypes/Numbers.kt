/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary


/**
 * Creates an arbitrary generator for divisors of a given number.
 *
 * This function is an extension of the `Arb.Companion` object and is designed to generate random divisors of a
 * specified integer. A divisor of a number is defined as an integer that can divide the number without leaving a
 * remainder.
 *
 * ## Behavior:
 * - The function calculates all divisors of the given number, ranging from 1 to the number itself.
 * - It then randomly selects one of these divisors using the provided random source (`rs`).
 * - If the number has no divisors other than itself (e.g., if it's a prime number), the function defaults to returning
 *   1.
 *
 * ## Usage:
 * This generator can be particularly useful in property-based testing scenarios where operations involving divisors are
 * involved, such as testing mathematical algorithms, validating division operations, or generating test cases for
 * factors of numbers.
 *
 * ### Example:
 * ```kotlin
 * val divisorGen = Arb.divisor(12)
 * val randomDivisor = divisorGen.bind() // Generates a random divisor of 12, like 2, 3, 4, or 6
 * ```
 *
 * @param number The integer for which divisors are to be generated.
 * @return An `Arb<Int>`, an arbitrary that generates a random divisor of the specified number.
 */
fun Arb.Companion.divisor(number: Int) = arbitrary { rs ->
    val divisors = (1..number).filter { number % it == 0 }
    if (divisors.isNotEmpty()) {
        val randomDivisor = divisors.random(rs.random)
        randomDivisor
    } else {
        1
    }
}
