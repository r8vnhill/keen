package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement
import java.util.LinkedList
import java.util.stream.IntStream
import kotlin.random.Random

/**
 * Generates a random "printable" character.
 */
fun Random.nextChar(filter: (Char) -> Boolean = { true }) =
    generateSequence { (' '..'z').random(this) }.filter(filter).first()


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
 * Returns an array with the indices of a random subset of the given size.
 *
 * @receiver the random instance.
 * @param from the size of the set.
 * @param pick the number of elements to pick from the set.
 * @return an array with the indices of a subset of the given size.
 */
fun Random.subset(pick: Int, from: Int): IntArray =
    ints(0, from)
        .limit(pick.toLong())
        .sorted()
        .toArray()

/**
 * Returns a sequence of random indices.
 *
 * @receiver the random number generator.
 * @param pickProbability the probability of picking an index.
 * @param end the end of the range.
 * @param start the start of the range. Defaults to 0.
 */
fun Random.indices(pickProbability: Double, end: Int, start: Int = 0): List<Int> {
    enforce {
        pickProbability should BeInRange(0.0..1.0)
    }
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
 * Returns a random integer outside the given range.
 */
fun Random.nextIntOutsideOf(range: Pair<Int, Int>) =
    when {
        nextBoolean() && range.first > Int.MIN_VALUE -> {
            (Int.MIN_VALUE until range.first).random(this)
        }

        range.second < Int.MAX_VALUE -> (range.second + 1..Int.MAX_VALUE).random(this)
        else -> (Int.MIN_VALUE until range.first).random(this)
    }


fun Random.nextDoubleOutsideOf(range: Pair<Double, Double>): Double {
    val (min, max) = range
    return when {
        nextBoolean() && min > Double.MIN_VALUE -> {
            this.nextDouble(Double.MIN_VALUE, min)
        }

        max < Double.MAX_VALUE -> this.nextDouble(max, Double.MAX_VALUE)
        else -> this.nextDouble(Double.MIN_VALUE, min)
    }
}

/**
 * Returns a list of lists of the given size, where each list contains a random subset of the given
 * elements.
 *
 * The subsets can be exclusive or not exclusive.
 * If exclusive, an element can only be used in one subset.
 * If not exclusive, an element can be used in multiple subsets.
 * For this reason, the number of elements must be a multiple of the subset size when using
 * exclusive subsets.
 *
 * This method ensures that each element is used at least once in a subset.
 *
 * @param elements the list of elements to use when creating the subsets.
 * @param exclusive determines whether the same element can be used in more than one subset.
 *      If true, an element can only be used in one subset. If false, an element can be used in
 *      multiple subsets.
 * @param size the size of each subset.
 */
fun <T> Random.subsets(elements: List<T>, exclusive: Boolean, size: Int): List<List<T>> {
    enforce {
        size should IntRequirement.BeInRange(1..elements.size)
        if (exclusive) {
            requirement(
                "The number of elements must be a multiple of the subset size when using " +
                        "exclusive subsets."
            ) { elements.size % size == 0 }
        }
    }

    // Create an empty list to hold the subsets.
    val subsets = mutableListOf<List<T>>()

    // Create a mutable copy of the input list of elements.
    val remainingElements = LinkedList(elements).also {
        it.shuffle(this)
    }

    // While there are still elements to use, create subsets.
    while (remainingElements.isNotEmpty()) {
        if (exclusive) {
            // If exclusive, takes the first ``size`` elements from the list.
            // Each element is used only once.
            subsets.add(remainingElements.removeFirst(size))
        } else {
            // If not exclusive, creates a subset of the given size.
            val subset = List(size) {
                // The first of the subset is always the first unused element.
                if (it == 0) {
                    remainingElements.removeFirst()
                } else {
                    // The rest of the elements are chosen randomly from the list of elements.
                    elements.random(Core.random).apply {
                        // Since the element was used, it is removed from the list of remaining
                        // elements.
                        remainingElements.remove(this)
                    }
                }
            }
            subsets.add(subset)
        }
    }
    return subsets
}

/**
 * Removes and returns the first ``size`` elements from the list.
 */
private fun <E> LinkedList<E>.removeFirst(size: Int): List<E> {
    val list = mutableListOf<E>()
    repeat(size) {
        list.add(this.removeFirst())
    }
    return list
}
