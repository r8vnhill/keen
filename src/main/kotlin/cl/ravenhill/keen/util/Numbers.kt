/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package cl.ravenhill.keen.util

import org.apache.commons.math3.util.Precision

/***************************************************************************************************
 * This file contains a collection of Kotlin extension functions, which provide additional
 * functionality to builtin types like Int and Double.
 * These functions include rounding up an integer to the next multiple of a given integer, checking
 * if a double is not NaN, and comparing doubles with a given tolerance.
 * The file is designed to be used as a utility library and is not intended to be a standalone
 * program.
 **************************************************************************************************/

/**
 * A typealias for a pair of integers that represent a range.
 */
typealias IntToInt = Pair<Int, Int>

/**
 * A typealias for a pair of doubles that represent a range.
 */
typealias DoubleToDouble = Pair<Double, Double>

/**
 * Returns true if the given [Int] is within the range represented by this [Pair] of [Int] values
 * (inclusive).
 */
operator fun IntToInt.contains(i: Int) = i <= second && i >= first

/**
 * Returns true if the given [Double] is within the range represented by this [Pair] of [Double]
 * values (inclusive).
 */
operator fun DoubleToDouble.contains(d: Double) = d <= second && d >= first

/**
 * Converts an [IntToInt] function to a range.
 *
 * @return A range of integers from the lower bound (inclusive) to the upper bound (inclusive)
 * defined by the [IntToInt] function.
 */
fun IntToInt.toRange() = first..second

/**
 * Converts a [DoubleToDouble] function to a range.
 *
 * @return A range of doubles from the lower bound (inclusive) to the upper bound (inclusive)
 * defined by the [DoubleToDouble] function.
 */
fun DoubleToDouble.toRange() = first..second

/**
 * Rounds up this integer to the next multiple of the given integer.
 */
infix fun Int.roundUpToMultipleOf(i: Int): Int {
    if (i == 0) return this
    val remainder = this % i
    if (remainder == 0) return this
    return this + i - remainder
}

/**
 * Returns true if this double is not NaN.
 */
fun Double.isNotNan() = !this.isNaN()

/**
 * Returns true if this double is equal to the given double.
 */
infix fun Double.eq(d: Double): Boolean = Precision.equals(this, d, 1e-10)

/**
 * Returns true if this double is not equal to the given double.
 */
infix fun Double.neq(d: Double): Boolean = !this.eq(d)
