/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.merge

/**
 * Generates an arbitrary [ArrayList] with elements of type [E].
 *
 * This function creates instances of [ArrayList] populated with elements generated from the specified
 * arbitrary [gen]. The size of the generated list is determined by the provided `range` parameter.
 * This function is particularly useful in scenarios where mutable lists with specific types of elements
 * are needed for testing.
 *
 * ## Usage:
 * - To generate an [ArrayList] of integers with a size range of 10 to 20:
 *   ```kotlin
 *   val arrayListArb = Arb.arrayList(Arb.int(), 10..20)
 *   val arrayList = arrayListArb.bind() // Resulting ArrayList will have a size between 10 and 20
 *   ```
 *
 * - To generate an [ArrayList] of strings with a default size range (0 to 100):
 *   ```kotlin
 *   val stringListArb = Arb.arrayList(Arb.string())
 *   val stringList = stringListArb.bind() // Resulting ArrayList will have a size between 0 and 100
 *   ```
 *
 * @param E The type of elements in the generated [ArrayList].
 * @param gen An [Arb]<[E]> that generates elements of type [E] for the [ArrayList].
 * @param range An [IntRange] specifying the possible size range of the [ArrayList]. Defaults to a range of 0 to 100.
 * @return An [Arb] that generates instances of [ArrayList]<[E]> with elements from the specified arbitrary [gen].
 */
fun <E> Arb.Companion.arrayList(gen: Arb<E>, range: IntRange = 0..100) = arbitrary {
    list(gen, range).bind().toMutableList()
}

/**
 * Generates an arbitrary [ArrayDeque] with elements of type [E].
 *
 * This function creates instances of [ArrayDeque], a double-ended queue, populated with elements
 * generated from the specified arbitrary [gen]. The size of the generated deque is determined by the
 * provided `range` parameter. This function is particularly useful for testing scenarios requiring
 * mutable, double-ended queues with specific element types.
 *
 * ## Usage:
 * - To generate an [ArrayDeque] of integers with a size range of 10 to 20:
 *   ```kotlin
 *   val arrayDequeArb = Arb.arrayDeque(Arb.int(), 10..20)
 *   val arrayDeque = arrayDequeArb.bind() // Resulting ArrayDeque will have a size between 10 and 20
 *   ```
 *
 * - To generate an [ArrayDeque] of strings with a default size range (0 to 100):
 *   ```kotlin
 *   val stringDequeArb = Arb.arrayDeque(Arb.string())
 *   val stringDeque = stringDequeArb.bind() // Resulting ArrayDeque will have a size between 0 and 100
 *   ```
 *
 * @param E The type of elements in the generated [ArrayDeque].
 * @param gen An [Arb]<[E]> that generates elements of type [E] for the [ArrayDeque].
 * @param range An [IntRange] specifying the possible size range of the [ArrayDeque]. Defaults to a range of 0 to 100.
 * @return An [Arb] that generates instances of [ArrayDeque]<[E]> with elements from the specified arbitrary [gen].
 */
fun <E> Arb.Companion.arrayDeque(gen: Arb<E>, range: IntRange = 0..100) = arbitrary {
    list(gen, range).bind().toCollection(ArrayDeque())
}

/**
 * Generates an arbitrary mutable list with elements of type [E].
 *
 * This function combines two types of mutable lists - [ArrayList] and [ArrayDeque] - to create a diverse
 * range of mutable list instances for testing purposes. The elements within these lists are generated
 * from the specified arbitrary [gen]. The size of the generated lists is controlled by the provided
 * [range] parameter.
 *
 * ## Usage:
 * - To generate a mutable list of integers within a specific size range:
 *   ```kotlin
 *   val mutableListArb = Arb.mutableList(Arb.int(), 5..10)
 *   val mutableList = mutableListArb.bind() // Resulting list will have a size between 5 and 10
 *   ```
 *
 * - To generate a mutable list of strings with the default size range (0 to 100):
 *   ```kotlin
 *   val stringListArb = Arb.mutableList(Arb.string())
 *   val stringList = stringListArb.bind() // Resulting list will have a size between 0 and 100
 *   ```
 *
 * This function is particularly useful for testing scenarios that require mutable lists with
 * varied internal structures and specific element types.
 *
 * @param E The type of elements in the generated mutable list.
 * @param gen An [Arb]<[E]> that generates elements of type [E] for the mutable list.
 * @param range An [IntRange] specifying the possible size range of the mutable list. Defaults to a range of 0 to 100.
 * @return An [Arb] that generates instances of mutable lists with elements from the specified arbitrary [gen].
 */
fun <E> Arb.Companion.mutableList(gen: Arb<E>, range: IntRange = 0..100) =
    arrayList(gen, range).merge(arrayDeque(gen, range))

/**
 * Generates an arbitrary ordered pair of elements based on provided arbitraries.
 * This function is particularly useful for testing scenarios where the order of elements matters,
 * and you need to generate pairs that follow a specific order, with optional strictness and
 * reversed ordering.
 *
 * ## Behavior:
 * - If [strict] is `true`, the function ensures that the generated pair has distinct elements.
 * - If [reversed] is `true`, the pair is ordered such that the first element is greater than or equal to the second.
 *   If `false`, the first element is less than or equal to the second.
 *
 * ## Example Usage:
 * ### Generating a pair with natural ordering:
 * ```
 * val pairArb = Arb.orderedPair(Arb.int(), Arb.int())
 * val pair = pairArb.bind()
 * // pair.first <= pair.second
 * ```
 *
 * ### Generating a strict pair in reversed order:
 * ```
 * val strictReversedPairArb = Arb.orderedPair(Arb.int(), Arb.int(), strict = true, reversed = true)
 * val strictReversedPair = strictReversedPairArb.bind()
 * // strictReversedPair.first > strictReversedPair.second
 * ```
 *
 * @param T The type of elements in the pair, which must be [Comparable].
 * @param first An [Arb]<[T]> to generate the first element of the pair.
 * @param second An [Arb]<[T]> to generate the second element of the pair.
 * @param strict A [Boolean] that, when true, ensures that the two elements of the pair are distinct.
 * @param reversed A [Boolean] that, when true, reverses the natural ordering of the pair.
 * @return An [Arb] that generates ordered pairs of type [T].
 */
fun <T> Arb.Companion.orderedPair(
    first: Arb<T>,
    second: Arb<T>,
    strict: Boolean = false,
    reversed: Boolean = false,
) where T : Comparable<T> = arbitrary {
    var (a, b) = first.bind() to second.bind()

    while (strict && a == b) {
        b = second.bind()
    }

    if (reversed) {
        if (a < b) {
            b to a
        } else {
            a to b
        }
    } else {
        if (a > b) {
            b to a
        } else {
            a to b
        }
    }
}


/**
 * Generates an arbitrary ordered pair from a single arbitrary source.
 * This function simplifies the process of creating ordered pairs by using a single [Arb]<[T]> source for both elements.
 * It is useful for scenarios requiring pairs of elements that follow a specific order based on the same type of
 * arbitrary generator.
 *
 * ## Behavior:
 * - If [strict] is `true`, the function ensures that the generated pair has distinct elements.
 * - If [reversed] is `true`, the pair is ordered such that the first element is greater than or equal to the second.
 *   If `false`, the first element is less than or equal to the second.
 *
 * ## Example Usage:
 * ### Generating a naturally ordered pair from a single arbitrary:
 * ```kotlin
 * val pairArb = Arb.orderedPair(Arb.int())
 * val pair = pairArb.bind()
 * // pair.first <= pair.second
 * ```
 *
 * ### Generating a strict pair in reversed order from a single arbitrary:
 * ```kotlin
 * val strictReversedPairArb = Arb.orderedPair(Arb.int(), strict = true, reversed = true)
 * val strictReversedPair = strictReversedPairArb.bind()
 * // strictReversedPair.first > strictReversedPair.second
 * ```
 *
 * @param T The type of elements in the pair, which must be [Comparable].
 * @param arb The [Arb]<[T]> used to generate both elements of the pair.
 * @param strict A [Boolean] that, when true, ensures that the two elements of the pair are distinct.
 * @param reversed A [Boolean] that, when true, reverses the natural ordering of the pair.
 * @return An [Arb] that generates ordered pairs of type [T] from a single arbitrary source.
 */
fun <T> Arb.Companion.orderedPair(
    arb: Arb<T>,
    strict: Boolean = false,
    reversed: Boolean = false,
) where T : Comparable<T> = orderedPair(arb, arb, strict, reversed)
