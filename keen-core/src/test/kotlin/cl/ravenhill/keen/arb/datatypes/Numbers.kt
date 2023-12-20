/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.pair


// region : -=================================== INT ==================================================================-
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
// endregion INT

// region : -================================= DOUBLE =================================================================-
/**
 * Creates an arbitrary generator for [Double] values excluding [Double.NaN] (Not a Number).
 *
 * This function extends the [Arb.Companion] object to generate `Double` values while ensuring they are not `NaN`.
 * It filters out `NaN` values from the provided arbitrary generator of `Double` ([arb]). By default, it uses the
 * standard `double()` ([Arb.Companion.double]) arbitrary generator. This is particularly useful in contexts where
 * ``NaN`` values are not desired or valid, such as in mathematical computations or when dealing with strictly numerical
 * datasets.
 *
 * ## Usage:
 * Utilize this function in property-based testing scenarios where ``NaN`` values need to be excluded. It provides a
 * convenient way to ensure that the generated `Double` values are always numbers.
 *
 * ### Example:
 * ```kotlin
 * val nonNaNDoubleGen = Arb.nonNaNDouble()
 * val randomDouble = nonNaNDoubleGen.bind() // Generates a random Double that is not NaN
 * ```
 * In this example, `nonNaNDoubleGen` is an arbitrary that generates `Double` values, ensuring that they are never NaN.
 *
 * @param arb An optional [Arb]<[Double]> to be filtered for non-NaN values. Defaults to the standard `double()`
 * ([Arb.Companion.double]) generator.
 * @return An [Arb]<[Double]> that generates non-``NaN`` `Double` values.
 */
fun Arb.Companion.nonNaNDouble(arb: Arb<Double> = double()) = arb.filterNot { it.isNaN() }

/**
 * Creates an arbitrary generator for pairs of non-`NaN` [Double] values.
 *
 * This function extends the [Arb.Companion] object to generate pairs of `Double` values, with both elements in the
 * pair being non-`NaN`. It utilizes the [Arb.Companion.nonNaNDouble] function to filter out `NaN` values from the
 * provided arbitrary generator of `Double` ([arb]). The resulting arbitrary generator produces pairs where each
 * element is a valid `Double` value, excluding `NaN`. This is particularly useful in scenarios where pairs of
 * numerical `Double` values are required, and `NaN` values are not permissible, such as in mathematical operations or
 * data analyses that require valid numeric inputs.
 *
 * ## Usage:
 * Employ this function in property-based testing scenarios where pairs of `Double` values are needed, and it is
 * crucial to ensure that none of the values are `NaN`. It provides a straightforward way to obtain pairs of valid
 * numerical `Double` values for testing purposes.
 *
 * ### Example:
 * ```kotlin
 * val nonNaNDoublePairGen = Arb.nonNaNDoublePair(Arb.double())
 * val randomPair = nonNaNDoublePairGen.bind() // Generates a random pair of non-`NaN` Double values
 * ```
 * In this example, `nonNaNDoublePairGen` is an arbitrary that generates pairs of `Double` values, ensuring that both
 * elements in each pair are never `NaN`.
 *
 * @param arb The [Arb]<[Double]> to be used as the base generator for creating non-`NaN` `Double` pairs. Typically,
 * this is a standard `double()` generator.
 * @return An [Arb]<[Pair]<[Double], [Double]>> that generates pairs of non-`NaN` `Double` values.
 */
fun Arb.Companion.nonNaNDoublePair(arb: Arb<Double>) = Arb.pair(Arb.nonNaNDouble(arb), Arb.nonNaNDouble(arb))

/**
 * Creates an arbitrary generator for probability values.
 *
 * This function is a part of the [Arb.Companion] object, and it generates double values between 0.0 and 1.0,
 * representing probabilities. These values are particularly useful in contexts where a probability value is required,
 * such as in the implementation of genetic algorithms or stochastic processes. The generated values exclude NaN
 * (Not a Number) and infinite values to ensure valid probability figures.
 *
 * ## Functionality:
 * - Generates double values in the range 0.0 to 1.0.
 * - Ensures that the generated values are neither NaN nor infinite, which are invalid for probability calculations.
 *
 * ## Usage:
 * Use this generator to obtain probability values for scenarios where stochastic elements are involved, such as
 * random decision-making in simulations, genetic algorithms, or other probabilistic models.
 *
 * ### Example:
 * ```kotlin
 * val probabilityGen = Arb.probability()
 * val probabilityValue = probabilityGen.bind() // Generates a probability value between 0.0 and 1.0
 * // Use probabilityValue in a context where a valid probability is required
 * ```
 * In this example, `probabilityGen` is an arbitrary that generates a valid probability value. This value can be used in
 * various applications where a random but controlled element is needed, ensuring the validity and reliability of the
 * probability used.
 *
 * @return An [Arb]<[Double]> that generates double values between 0.0 and 1.0, excluding NaN and infinite values.
 */
fun Arb.Companion.probability() = double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() }
// endregion DOUBLE