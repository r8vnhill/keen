/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.pair

/**
 * Generates an arbitrary ordered pair of comparable elements.
 *
 * This function is useful in testing scenarios where pairs of elements need to follow a specific order,
 * such as being sorted or strictly different. It generates pairs where the elements are ordered based
 * on their natural ordering, with options to enforce strict inequality or reversed ordering.
 *
 * ## Usage:
 * ### Generating a naturally ordered pair:
 * ```
 * val pairArb = Arb.orderedPair(Arb.int(), Arb.int())
 * val pair = pairArb.bind()
 * // pair.first <= pair.second
 * ```
 *
 * ### Generating a strict and reversed pair:
 * ```
 * val strictReversedPairArb = Arb.orderedPair(Arb.int(), Arb.int(), strict = true, reversed = true)
 * val strictReversedPair = strictReversedPairArb.bind()
 * // strictReversedPair.first > strictReversedPair.second
 * ```
 *
 * @param T The type of elements in the pair, which must be [Comparable].
 * @param a An [Arb]<[T]> for generating the first element of the pair.
 * @param b An [Arb]<[T]> for generating the second element of the pair.
 * @param strict If `true`, ensures that the generated pair contains distinct elements.
 * @param reversed If `true`, the pair is ordered in descending order. If `false`, the pair is in ascending order.
 *
 * @return An [Arb] that generates ordered pairs of type [T].
 */
fun <T> arbOrderedPair(
    a: Arb<T>,
    b: Arb<T>,
    strict: Boolean = false,
    reversed: Boolean = false,
) where T : Comparable<T> = Arb.pair(a, b).filter { (first, second) ->
    if (strict) first != second else true
}.filter { (first, second) ->
    if (reversed) first >= second else first <= second
}

/**
 * Generates an arbitrary ordered pair of comparable elements using a single arbitrary generator.
 *
 * This function creates pairs of elements where both elements are generated using the same arbitrary generator ([gen]).
 * The elements are then ordered based on their natural ordering, ensuring that the pair follows a consistent
 * sequence based on the specified parameters.
 *
 * ## Usage Examples:
 * ### Generating a naturally ordered pair from the same generator:
 * ```
 * val pairArb = Arb.orderedPair(Arb.int())
 * val pair = pairArb.bind()
 * // Ensures that pair.first <= pair.second
 * ```
 *
 * ### Generating a strict and reversed pair from the same generator:
 * ```
 * val strictReversedPairArb = Arb.orderedPair(Arb.int(), strict = true, reverted = true)
 * val strictReversedPair = strictReversedPairArb.bind()
 * // Ensures that strictReversedPair.first > strictReversedPair.second
 * ```
 *
 * @param T The type of elements in the pair, which must be [Comparable].
 * @param gen The [Arb]<[T]> used for generating both elements of the pair.
 * @param strict A [Boolean] that, when true, ensures that the two elements of the pair are distinct.
 * @param reverted A [Boolean] that, when true, reverses the natural ordering of the pair.
 *
 * @return An [Arb] that generates ordered pairs of type [T].
 */
fun <T> arbOrderedPair(
    gen: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false,
) where T : Comparable<T> = arbOrderedPair(gen, gen, strict, reverted)
