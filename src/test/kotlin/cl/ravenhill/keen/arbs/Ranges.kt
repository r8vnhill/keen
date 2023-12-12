/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs

import cl.ravenhill.keen.arbs.datatypes.orderedPair
import cl.ravenhill.keen.arbs.datatypes.real
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.char

/**
 * Generates an arbitrary range of elements of type [T] that are [Comparable].
 *
 * This function creates ranges where the start and end values are determined by the provided
 * arbitraries [a] and [b]. The generated range ensures that the start value is less than or
 * equal to the end value, conforming to the natural ordering of [T].
 *
 * ## Usage:
 * - To generate a range of integers:
 *   ```kotlin
 *   val intRangeArb = Arb.range(Arb.int(0..50), Arb.int(51..100))
 *   val intRange = intRangeArb.bind() // Resulting range will be within 0 to 100
 *   ```
 *
 * - To generate a range of comparable custom objects:
 *   ```kotlin
 *   // Assuming MyComparable is a Comparable class
 *   val myComparableRangeArb = Arb.range(Arb.myComparable(), Arb.myComparable())
 *   val myComparableRange = myComparableRangeArb.bind()
 *   // Resulting range will have start and end values in the natural order of MyComparable
 *   ```
 *
 * This function is particularly useful when testing functions or algorithms that operate on
 * ranges or intervals, especially where the ordering of elements is critical.
 *
 * @param T The type of elements in the generated range, which must be [Comparable].
 * @param a An [Arb]<[T]> to generate the start value of the range.
 * @param b An [Arb]<[T]> to generate the end value of the range.
 * @return An [Arb] that generates ranges of type [T], with start and end values determined by the specified
 *   arbitraries.
 */
fun <T> Arb.Companion.range(a: Arb<T>, b: Arb<T>) where T : Comparable<T> = arbitrary {
    orderedPair(a, b).bind().let {
        it.first..it.second
    }
}

/**
 * Generates an arbitrary range of characters.
 *
 * This function creates ranges of `Char` values, where the start (minimum) and end (maximum) values
 * are determined by the provided arbitraries [a] and [b]. The resulting range ensures that the start
 * character comes before or is equal to the end character in the Unicode character sequence.
 *
 * ## Usage:
 * - To generate a range of characters with default character arbitraries:
 *   ```kotlin
 *   val charRangeArb = Arb.charRange()
 *   val charRange = charRangeArb.bind() // Resulting range will have start and end characters in natural order
 *   ```
 *
 * - To generate a range of characters within specified bounds:
 *   ```kotlin
 *   val charRangeArb = Arb.charRange(Arb.char('a'..'e'), Arb.char('f'..'z'))
 *   val charRange = charRangeArb.bind() // Resulting range will be within 'a' to 'z'
 *   ```
 *
 * This function is particularly useful when testing algorithms or functions that require character
 * ranges, such as string processing routines, parsers, or text-based data validation.
 *
 * @param a An [Arb]<[Char]> to generate the start character of the range. Defaults to a general character arbitrary.
 * @param b An [Arb]<[Char]> to generate the end character of the range. Defaults to the same general character
 *   arbitrary as [a].
 * @return An [Arb] that generates ranges of type `Char`, with start and end characters determined by the specified
 *   arbitraries.
 */
fun Arb.Companion.charRange(a: Arb<Char> = char(), b: Arb<Char> = char()): Arb<ClosedRange<Char>> = range(a, b)

/**
 * Generates an arbitrary range of `Double` values.
 *
 * This function creates ranges of `Double` values, where the start (minimum) and end (maximum) values
 * are determined by the provided arbitraries [a] and [b]. The resulting range ensures that the start
 * value is less than or equal to the end value.
 *
 * ## Usage:
 * - To generate a range of `Double` values with default real number arbitraries:
 *   ```kotlin
 *   val doubleRangeArb = Arb.doubleRange()
 *   val doubleRange = doubleRangeArb.bind() // Resulting range will have start and end values in natural order
 *   ```
 *
 * - To generate a range of `Double` values within specified bounds:
 *   ```kotlin
 *   val doubleRangeArb = Arb.doubleRange(Arb.real(0.0..100.0), Arb.real(101.0..200.0))
 *   val doubleRange = doubleRangeArb.bind() // Resulting range will be within 0.0 to 200.0
 *   ```
 *
 * This function is particularly useful in scenarios that require testing with a range of floating-point numbers,
 * such as mathematical computations, statistical analyses, or simulations involving continuous values.
 *
 * @param a An [Arb]<[Double]> to generate the start value of the range. Defaults to a general real number arbitrary.
 * @param b An [Arb]<[Double]> to generate the end value of the range. Defaults to the same general real number
 *   arbitrary as [a].
 * @return An [Arb] that generates ranges of type `Double`, with start and end values determined by the specified
 *   arbitraries.
 */
fun Arb.Companion.doubleRange(a: Arb<Double> = real(), b: Arb<Double> = real()): Arb<ClosedRange<Double>> = range(a, b)
