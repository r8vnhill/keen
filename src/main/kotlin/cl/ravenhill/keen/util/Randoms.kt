package cl.ravenhill.keen.util

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.requirements.CollectionRequirement.BeEmpty
import cl.ravenhill.enforcer.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.enforcer.requirements.IntRequirement
import cl.ravenhill.enforcer.requirements.IntRequirement.BeAtLeast
import java.util.LinkedList
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
fun Random.nextChar(
    range: CharRange = Char.MIN_VALUE..Char.MAX_VALUE,
    filter: (Char) -> Boolean = { true }
) = generateSequence { range.random(this) }.filter(filter).first()

fun Random.nextString(
    length: Int = nextInt(1, 10),
    range: CharRange = Char.MIN_VALUE..Char.MAX_VALUE,
    filter: (Char) -> Boolean = { true }
) = List(length) { nextChar(range, filter) }.joinToString("")

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
            pickProbability must BeInRange(0.0..1.0)
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
fun Random.indices(size: Int, end: Int, start: Int = 0): List<Int> {
    enforce {
        "The size [$size] must be at most the size of the range [${end - start}]." {
            size must IntRequirement.BeAtMost(end - start)
        }
    }
    val remainingIndices = List(end - start) { start + it }.toMutableList()
    return List(size) {
        remainingIndices.removeAt(nextInt(remainingIndices.size))
    }
}

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
 * ## Examples
 *
 * ### Generate three exclusive subsets of size two from a list of integers:
 *
 * ```
 * val elements = listOf(1, 2, 3, 4, 5, 6)
 * val size = 2
 * val exclusive = true
 * val limit = 3
 * val subsets = Random.subsets(elements, size, exclusive, limit)
 * // subsets: [[2, 6], [4, 3], [5, 1]]
 * ```
 *
 * ### Generate four non-exclusive subsets of size three from a list of strings:
 *
 * ```
 * val elements = listOf("cat", "dog", "fish", "bird", "hamster")
 * val size = 3
 * val exclusive = false
 * val limit = 4
 * val subsets = Random.subsets(elements, size, exclusive, limit)
 * // subsets: [["hamster", "fish", "dog"], ["bird", "fish", "hamster"], ["cat", "dog", "fish"], ["hamster", "cat", "dog"]]
 * ```
 *
 * ### Generate two exclusive subsets of size four from a list of characters:
 *
 * ```
 * val elements = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l')
 * val size = 4
 * val exclusive = true
 * val limit = 2
 * val subsets = Random.subsets(elements, size, exclusive, limit)
 * // subsets: [["h", "j", "l", "f"], ["b", "g", "a", "e"]]
 * ```
 *
 * @param elements the input list of elements to generate subsets from.
 * @param size the size of each subset.
 * @param exclusive whether each element can be used only once across all subsets.
 * @param limit the maximum number of subsets to generate.
 * Default is [Int.MAX_VALUE].
 * @return a list of randomly generated subsets.
 * @throws EnforcementException if the input parameters are invalid.
 */
fun <T> Random.subsets(
    elements: List<T>,
    size: Int,
    exclusive: Boolean,
    limit: Int = Int.MAX_VALUE
): List<List<T>> {
    validateSubsetsInput(elements, size, exclusive, limit)
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
            val subset = createNonExclusiveSubset(elements, remainingElements, size)
            subsets.add(subset)
        }
        i++
    }
    return subsets
}

/**
 * Creates a non-exclusive subset of elements from the given list.
 *
 * @param elements the list of elements to choose from.
 * @param remainingElements the mutable list of remaining elements.
 * @param size the size of the subset to create.
 * @return a non-exclusive subset of elements.
 */
private fun <T> Random.createNonExclusiveSubset(
    elements: List<T>,
    remainingElements: MutableList<T>,
    size: Int
) = List(size) {
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

/**
 * Validates the input parameters for generating subsets.
 *
 * @param elements the input list of elements.
 * @param size the size of each subset.
 * @param exclusive whether each element can be used only once across all subsets.
 * @param limit the maximum number of subsets to generate.
 *
 * @throws EnforcementException if the input parameters are invalid.
 */
private fun <T> validateSubsetsInput(elements: List<T>, size: Int, exclusive: Boolean, limit: Int) =
    enforce {
        if (elements.isEmpty()) {
            "The input list must not be empty." { elements mustNot BeEmpty }
        } else {
            "The subset size [$size] must be at least 1 and at most the number of elements in the input list [${elements.size}]." {
                size must IntRequirement.BeInRange(1..elements.size)
            }
            if (exclusive && size != 0) {
                "The number of elements [${elements.size}] must be a multiple of the subset size [$size] when using exclusive subsets." {
                    requirement { elements.size % size == 0 }
                }
            }
        }
        "The limit [$limit] must be at least 1." { limit must BeAtLeast(1) }
    }
