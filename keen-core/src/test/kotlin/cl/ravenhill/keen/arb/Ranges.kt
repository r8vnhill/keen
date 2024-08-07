/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import cl.ravenhill.keen.arb.datatypes.arbOrderedPair
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import kotlin.math.abs

/**
 * Generates an arbitrary closed range of comparable elements.
 *
 * This function creates instances of a closed range, defined by two elements of type [T] where [T] is a
 * [Comparable] type. It uses two separate arbitraries ([a] and [b]) to generate the start and end points
 * of the range. The generated range ensures that the start is less than or equal to the end, maintaining
 * the integrity of a closed range.
 *
 * ## Usage:
 * This function is useful in scenarios where a closed range of specific types needs to be generated
 * dynamically for property-based testing. It can cater to a wide variety of types as long as they
 * implement the [Comparable] interface.
 *
 * ### Example:
 * Generating a range of integers:
 * ```kotlin
 * val intRangeArb = arbRange(Arb.int(0..100), Arb.int(101..200))
 * val intRange = intRangeArb.bind() // Produces a closed range where start <= end
 * ```
 * In this example, `intRangeArb` will generate ranges where the start is an integer between 0 and 100,
 * and the end is an integer between 101 and 200.
 *
 * @param T The type of elements in the range. Must be [Comparable].
 * @param a An [Arb]<[T]> for generating the start element of the range.
 * @param b An [Arb]<[T]> for generating the end element of the range.
 *
 * @return An [Arb] that generates closed ranges of type [T].
 */
fun <T> arbRange(a: Arb<T>, b: Arb<T>) where T : Comparable<T> = arbOrderedPair(a, b)
    .map { (start, end) -> start..end }
    .filter { abs(it.endInclusive.compareTo(it.start)) > 0 }
    .filter { it.endInclusive > it.start }
