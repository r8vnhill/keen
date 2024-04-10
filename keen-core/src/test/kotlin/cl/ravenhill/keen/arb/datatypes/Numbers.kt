/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.pair

/**
 * Creates an arbitrary generator for divisors of a given number.
 *
 * ## Behavior:
 * - The function calculates all divisors of the given number, ranging from 1 to the number itself.
 * - It then randomly selects one of these divisors using the provided random source (`rs`).
 * - If the number has no divisors other than itself (e.g., if it's a prime number), the function defaults to returning 1.
 *
 * @param number The integer for which divisors are to be generated.
 * @return An `Arb<Int>`, an arbitrary that generates a random divisor of the specified number.
 */
fun arbDivisor(number: Int) = arbitrary { rs ->
    val divisors = (1..number).filter { number % it == 0 }
    if (divisors.isNotEmpty()) {
        val randomDivisor = divisors.random(rs.random)
        randomDivisor
    } else {
        1
    }
}

/**
 * Creates an arbitrary generator for `Double` values excluding NaN (Not a Number).
 *
 * @param arb
 *  An optional `Arb<Double>` to be filtered for non-NaN values.
 *  Defaults to the standard `double()` ([Arb.Companion.double]) generator.
 * @return An `Arb<Double>` that generates non-`NaN` `Double` values.
 */
fun arbNonNaNDouble(arb: Arb<Double> = Arb.double()) = arb.filterNot { it.isNaN() }

/**
 * Creates an arbitrary generator for pairs of non-NaN `Double` values.
 *
 * @param arb
 *  The `Arb<Double>` to be used as the base generator for creating non-NaN `Double` pairs.
 *  Defaults to the standard `double()` ([Arb.Companion.double]) generator.
 * @return An `Arb<Pair<Double], Double>>` that generates pairs of non-NaN `Double` values.
 */
fun arbNonNaNDoublePair(arb: Arb<Double>) = Arb.pair(arbNonNaNDouble(arb), arbNonNaNDouble(arb))

/**
 * Creates an arbitrary generator for probability values.
 *
 * @return An `Arb<Double>` that generates double values between 0.0 and 1.0, excluding NaN and infinite values.
 */
fun arbProbability() = Arb.double(0.0..1.0, includeNonFiniteEdgeCases = false)
