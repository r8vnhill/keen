package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement
import java.util.LinkedList
import java.util.stream.IntStream
import kotlin.random.Random

/***************************************************************************************************
 * This code contains several extension functions for the Random class in Kotlin.
 **************************************************************************************************/

/**
 * Returns a random "printable" character.
 *
 * By default, the character returned can be any printable character in the ASCII range [32, 126],
 * inclusive.
 * If a [filter] is provided, the character returned must satisfy the `filter` function; otherwise,
 * a new character is generated until the condition is met.
 *
 * @param filter a predicate function that takes a [Char] and returns a [Boolean] indicating
 *  whether the character is acceptable
 * @return a random "printable" character that satisfies the [filter], or the first one
 *  found if no [filter] is specified.
 */
fun Random.nextChar(range: CharRange, filter: (Char) -> Boolean = { true }) =
    generateSequence { range.random(this) }.filter(filter).first()

/**
 * Returns a stream of pseudorandom int values, each conforming to the given origin
 * (inclusive) and bound (exclusive).
 *
 * @receiver the random instance.
 * @param from the origin (inclusive) of each random value.
 * @param until the bound (exclusive) of each random value.
 * @return a stream of pseudorandom int values.
 */
fun Random.ints(from: Int = 0, until: Int = Int.MAX_VALUE): IntStream =
    IntStream.generate { this.nextInt(from, until) }

/**
 * Returns a list of randomly selected indices, using the given pick probability.
 *
 * The [pickProbability] parameter determines the likelihood that each index in the range will be
 * picked.
 * If the probability is 0.0, then an empty list is returned.
 * If the probability is 1.0, then all indices in the range are included in the result.
 * Otherwise, each index is included with probability [pickProbability].
 * The result is returned as a list of integers in ascending order.
 *
 * Note that the actual probability of selecting each index may deviate slightly from
 * [pickProbability], due to the randomness of the selection process.
 *
 * @receiver the random instance to use.
 * @param pickProbability the probability that an index will be picked, between 0.0 and 1.0
 *  (inclusive).
 * @param end the exclusive end index of the range to select from.
 * @param start the inclusive start index of the range to select from (default is 0).
 * @return a list of randomly selected indices.
 */
fun Random.indices(pickProbability: Double, end: Int, start: Int = 0): List<Int> {
    enforce {
        "The probability [$pickProbability] must be between 0.0 and 1.0, inclusive." {
            pickProbability should BeInRange(0.0..1.0)
        }
    }
    // Select the indices using the given pick probability.
    return when {
        // If the probability is too low, then no indexes will be picked.
        pickProbability <= 1e-20 -> emptyList()
        // If the probability is too high, then all indexes will be picked.
        pickProbability >= 1 - 1e-20 -> List(end - start) { it + start }
        // Otherwise, pick indexes randomly.
        else -> List(end - start) { start + it }
            .filter { this.nextDouble() <= pickProbability }
    }
}

/**
 * Generates a random [List] of indices in the range [0, [end]).
 *
 * @param size the size of the list to generate.
 * @param end the size of the list to select indices from.
 * @param start the inclusive start index of the range to select from (default is 0).
 * @return a list of indices.
 */
fun Random.indices(size: Int, end: Int, start: Int = 0): List<Int> =
    subsets(List(end - start) { it + start }, size, true, 1).first()

/**
 * Returns a random value outside the specified [range], using this [Random] instance.
 *
 * If the range includes all possible values, the function will always return a random value.
 *
 * @param range a pair of values representing the inclusive lower and upper bounds of the range.
 * @param minFunc a function that returns the minimum value for the type of values in the range.
 * @param maxFunc a function that returns the maximum value for the type of values in the range.
 * @param randomFunc a function that generates a random value of the same type as the values in the
 *  range.
 * @return a random value outside the specified range.
 */
fun <T : Comparable<T>> Random.nextValueExclusive(
    range: Pair<T, T>,
    minFunc: () -> T,
    maxFunc: () -> T,
    randomFunc: (T, T) -> T
): T {
    val (min, max) = range
    return when {
        nextBoolean() && min > minFunc() -> {
            randomFunc(minFunc(), min)
        }

        max < maxFunc() -> randomFunc(max, maxFunc())
        else -> randomFunc(minFunc(), min)
    }
}

/**
 * Returns a random integer outside the specified [range], using this [Random] instance.
 *
 * If the range includes all possible integer values, the function will always return a random
 * value.
 *
 * @param range a pair of integers representing the inclusive lower and upper bounds of the range.
 * @return a random integer outside the specified range.
 */
fun Random.nextIntExclusive(range: Pair<Int, Int>) =
    nextValueExclusive(range, { Int.MIN_VALUE }, { Int.MAX_VALUE }, Random::nextInt)

/**
 * Returns a random double outside the specified [range], using this [Random] instance.
 *
 * If the range is invalid, i.e., `range.first >= range.second`, this function will return a value
 * of 0.0.
 *
 * @param range a pair of doubles representing the inclusive lower and upper bounds of the range.
 * @return a random double outside the specified range.
 */
fun Random.nextDoubleExclusive(range: Pair<Double, Double>) =
    nextValueExclusive(range, { Double.MIN_VALUE }, { Double.MAX_VALUE }, Random::nextDouble)

/**
 * Returns a list of subsets of a given size, where each subset contains a random selection of
 * elements from the input list.
 *
 * If [exclusive] is `true`, each element is only used once across all subsets.
 * If `false`, an element can be used in multiple subsets.
 *
 * The [size] parameter specifies the number of elements in each subset.
 * It must be at least 1 and at most the size of the input list.
 * If [exclusive] is `true`, the size of the input list must be a multiple of the subset size.
 *
 * Each element in the input list is guaranteed to be included in at least one subset.
 *
 * @param elements the input list of elements to generate subsets from.
 * @param size the size of each subset.
 * @param exclusive whether each element can be used only once across all subsets.
 * @param limit the maximum number of subsets to generate.
 * Default is [Int.MAX_VALUE].
 * @return a list of randomly generated subsets.
 */
fun <T> Random.subsets(
    elements: List<T>,
    size: Int,
    exclusive: Boolean,
    limit: Int = Int.MAX_VALUE
): List<List<T>> {
    enforce {
        "The subset size [$size] must be at least 1 and at most the number of elements " +
                "in the input list [${elements.size}]." {
                    size should IntRequirement.BeInRange(1..elements.size)
                }
        if (exclusive) {
            "The number of elements must be a multiple of the subset size when using " +
                    "exclusive subsets." {
                        requirement { elements.size % size == 0 }
                    }
        }
    }
    // Create an empty list to hold the subsets.
    val subsets = mutableListOf<List<T>>()
    // Create a mutable copy of the input list of elements.
    val remainingElements = LinkedList(elements).also {
        it.shuffle(this)
    }
    var i = 0
    // While there are still elements to use, create subsets.
    while (remainingElements.isNotEmpty() && i < limit) {
        if (exclusive) {
            // If exclusive, takes the first `size` elements from the list.
            // Each element is used only once.
            subsets.add(remainingElements.dropFirst(size))
        } else {
            // If not exclusive, creates a subset of the given size.
            val subset = List(size) {
                // The first of the subset is always the first unused element.
                if (it == 0) {
                    remainingElements.removeFirst()
                } else {
                    // The rest of the elements are chosen randomly from the list of elements.
                    elements.random(this).apply {
                        // Since the element was used, it is removed from the list of remaining
                        // elements.
                        remainingElements.remove(this)
                    }
                }
            }
            subsets.add(subset)
        }
        i++
    }
    return subsets
}
