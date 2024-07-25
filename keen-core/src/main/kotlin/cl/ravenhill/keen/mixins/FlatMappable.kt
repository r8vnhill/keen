package cl.ravenhill.keen.mixins

/**
 * Represents a type that can be flat-mapped and flattened.
 *
 * The `FlatMappable` interface provides a contract for types that support the `flatMap` and `flatten` operations. These
 * operations are useful in scenarios where a transformation or flattening of nested structures is required.
 *
 * ## Usage:
 * This interface is typically implemented by classes that hold or manage collections of elements, allowing for
 * flexible transformation and flattening of these collections.
 *
 * ### Example:
 * ```
 * class MyFlatMappable<T>(private val elements: List<T>) : FlatMappable<T> {
 *     override fun <R> flatMap(transform: (T) -> R): List<R> {
 *         return elements.map(transform)
 *     }
 *
 *     override fun flatten(): List<T> {
 *         return elements.flatten()
 *     }
 * }
 * ```
 * In this example, `MyFlatMappable` implements the `FlatMappable` interface, providing specific behavior for the
 * `flatMap` and `flatten` methods.
 *
 * @param T The type of elements contained in this `FlatMappable`.
 */
interface FlatMappable<T> {

    /**
     * Applies a transformation function to each element and returns a list of the results.
     *
     * The `flatMap` method applies the provided transformation function to each element in the collection, producing
     * a new list of transformed elements.
     *
     * ### Example:
     * ```
     * val flatMappable = MyFlatMappable(listOf(1, 2, 3))
     * val result = flatMappable.flatMap { it * 2 } // Returns [2, 4, 6]
     * ```
     * In this example, the `flatMap` method is used to double each element in the collection.
     *
     * @param R The type of elements in the resulting list.
     * @param transform The transformation function to apply to each element.
     * @return A list of transformed elements.
     */
    fun <R> flatMap(transform: (T) -> R): List<R>

    /**
     * Flattens a nested structure into a single list of elements.
     *
     * The `flatten` method is used to convert a nested collection into a single flat list of elements. It is
     * particularly useful for collections of collections, where all nested elements need to be combined into a single
     * list.
     *
     * ### Example:
     * ```
     * val flatMappable = MyFlatMappable(listOf(listOf(1, 2), listOf(3, 4)))
     * val result = flatMappable.flatten() // Returns [1, 2, 3, 4]
     * ```
     * In this example, the `flatten` method is used to combine nested lists into a single flat list.
     *
     * @return A flat list of elements.
     */
    fun flatten(): List<T>
}
