/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils

import cl.ravenhill.keen.Domain
import kotlin.math.abs

/**
 * Checks if a [Double] value is not NaN (Not a Number).
 *
 * This extension function provides a convenient way to verify that a [Double] value is a valid number and not
 * the special NaN (Not a Number) value. NaN is used to represent undefined or unrepresentable values in floating-point
 * calculations.
 *
 * @return `true` if the [Double] value is not NaN, `false` if it is NaN.
 */
fun Double.isNotNaN() = !isNaN()

/**
 * Compares two double values for equality, considering special cases like infinity and a custom threshold.
 *
 * The `eq` infix function is an extension function for `Double` that checks if two double values are equal, with
 * specific handling for positive and negative infinity. The comparison uses a custom threshold defined by
 * [Domain.equalityThreshold] to account for floating-point precision errors, making it more reliable for comparing
 * double values in scenarios where exact equality is difficult to achieve.
 *
 * ## Special Cases:
 * - **Positive Infinity**: If both values are `Double.POSITIVE_INFINITY`, they are considered equal.
 * - **Negative Infinity**: If both values are `Double.NEGATIVE_INFINITY`, they are considered equal.
 * - **General Case**: For all other values, the function checks if the absolute difference between the two values is less
 *   than `Domain.equalityThreshold`.
 *
 * @param d The double value to compare with the receiver.
 * @return `true` if the values are considered equal according to the defined rules; `false` otherwise.
 */
infix fun Double.eq(d: Double): Boolean = when {
    this == Double.POSITIVE_INFINITY && d == Double.POSITIVE_INFINITY -> true
    this == Double.NEGATIVE_INFINITY && d == Double.NEGATIVE_INFINITY -> true
    else -> abs(this - d) < Domain.equalityThreshold
}

/**
 * Subtracts a given value from each element in an iterable collection of doubles.
 *
 * @param d The value to be subtracted from each element in the collection.
 * @return A list of doubles where each element is the result of the corresponding element in the original collection
 *   minus the given value.
 */
infix fun Iterable<Double>.sub(d: Double): List<Double> = map { it - d }
