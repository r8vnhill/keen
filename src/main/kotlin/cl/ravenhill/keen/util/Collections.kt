/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.CollectionRequirement.BeEmpty
import cl.ravenhill.enforcer.requirements.IntRequirement.BeInRange

/*****************************************************************************************
 * This code includes a collection of extensions and functions for different types of
 * collections in Kotlin.
 ****************************************************************************************/

// region : -== ARRAYS ==-
/**
 * Transforms the given array into an incremental array, where each value is the
 * sum of the previous values.
 */
fun DoubleArray.incremental() {
    for (i in 1 until this.size) {
        this[i] += this[i - 1]
    }
}

/**
 * Determines if the elements in the [DoubleArray] are sorted in ascending order.
 *
 * Iterates through the array from the second element to the last. For each element,
 * it compares it with its preceding element. If any element is found to be less than
 * its predecessor, the function returns `false`, indicating the array is not sorted.
 */
fun DoubleArray.isSorted(): Boolean = zip(drop(1)).all { (a, b) -> a <= b }

// endregion ARRAYS

// region : -== ITERABLE ==-
/**
 * Returns a new list with the subtrahend subtracted from each element.
 */
infix fun Iterable<Double>.sub(subtrahend: Double) = this.map { it - subtrahend }

/**
 * Returns a map with the duplicates and their indices of an iterable.
 *
 * @return a map with the duplicates and their indices.
 */
val <T> Iterable<T>.duplicates: Map<T, List<Int>>
    get() = withIndex() // add the index to each element in the iterable
        .groupBy({ it.value }) { it.index } // group the elements by their value and collect their indices
        .filterValues { it.size > 1 } // filter out any values that only appear once
// endregion ITERABLE

// region : -== LIST ==- :
/**
 * Returns a new list containing the elements of this list at the given [indices].
 * The [indices] list may contain duplicate indices, in which case the corresponding elements will
 * be included multiple times.
 *
 * @param indices the list of zero-based indices to retrieve elements from
 * @return a new list containing the elements at the given indices, in the order they appear in the
 *      [indices] list
 * @throws IndexOutOfBoundsException if any index in [indices] is negative or greater than or equal
 *      to the size of this list
 * @see List.get
 */
operator fun <E> List<E>.get(indices: List<Int>) = indices.map { this[it] }

/**
 * Returns a new list that is the transpose of this list of lists.
 *
 * The resulting list has the same number of elements as the sublists of this list, and each element
 * is a list that contains the i-th element of each sublist of this list.
 *
 * ## Example
 *
 * ```
 * val matrix = listOf(
 *     listOf(1, 2, 3),
 *     listOf(4, 5, 6),
 *     listOf(7, 8, 9)
 * )
 *
 * val transposedMatrix = matrix.transpose()
 *
 * println(matrix) // [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
 * println(transposedMatrix) // [[1, 4, 7], [2, 5, 8], [3, 6, 9]]
 * ```
 *
 * ## Note
 *
 * The sublists of this list must all have the same size, i.e. this list must be a valid matrix.
 */
fun <E> List<List<E>>.transpose(): List<List<E>> {
    enforce {
        "All sublists must have the same size" { requirement { all { it.size == first().size } } }
    }
    return when {
        isEmpty() -> emptyList()
        else -> (0 until first().size).map { i -> map { it[i] } }
    }
}
// endregion LIST

// region : -== MUTABLE COLLECTION ==-
/**
 * Adds the given element to the collection if it is not already present.
 *
 * @return `true` if the element was added, `false` otherwise.
 */
fun <E> MutableCollection<E>.addIfAbsent(element: E) =
    !this.contains(element) && this.add(element)

/**
 * Removes the first [n] elements from this collection.
 *
 * @param n the number of elements to remove.
 * @return a list containing the removed elements.
 */
fun <E> MutableList<E>.dropFirst(n: Int): List<E> {
    enforce {
        "Size [$n] should be in range [0, $n]" { n must BeInRange(0..size) }
    }
    return (0 until n).map { removeFirst() }
}
// region : -== LIST ==-

/**
 * Swaps the elements at the given indices in the receiver.
 */
fun <E> MutableList<E>.swap(i: Int, j: Int) {
    enforce {
        "The list must not be empty" { this@swap mustNot BeEmpty }
    }
    enforce {
        "i [$i] should be in range [0, ${this@swap.size})" {
            i must BeInRange(0..<this@swap.size)
        }
        "j [$j] should be in range [0, ${this@swap.size})" {
            j must BeInRange(0..<this@swap.size)
        }
    }
    if (i == j) return
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}
// endregion LIST
// endregion MUTABLE COLLECTION
