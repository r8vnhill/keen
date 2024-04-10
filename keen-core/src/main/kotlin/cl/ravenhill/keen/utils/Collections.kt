/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeAtMost
import cl.ravenhill.jakt.constraints.ints.BeInRange
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException

/**
 * Subtracts a specified value from each element in an iterable of doubles.
 *
 * This extension function simplifies the process of subtracting a constant value (subtrahend) from each element
 * in an iterable collection of doubles (such as a List or Set). The result is a new collection where the specified
 * value has been subtracted from each original element.
 *
 * Example Usage:
 * ```
 * val originalValues = listOf(5.0, 10.0, 15.0)
 * val subtractedValues = originalValues sub 2.0
 * // subtractedValues will be [3.0, 8.0, 13.0]
 * ```
 * In this example, the `sub` function is used with an infix notation to subtract 2.0 from each element of
 * `originalValues`, resulting in a new list `subtractedValues`.
 *
 * @receiver An [Iterable] of [Double] representing the original collection of double values.
 * @param subtrahend The [Double] value to be subtracted from each element in the collection.
 * @return A new [Iterable] of [Double] where each element is the result of the original element minus the subtrahend.
 */
infix fun Iterable<Double>.sub(subtrahend: Double) = this.map { it - subtrahend }

/**
 * Retrieves a sublist from the original list, defined by a specified closed range of indices.
 *
 * ## Constraints:
 * - The provided range must be within the bounds of the list's indices. The start and end points of the range are both
 *   inclusive.
 *
 * ## Usage:
 * This function can be used to easily extract a portion of a list using range notation. It is particularly useful in
 * scenarios where processing or analyzing segments of a list is required.
 *
 * ### Example:
 * ```
 * val list = listOf("a", "b", "c", "d", "e")
 * val sublist = list[1..3] // Returns ["b", "c", "d"]
 * ```
 *
 * @param E the type of elements in the list.
 * @param indices a closed range specifying the start and end indices of the sublist.
 * @return A list containing the elements from the specified range.
 * @receiver List<E> the original list from which the sublist is extracted.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws CollectionConstraintException if the range is not within the list's indices.
 */
@Throws(CompositeException::class, CollectionConstraintException::class)
operator fun <E> List<E>.get(indices: ClosedRange<Int>): List<E> {
    constraints {
        "The list must not be empty" { this@get mustNot BeEmpty }
        "The start index (${indices.start}) must be in range ${this@get.indices}" {
            indices.start must BeInRange(this@get.indices)
        }
        "The end index (${indices.endInclusive}) must be in range ${this@get.indices}" {
            indices.endInclusive must BeInRange(this@get.indices)
        }
        "The start index (${indices.start}) must be less than or equal to the end index (${indices.endInclusive})" {
            indices.start must BeAtMost(indices.endInclusive)
        }
    }
    return subList(indices.start, indices.endInclusive + 1)
}

/**
 * Transforms a list of [Double] values into an incremental list where each element is the cumulative sum of all
 * preceding elements in the original list.
 *
 * The function iteratively adds each element in the list to a running total, forming a new list where each element
 * represents the sum of all prior elements up to and including itself in the original list.
 *
 * ## Example Usage:
 * ```
 * val originalList = listOf(1.0, 2.0, 3.0)
 * val incrementalList = originalList.incremental() // Result: [1.0, 3.0, 6.0]
 * ```
 * In this example, the first element in `incrementalList` is 1.0 (same as the original list), the second element is
 * 1.0 + 2.0 = 3.0, and the third element is 1.0 + 2.0 + 3.0 = 6.0.
 *
 * @return A [List<Double>] where each element is the cumulative sum of the elements from the original list up to that
 *   point.
 */
fun List<Double>.incremental() = this.scan(0.0) { sum, element -> sum + element }.drop(1)

/**
 * Transposes a list of lists, converting rows into columns and vice versa.
 *
 * This function rearranges a list of lists (such as a matrix) by swapping rows and columns. Each row in the original
 * list becomes a column in the transposed list, and vice versa. For the transpose operation to be valid, all inner
 * lists must have the same size.
 *
 * ## Constraints:
 * - All inner lists must have the same size.
 * - If the outer list is empty, the function returns an empty list, as there is no content to transpose.
 *
 * ## Usage:
 * This method is particularly useful in mathematical contexts or when working with matrix-like structures, where
 * the transposition of data is required.
 *
 * ### Example:
 * ```
 * val matrix = listOf(
 *     listOf(1, 2, 3),
 *     listOf(4, 5, 6),
 *     listOf(7, 8, 9)
 * )
 * val transposed = matrix.transpose()
 * // transposed will be: listOf(listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9))
 * ```
 *
 * @param E The type of elements in the inner lists.
 * @return A list of lists where the original rows are now columns and the original columns are now rows.
 * @throws CompositeException if the inner lists have different sizes.
 */
@Throws(CompositeException::class, CollectionConstraintException::class)
fun <E> List<List<E>>.transpose(): List<List<E>> {
    constraints {
        "All lists must have the same size" {
            // If the list is empty, it is considered to be transposed
            distinctBy { it.size } must HaveSize { it in 0..1 }
        }
    }
    return when {
        isEmpty() -> emptyList()
        else -> this[0].indices.map { index -> map { it[index] } }
    }
}

/**
 * Swaps the elements at indices `i` and `j` in the MutableList. This function is generic and can be used with any type
 * of MutableList.
 *
 * ## Constraints:
 * - The list must not be empty. This is checked before performing the swap.
 * - Both indices `i` and `j` must be within the valid range of the list's indices, i.e., between `0` and
 *   `list.size - 1`.
 *
 * ## Usage:
 * This function is useful when you need to swap two elements in a mutable list without creating a new list or using
 * additional space.
 *
 * ### Example 1: Swapping elements in an integer list
 * ```
 * val list = mutableListOf(1, 2, 3)
 * list.swap(0, 2) // list becomes [3, 2, 1]
 * ```
 *
 * ### Example 2: Swapping elements in a string list
 * ```
 * val list = mutableListOf("apple", "banana", "cherry")
 * list.swap(1, 2) // list becomes ["apple", "cherry", "banana"]
 * ```
 *
 * @param E the type of elements in the list.
 * @param i the index of the first element to swap.
 * @param j the index of the second element to swap.
 * @receiver the mutable list on which the swap operation will be performed.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws CollectionConstraintException if the list is empty.
 * @throws IntConstraintException if either `i` or `j` are out of range.
 */
@Throws(CompositeException::class, CollectionConstraintException::class, IntConstraintException::class)
fun <E> MutableList<E>.swap(i: Int, j: Int) {
    constraints {
        "The list must not be empty" { this@swap mustNot BeEmpty }
        "The first index ($i) must be in range ${this@swap.indices}" { i must BeInRange(this@swap.indices) }
        "The second index ($j) must be in range ${this@swap.indices}" { j must BeInRange(this@swap.indices) }
    }
    if (i != j) {
        this[i] = this[j].also { this[j] = this[i] }
    }
}
