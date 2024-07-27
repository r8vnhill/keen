/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

/**
 * Represents an entity that can be flattened and mapped over.
 *
 * The `FlatMappable` interface defines the structure for an entity that supports flattening and flat-mapping
 * operations. This is particularly useful in evolutionary algorithms and other contexts where hierarchical or nested
 * structures need to be processed.
 *
 * ## Usage:
 * Implement this interface in classes where flattening and flat-mapping functionality is required. The `flatten` method
 * should be implemented to return a list of elements, and the `flatMap` method provides a default implementation that
 * applies a transformation function to each flattened element.
 *
 * ### Example:
 * ```kotlin
 * class TreeNode<T>(val value: T, val children: List<TreeNode<T>>) : FlatMappable<T> {
 *     override fun flatten() = listOf(value) + children.flatMap { it.flatten() }
 * }
 *
 * val tree = TreeNode(1, listOf(TreeNode(2, emptyList()), TreeNode(3, emptyList())))
 * val flattened = tree.flatten() // [1, 2, 3]
 * val flatMapped = tree.flatMap { listOf(it, it * 10) } // [1, 10, 2, 20, 3, 30]
 * ```
 *
 * @param T The type of elements in the flattenable structure.
 */
interface FlatMappable<out T> {

    /**
     * Flattens the structure into a list of elements.
     *
     * This method should be implemented to return a list of elements from the hierarchical or nested structure.
     *
     * @return A list of elements after flattening the structure.
     */
    fun flatten(): List<T>

    /**
     * Applies a transformation function to each flattened element and returns a list of the results.
     *
     * This method provides a default implementation that first flattens the structure and then applies the
     * transformation function to each element in the flattened list.
     *
     * @param R The type of elements in the resulting list.
     * @param f The transformation function to apply to each flattened element.
     * @return A list of transformed elements.
     */
    fun <R> flatMap(f: (T) -> List<R>) = flatten().flatMap(f)
}
