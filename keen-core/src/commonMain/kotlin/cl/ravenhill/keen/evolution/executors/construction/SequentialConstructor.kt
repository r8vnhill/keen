/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.exceptions.InvalidSizeException

/**
 * A sequential constructor for creating a list of elements in a specified order.
 *
 * The `SequentialConstructor` class implements the [ConstructorExecutor] interface, providing a straightforward
 * approach to constructing a list of elements in a sequential manner. This class ensures that elements are generated
 * and initialized in the exact order of their indices, making it ideal for scenarios where the order of elements is
 * crucial, such as constructing chromosomes, genotypes, or other ordered data structures in evolutionary algorithms.
 *
 * ## Usage:
 * The `SequentialConstructor` is best used when you need to create a list of elements where the order of construction
 * is significant. This constructor generates elements one after the other, ensuring that the list is built in a
 * defined sequence. It is particularly useful when the number of elements is manageable and the order is essential.
 * If performance is a concern, and you need to generate a large number of elements concurrently, consider using the
 * [CoroutineConcurrentConstructor] instead.
 *
 * ### Example:
 * ```kotlin
 * suspend fun example() {
 *     val list = SequentialConstructor(10) { index -> index * 2 }
 *     println(list) // Output: [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]
 * }
 * ```
 *
 * In this example, the `SequentialConstructor` is used to create a list of integers where each element is twice its
 * index. The list is constructed sequentially, ensuring that the elements are added in the correct order.
 *
 * @param T The type of elements to be constructed.
 * @throws InvalidSizeException if the size of the list is negative.
 */
class SequentialConstructor<T> : ConstructorExecutor<T> {

    /**
     * Constructs a list of elements sequentially, initializing each element using the provided function.
     *
     * @param size The number of elements to construct. Must be a non-negative integer.
     * @param init A suspending function that takes an index as input and returns an element of type [T]. This function
     *   is responsible for initializing or creating each element in the list.
     * @return A list of elements of type [T], where each element is initialized according to the provided [init]
     *   function.
     * @throws CompositeException if any of the constraints are violated.
     */
    override suspend operator fun invoke(size: Int, init: suspend (index: Int) -> T): List<T> {
        constrained {
            "Cannot create a list with a negative size"(::InvalidSizeException) { size must BePositive }
        }.onLeft { throw it }
        return List(size) { index -> init(index) }
    }

    /**
     * Companion object to provide a convenient way to create a `SequentialConstructor` instance.
     *
     * @see invoke
     */
    companion object {
        /**
         * Creates a list of elements sequentially using the `SequentialConstructor`.
         *
         * This method provides a convenient way to use the `SequentialConstructor` without directly instantiating
         * the class. It sequentially constructs a list of elements by applying the provided initialization function
         * to each index in the list.
         *
         * @param size The number of elements to construct. Must be a non-negative integer.
         * @param init A suspending function that takes an index as input and returns an element of type [T].
         * @return A list of elements of type [T].
         * @throws InvalidSizeException if the size of the list is negative.
         */
        suspend operator fun <T> invoke(size: Int, init: suspend (index: Int) -> T): List<T> =
            SequentialConstructor<T>()(size, init)
    }
}
