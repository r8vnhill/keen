/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

/**
 * Generates a pair of values in ascending or descending order based on the `reverted` parameter.
 *
 * This function binds two values, `i` and `j`, from the provided arbitrary generators `a` and `b`
 * respectively.
 * If the value `i` is less than `j` and `reverted` is false, or `i` is more than `j` and `reverted`
 * is true, the function returns the pair `(i, j)`.
 * Otherwise, it returns `(j, i)`.
 * If `strict` is true, it ensures that `i` and `j` are distinct values by re-binding `j` until it
 * is different from `i`.
 *
 * @receiver The `Arb.Companion` object.
 * @param a An [Arb] instance that generates [T] values.
 * @param b An [Arb] instance that generates [T] values.
 * @param strict Whether the pair should be strictly ordered (i.e., `i` is always less than `j`).
 * Defaults to `false`.
 * @param reverted Whether the generated pair should be in descending order.
 * Defaults to `false`.
 * @return An [Arb] instance that generates ordered pairs of [T] values.
 */
fun <T : Comparable<T>> Arb.Companion.orderedPair(
    a: Arb<T>,
    b: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false
) = arbitrary {
    val i = a.bind()
    var j = b.bind()

    while (strict && i == j) {
        j = b.bind() // Re-bind `j` until it is different from `i` if `strict` is `true`
    }

    if ((i < j && !reverted) || (i > j && reverted)) i to j else j to i
}

/**
 * Generates a pair of values in ascending or descending order from a single [Arb] instance.
 *
 * This function delegates to the [orderedPair] function that takes two separate [Arb] instances.
 * The same [Arb] instance `gen` is used for both values of the pair, which means that it generates
 * pairs of values from the same distribution.
 *
 * If `strict` is true, it ensures that the pair consists of distinct values.
 * If `reverted` is true, the pair is in descending order.
 *
 * @receiver The `Arb.Companion` object.
 * @param gen An [Arb] instance that generates [T] values.
 * @param strict Whether the pair should be strictly ordered (i.e., both values are distinct).
 * Defaults to `false`.
 * @param reverted Whether the generated pair should be in descending order.
 * Defaults to `false`.
 * @return An [Arb] instance that generates ordered pairs of [T] values.
 */
fun <T : Comparable<T>> Arb.Companion.orderedPair(
    gen: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false
) = orderedPair(gen, gen, strict, reverted)

/**
 * Generates a triple of values in ascending or descending order based on the `reverted` parameter.
 *
 * This function binds three values from the provided arbitrary generators `a`, `b`, and `c`.
 * The values are put into a list, sorted, and then used to create a triple.
 * If `strict` is true, it ensures that all three values are distinct.
 * If `reverted` is true, the triple will be in descending order.
 *
 * @receiver The `Arb.Companion` object.
 * @param a An [Arb] instance that generates [T] values.
 * @param b An [Arb] instance that generates [T] values.
 * @param c An [Arb] instance that generates [T] values.
 * @param strict Whether the triple should be strictly ordered (i.e., all three values are
 * distinct).
 * Defaults to `false`.
 * @param reverted Whether the generated triple should be in descending order.
 * Defaults to `false`.
 * @return An [Arb] instance that generates ordered triples of [T] values.
 */
fun <T : Comparable<T>> Arb.Companion.orderedTriple(
    a: Arb<T>,
    b: Arb<T>,
    c: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false
) = arbitrary {
    val i = a.bind()
    var j = b.bind()
    var k = c.bind()

    while (strict && i == j) {
        j = b.bind() // Re-bind `j` until it is different from `i` if `strict` is `true`
    }

    while (strict && (i == k || j == k)) {
        k = c.bind() // Re-bind `k` until it is different from `i` and `j` if `strict` is `true`
    }

    val sortedTriple = if (!reverted) {
        listOf(i, j, k).sorted()
    } else {
        listOf(i, j, k).sortedDescending()
    }
    Triple(sortedTriple[0], sortedTriple[1], sortedTriple[2])
}

/**
 * Generates a triple of values in ascending or descending order from a single [Arb] instance.
 *
 * This function delegates to the [orderedTriple] function that takes three separate [Arb]
 * instances.
 * The same [Arb] instance `gen` is used for all three values of the triple, which means that it
 * generates triples of values from the same distribution.
 *
 * If `strict` is true, it ensures that the triple consists of distinct values.
 * If `reverted` is true, the triple is in descending order.
 *
 * @receiver The `Arb.Companion` object.
 * @param gen An [Arb] instance that generates [T] values.
 * @param strict Whether the triple should be strictly ordered (i.e., all three values are
 * distinct).
 * Defaults to `false`.
 * @param reverted Whether the generated triple should be in descending order.
 * Defaults to `false`.
 * @return An [Arb] instance that generates ordered triples of [T] values.
 */
fun <T : Comparable<T>> Arb.Companion.orderedTriple(
    gen: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false
) = orderedTriple(gen, gen, gen, strict, reverted)

/**
 * Constructs an error message indicating an unfulfilled constraint based on the given
 * [description].
 * This function is typically used when enforcing constraints and reporting constraint violations.
 *
 * @param description the description of the unfulfilled constraint.
 * @return an error message indicating the unfulfilled constraint.
 */
fun unfulfilledConstraint(description: String): String = ""
