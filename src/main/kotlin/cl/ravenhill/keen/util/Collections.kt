package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BeInRange

/***************************************************************************************************
 * This code includes a collection of extensions and functions for different types of collections in
 * Kotlin.
 * It includes an extension function for incrementally adding the previous value of each element in
 * a DoubleArray, a function for subtracting a given value from each element in an Iterable<Double>,
 * a function for retrieving elements from a List at given indices, a function for adding an element
 * to a MutableCollection only if it does not already exist, and a function for swapping elements at
 * given indices in a MutableList.
 * The code also includes some documentation for each function, indicating what each function does
 * and how it can be used.
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
fun <E> MutableCollection<E>.addIfAbsent(element: E): Boolean {
    if (element in this) {
        return false
    }
    return this.add(element)
}
// endregion

// region : MutableList
/**
 * Swaps the elements at the given indices in the receiver.
 */
fun <E> MutableList<E>.swap(i: Int, j: Int) {
    enforce {
        i should BeInRange(0 until this@swap.size) {
            "i should be in range [0, ${this@swap.size})"
        }
        j should BeInRange(0 until this@swap.size) {
            "j should be in range [0, ${this@swap.size})"
        }
    }
    if (i == j) return
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}
// endregion
