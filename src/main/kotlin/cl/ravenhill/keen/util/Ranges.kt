/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work.
 * If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */
@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package cl.ravenhill.keen.util

/***************************************************************************************************
 * This file provides efficient, streamlined utilities for managing numerical ranges, using type
 * aliases and suppressing a Kotlin specific warning for optimal performance.
 *
 * It establishes `IntToInt` and `DoubleToDouble` type aliases for pairs of Integers and Doubles
 * respectively, simplifying the representation of numerical ranges.
 * Moreover, it includes extension functions that augment these type aliases with operations for
 * checking containment within the range and converting the pairs to Kotlin's built-in range types.
 *
 * Notably, the file suppresses the "ConvertTwoComparisonsToRangeCheck" warning.
 * This suppression allows direct comparisons with the first and second elements of the pairs for
 * containment checking, instead of implicitly creating a range object.
 * This direct approach can provide a performance advantage by avoiding unnecessary object creation,
 * while also keeping the code explicit and straightforward.
 **************************************************************************************************/

/**
 * A typealias for a pair of integers that represent a range.
 */
typealias IntToInt = Pair<Int, Int>

/**
 * A typealias for a pair of doubles that represent a range.
 */
@Deprecated("Prefer using a ClosedFloatingPointRange instead.")
typealias DoubleToDouble = Pair<Double, Double>

/**
 * A typealias for a closed range of doubles.
 */
typealias DoubleRange = ClosedFloatingPointRange<Double>

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

