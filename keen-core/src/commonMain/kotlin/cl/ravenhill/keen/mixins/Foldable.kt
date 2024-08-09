/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

/**
 * Represents a foldable structure that can be reduced to a single value.
 *
 * The `Foldable` interface defines a contract for data structures that support the fold operation, which reduces the
 * structure to a single value by iteratively applying a binary operation to its elements and an accumulator. The
 * operation takes an initial value and processes each element of the structure, producing a final accumulated result.
 *
 * ## Usage:
 * This interface is intended to be implemented by data structures that can be folded, such as lists, trees, or other
 * collections. The `fold` method allows for flexible accumulation of results based on the provided operation.
 *
 * ### Example:
 * Implementing `Foldable` for a simple list-like structure:
 * ```kotlin
 * class SimpleList<T>(private val elements: List<T>) : Foldable<T> {
 *     override fun <R> fold(initial: R, operation: (R, T) -> R): R {
 *         var result = initial
 *         for (element in elements) {
 *             result = operation(result, element)
 *         }
 *         return result
 *     }
 * }
 *
 * // Example usage:
 * val numbers = SimpleList(listOf(1, 2, 3, 4))
 * val sum = numbers.fold(0) { acc, i -> acc + i } // sum = 10
 * ```
 *
 * @param T The type of elements contained in the structure.
 */
interface Foldable<T> {

    /**
     * Folds the elements of the structure into a single value.
     *
     * The `fold` method iteratively applies the provided binary operation to the elements of the structure,
     * starting with the initial value and processing each element to produce a final accumulated result.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each element of the structure.
     * @return The final accumulated result after all elements have been processed.
     */
    fun <R> fold(initial: R, operation: (R, T) -> R): R
}
