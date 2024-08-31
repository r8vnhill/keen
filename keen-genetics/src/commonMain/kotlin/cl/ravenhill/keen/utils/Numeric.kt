/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils

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
