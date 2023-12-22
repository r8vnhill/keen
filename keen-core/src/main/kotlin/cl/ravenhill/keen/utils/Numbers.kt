/*
 * Copyright (c) 2023, Ignacio Slater M.
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
 * ## Usage:
 * This function can be used in scenarios where it's necessary to ensure that a [Double] value is valid and can be
 * used in further calculations or comparisons. It's particularly useful in mathematical computations or algorithms
 * where encountering NaN could lead to incorrect results or exceptions.
 *
 * ### Example:
 * ```
 * val myValue = 1.0 / 0.0 // Results in Double.POSITIVE_INFINITY
 * if (myValue.isNotNan()) {
 *     println("Valid number")
 * } else {
 *     println("NaN encountered")
 * }
 * ```
 * In this example, `isNotNan()` is used to check if `myValue` is not NaN. Since dividing by zero in floating-point
 * arithmetic results in infinity and not NaN, the output will be "Valid number".
 *
 * @return `true` if the [Double] value is not NaN, `false` if it is NaN.
 */
fun Double.isNotNaN() = !isNaN()

/**
 * Infix function for approximate equality comparison of two `Double` values.
 *
 * This function compares two `Double` values for equality within a specified threshold. The comparison
 * is not exact due to the nature of floating-point arithmetic. Instead, it checks if the absolute difference
 * between the two values is less than a predefined threshold ([Domain.equalityThreshold]), which represents
 * the acceptable margin of error for equality.
 *
 * ## Usage:
 * ```
 * val a = 1.000001
 * val b = 1.000002
 * if (a eq b) {
 *     println("a and b are approximately equal")
 * }
 * ```
 * In this example, `a eq b` checks if `a` and `b` are approximately equal considering the predefined threshold.
 *
 * @param d The [Double] value to be compared with the receiver [Double].
 * @return `true` if the absolute difference between the receiver and the argument `d` is less than the equality
 *   threshold; `false` otherwise.
 */
infix fun Double.eq(d: Double): Boolean = if (this == d) {
    true
} else {
    abs(this - d) < Domain.equalityThreshold
}

/**
 * Rounds up the current integer to the nearest multiple of a specified integer.
 *
 * This function takes an integer value and rounds it up to the nearest multiple of a specified integer [i].
 * If the current integer is already a multiple of [i], it returns the current integer. If [i] is zero, the
 * function simply returns the current integer. Otherwise, it calculates the next multiple of [i] that is
 * greater than or equal to the current integer.
 *
 * ## Usage:
 * ```
 * val result1 = 7 roundUpToMultipleOf 5 // Returns 10
 * val result2 = 12 roundUpToMultipleOf 0 // Returns 12
 * val result3 = 15 roundUpToMultipleOf 5 // Returns 15
 * ```
 * In these examples:
 * - `result1` is 10 because the next multiple of 5 greater than 7 is 10.
 * - `result2` is 12 because when the specified multiple is 0, the function returns the current integer.
 * - `result3` is 15 because 15 is already a multiple of 5.
 *
 * @param i The integer value to which the current integer is to be rounded up.
 * @return The smallest integer that is greater than or equal to the current integer and is a multiple of `i`.
 */
infix fun Int.roundUpToMultipleOf(i: Int): Int = when (i) {
    0 -> this
    else -> when (val remainder = this % i) {
        0 -> this
        else -> this + i - remainder
    }
}
