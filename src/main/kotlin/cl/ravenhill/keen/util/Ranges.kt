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
 * A typealias for a closed range of doubles.
 */
typealias DoubleRange = ClosedFloatingPointRange<Double>
