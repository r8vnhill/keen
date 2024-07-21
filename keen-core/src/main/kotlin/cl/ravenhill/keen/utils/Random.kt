/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.jakt.constraints.ints.BeAtMost
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import java.util.*
import kotlin.random.Random
import cl.ravenhill.jakt.constraints.ints.BeInRange as IntBeInRange

/**
 * Generates a random `Char` within a specified range, with an optional filtering condition.
 *
 * This extension function for the `Random` class provides a way to generate a random character within a given range.
 * An optional filter function can be provided to exclude certain characters from the selection. The function repeatedly
 * generates random characters within the range until one satisfies the filter condition.
 *
 * ## Example:
 * ```
 * val randomChar = Random.nextChar('a'..'z') // Random lowercase letter
 * val digit = Random.nextChar() { it.isDigit() } // Random digit
 * ```
 * In the first example, `randomChar` will be a random lowercase letter.
 * In the second example, `digit` will be a random digit, although the range already ensures that.
 *
 * @param range The range of characters to select from. Defaults to the full range of `Char` from `Char.MIN_VALUE` to
 *   `Char.MAX_VALUE`.
 * @param filter An optional lambda function that returns `true` for acceptable characters and `false` for those that
 *   should be excluded. Defaults to a lambda that accepts all characters.
 * @return A randomly generated character that falls within the specified range and satisfies the filter condition.
 */
fun Random.nextChar(range: ClosedRange<Char> = Char.MIN_VALUE..Char.MAX_VALUE, filter: (Char) -> Boolean = { true }) =
    generateSequence { (range.start..range.endInclusive).random(this) }.filter(filter).first()

/**
 * Generates a random double within a specified range.
 *
 * This extension function for [Random] simplifies the generation of a random double value that falls within
 * a specific closed range. The range is defined by a [ClosedRange]<[Double]>, and the generated value is
 * guaranteed to be within the range's start and end, inclusive.
 *
 * ## Usage:
 * This function can be used wherever a random double value is needed within a specific range. It is particularly
 * useful when dealing with operations that require random numbers with upper and lower bounds.
 *
 * ### Example:
 * ```
 * val random = Random.Default
 * val range = 1.0..10.0
 *
 * // Generating a random double within the range of 1.0 to 10.0 (inclusive)
 * val randomValue = random.nextDoubleInRange(range)
 * ```
 * In this example, `randomValue` will be a double between 1.0 and 10.0, including the boundaries.
 *
 * @receiver [Random] The random number generator.
 * @param range The range within which to generate the random double. It is a [ClosedRange<Double>]
 *   indicating the lower and upper bounds for the random value.
 * @return A random double value that falls within the specified range.
 */
fun Random.nextDoubleInRange(range: ClosedRange<Double>) = nextDouble(range.start, range.endInclusive)

/**
 * Generates a random integer within a specified range.
 *
 * This extension function for the `Random` class provides an easy way to generate a random integer within
 * a given closed range. The range is defined by a `ClosedRange<Int>`, which includes both the start and end values.
 *
 * ## Usage:
 * This function is useful in scenarios where a random integer is needed within specific bounds. It can be particularly
 * helpful in simulations, randomized algorithms, or any context where random but bounded integer values are required.
 *
 * ### Example:
 * ```kotlin
 * val random = Random.Default
 * val range = 1..10
 *
 * // Generating a random integer within the range of 1 to 10 (inclusive)
 * val randomValue = random.nextIntInRange(range)
 * ```
 * In this example, `randomValue` will be an integer between 1 and 10, inclusive of both the boundaries.
 *
 * @param range The range within which to generate the random integer. It is a `ClosedRange<Int>` representing the
 *   lower and upper bounds for the random value.
 * @return A random integer value that falls within the specified range.
 */
fun Random.nextIntInRange(range: ClosedRange<Int>): Int {
    constraints { "Cannot generate a random integer within an empty range" { constraint { !range.isEmpty() } } }
    return nextInt(range.start, range.endInclusive)
}

/**
 * Generates a list of indices based on a specified probability.
 *
 * This function extends the functionality of the `Random` class to provide a way to randomly select indices from a
 * specified range. Each index in the range has a chance of being selected based on the given probability.
 *
 * ## Constraints:
 * - The pick probability must be within the range [0.0, 1.0].
 *
 * ## Process:
 * - Iterates over each index in the specified range (from `start` to `end`).
 * - For each index, it uses the `nextDouble()` method to generate a random double. If this value is less than the
 *   `pickProbability`, the index is included in the resulting list.
 *
 * ## Usage:
 * This method can be used in scenarios where a subset of indices needs to be randomly selected from a range, such as
 * in evolutionary algorithm operations like crossover and mutation.
 *
 * ### Example:
 * ```kotlin
 * val random = Random.Default
 * val pickProbability = 0.3
 * val indices = random.indices(pickProbability, end = 10)
 * // 'indices' will contain a random subset of indices from 0 to 9, each with a 30% chance of being included.
 * ```
 *
 * @param pickProbability The probability with which each index in the range is picked. Must be between 0.0 and 1.0.
 * @param end The exclusive upper bound of the index range.
 * @param start The inclusive lower bound of the index range. Defaults to 0.
 * @return A list of indices, each selected based on the specified probability.
 */
fun Random.indices(pickProbability: Double, end: Int, start: Int = 0): List<Int> {
    constraints {
        "The pick probability ($pickProbability) must be in the range ${0.0..1.0}" {
            pickProbability must BeInRange(0.0..1.0)
        }
        "The end ($end) must be greater than or equal to the start ($start)" { start must BeAtMost(end - 1) }
        "The end index ($end) must be greater than or equal to 0" { end mustNot BeNegative }
        "The start index ($start) must be greater than or equal to 0" { start mustNot BeNegative }
    }
    // Selects the indices using the provided probability.
    return (start..<end).filter { nextDouble() < pickProbability }
}

/**
 * Generates a list of unique random indices within a specified range. This function is an extension of the `Random`
 * class.
 *
 * ## Process:
 * 1. Validates that the requested number of indices ([size]) does not exceed the total number of possible indices
 *   within the specified range ([start] to [end]).
 * 2. Creates a list of all possible indices within the range.
 * 3. Randomly selects `size` indices from this list, ensuring each index is unique.
 *
 * ## Constraints:
 * - The number of indices requested (`size`) must be less than or equal to the total number of indices in the specified
 *   range. This is to prevent duplicates and ensure the uniqueness of each index.
 *
 * ## Usage:
 * This function can be used in scenarios where a set of unique random indices is required from a specific range, such
 * as for random sampling, shuffling elements, or generating random subsets from a larger set.
 *
 * ### Example:
 * ```
 * val random = Random()
 * val randomIndices = random.indices(size = 3, end = 10)
 * // Generates a list of 3 unique random indices from 0 to 9
 * ```
 *
 * @param size The number of unique indices to generate.
 * @param end The exclusive upper bound of the index range.
 * @param start The inclusive lower bound of the index range, defaulting to 0.
 * @return A list of unique indices, each within the specified range.
 * @receiver The instance of `Random` used to generate the indices.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws IntConstraintException if the size exceeds the number of possible indices.
 */
fun Random.indices(size: Int, end: Int, start: Int = 0): List<Int> {
    constraints {
        "The size ($size) must be greater than or equal to 0" { size mustNot BeNegative }
        "The size ($size) must be at most the size of the range (${end - start})." {
            size must BeAtMost(end - start)
        }
        "The end index ($end) must be greater than or equal to 0." { end mustNot BeNegative }
        "The start index ($start) must be greater than or equal to 0." { start mustNot BeNegative }
        "The start index ($start) must be less than the end index ($end)." { start must BeAtMost(end - 1) }
    }
    val remainingIndices = List(end - start) { start + it }.toMutableList()
    return List(size) {
        remainingIndices.removeAt(nextInt(remainingIndices.size))
    }.sorted()
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
 * // subsets: [
 * //     ["hamster", "fish", "dog"], ["bird", "fish", "hamster"], ["cat", "dog", "fish"], ["hamster", "cat", "dog"]
 * // ]
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
 * @throws CompositeException if the input parameters are invalid.
 */
fun <T> Random.subsets(
    elements: List<T>,
    size: Int,
    exclusive: Boolean,
    limit: Int = Int.MAX_VALUE,
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
            subsets += remainingElements.dropFirst(size)
        } else {
            // If not exclusive, creates a subset of the given size.
            val subset = createNonExclusiveSubset(elements, remainingElements, size)
            subsets += subset
        }
        i++
    }
    return subsets
}

fun <E> MutableList<E>.dropFirst(n: Int): List<E> {
    constraints { "Size [$n] should be in range [0, $n]" { n must IntBeInRange(0..size) } }
    return (0 until n).map { removeFirst() }
}

/**
 * Generates a non-exclusive subset from a list of elements.
 *
 * This function creates a subset of a specified size from the given list. The subsets generated are non-exclusive,
 * meaning elements can appear in multiple subsets. The function ensures that each element is used at most once across
 * all generated subsets by maintaining a list of remaining elements that have not been included in any subset.
 *
 * ## Process:
 * - The first element in each subset is always the first unused element from the list of remaining elements.
 * - The subsequent elements in the subset are selected randomly from the entire list of elements.
 * - After an element is chosen, it is removed from the list of remaining elements to avoid repetition.
 *
 * ## Usage:
 * This function is typically used when a series of subsets need to be generated from a larger set, and the subsets
 * should cover all elements without repeating any element within the same subset.
 *
 * @param elements The list of elements to generate subsets from.
 * @param remainingElements A mutable list of elements that have not been used in any subset.
 * @param size The size of the subset to generate.
 * @return A list representing the generated subset.
 * @throws IllegalArgumentException if `remainingElements` is empty or if `size` exceeds the number of remaining
 *   elements.
 */
private fun <T> Random.createNonExclusiveSubset(
    elements: List<T>,
    remainingElements: MutableList<T>,
    size: Int,
) = List(size) {
    // The first of the subset is always the first unused element.
    if (it == 0) {
        remainingElements.removeFirst()
    } else {
        // The rest of the elements are chosen randomly from the list of elements.
        elements.random(this).apply {
            // Since the element was used, it is removed from the list of remaining elements.
            remainingElements.remove(this)
        }
    }
}

/**
 * Validates the input parameters for generating subsets of a list.
 *
 * This function ensures that the input parameters for subset generation meet specific constraints. It checks the
 * validity of the input list, the subset size, the exclusivity condition, and the limit on the number of subsets.
 *
 * ## Validation Checks:
 * - **Non-empty List**: The input list must not be empty.
 * - **Positive Subset Size**: The subset size must be a positive integer.
 * - **Exclusive Subsets**: If the subsets are meant to be exclusive (non-overlapping), the subset size must not
 *   exceed the size of the input list. Additionally, the total size of the input list must be a multiple of the
 *   subset size.
 * - **Valid Limit**: The limit on the number of generated subsets must be at least 1.
 *
 * ## Usage:
 * This function is typically invoked internally before generating subsets of a list to ensure that the input
 * parameters are valid. It throws an exception if any of the validation checks fail.
 *
 * @param elements The input list from which subsets are to be generated.
 * @param size The size of each subset.
 * @param exclusive A boolean flag indicating whether the subsets should be exclusive (non-overlapping).
 * @param limit The maximum number of subsets to generate.
 * @throws CompositeException containing specific constraint exceptions if any validation checks fail.
 */
@Throws(
    CompositeException::class,
    CollectionConstraintException::class,
    IntConstraintException::class,
    ConstraintException::class
)
private fun <T> validateSubsetsInput(elements: List<T>, size: Int, exclusive: Boolean, limit: Int) =
    constraints {
        if (elements.isEmpty()) {
            "The input list must not be empty." { elements mustNot BeEmpty }
        } else {
            "The subset size [$size] must be at least 1" {
                size must BePositive
            }
            if (exclusive) {
                "The subset size [$size] must be at most the size of the input list [${elements.size}]." {
                    size must BeAtMost(elements.size)
                }
                if (size != 0) {
                    "Subset count [${elements.size}] must be a multiple of size [$size] for exclusivity." {
                        constraint { elements.size % size == 0 }
                    }
                }
            }
        }
        "The limit [$limit] must be at least 1." { limit must BeAtLeast(1) }
    }
