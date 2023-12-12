package cl.ravenhill.keen.arbs.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.next

/**
 * Generates an arbitrary real number within the range [0.0, 1.0], representing a probability.
 *
 * This function leverages Kotest's [Arb] (Arbitrary) API to produce real numbers that are commonly
 * used as probabilities in various computations and simulations. The values are confined within the
 * standard probability range from 0.0 (inclusive) to 1.0 (inclusive).
 *
 * ## Usage:
 * - To generate a random probability value:
 *   ```kotlin
 *   val probabilityArb = Arb.probability()
 *   val probability = probabilityArb.bind() // A value between 0.0 and 1.0
 *   ```
 *
 * This function is particularly useful in scenarios where random but controlled probabilities are
 * needed, such as in simulations, statistical modeling, or certain aspects of genetic algorithms where
 * probabilities determine the likelihood of events like mutation or crossover.
 *
 * @return An [Arb] that generates real numbers within the range [0.0, 1.0], suitable for use as probabilities.
 */
fun Arb.Companion.probability() = real(0.0..1.0)

/**
 * Returns an arbitrary generator for [Double] values within the given [range], excluding NaN and
 * infinite values.
 */
fun Arb.Companion.real(
    range: ClosedFloatingPointRange<Double> = Double.MIN_VALUE..Double.MAX_VALUE
) = arbitrary {
    double(range).next()
}