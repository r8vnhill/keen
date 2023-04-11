package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BeInRange

/***************************************************************************************************
 * This code includes a collection of extensions and functions for different types of collections in
 * Kotlin.
 **************************************************************************************************/

// region : Arrays
/**
 * Transforms the given array into an incremental array, where each value is the
 * sum of the previous values.
 */
fun DoubleArray.incremental() {
    for (i in 1 until this.size) {
        this[i] += this[i - 1]
    }
}
// endregion

// region : Iterable
/**
 * Returns a new list with the subtrahend subtracted from each element.
 */
infix fun Iterable<Double>.sub(subtrahend: Double) = this.map { it - subtrahend }
// endregion

// region : List
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
// endregion

// region : MutableCollection
/**
 * Adds the given element to the collection if it is not already present.
 *
 * @return `true` if the element was added, `false` otherwise.
 */
fun <E> MutableCollection<E>.addIfAbsent(element: E) = !this.contains(element) && this.add(element)

/**
 * Removes the first [size] elements from this linked list and returns them as a list.
 *
 * @param size the number of elements to remove.
 * @return a list containing the removed elements.
 */
fun <E> MutableCollection<E>.removeFirst(size: Int): List<E> =
    take(size).also { removeAll(it.toSet()) } // Use a set to improve performance.
// endregion

// region : MutableList
/**
 * Swaps the elements at the given indices in the receiver.
 */
fun <E> MutableList<E>.swap(i: Int, j: Int) {
    enforce {
        "i should be in range [0, ${this@swap.size})" {
            i should BeInRange(0 until this@swap.size)
        }
        "j should be in range [0, ${this@swap.size})" {
            j should BeInRange(0 until this@swap.size)
        }
    }
    if (i == j) return
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}
// endregion
