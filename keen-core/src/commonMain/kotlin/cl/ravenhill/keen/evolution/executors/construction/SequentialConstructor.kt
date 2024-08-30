/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.exceptions.InvalidSizeException
import kotlinx.coroutines.coroutineScope

/**
 * A sequential constructor for creating lists of elements in an evolutionary algorithm.
 *
 * The `SequentialConstructor` class implements the `ConstructorExecutor` interface and provides a mechanism to
 * sequentially construct a list of elements by invoking an initializer function for each index. This class ensures
 * that the size of the list is non-negative, adhering to the constraint that lists cannot be created with a negative
 * size.
 *
 * ## Usage:
 * The `SequentialConstructor` class is used in scenarios where a list of elements needs to be created based on an
 * initialization function. The initialization function is invoked for each index from `0` to `size - 1`, and the
 * resulting elements are collected into a list.
 *
 * ### Example 1: Creating a List of Integers
 * ```kotlin
 * val constructor = SequentialConstructor<Int>()
 * val list = constructor(5) { it * 2 }
 * println(list) // Output: [0, 2, 4, 6, 8]
 * ```
 *
 * @param T The type of the elements in the list.
 */
class SequentialConstructor<T> : ConstructorExecutor<T> {

    /**
     * Invokes the constructor to create a list of elements.
     *
     * This method sequentially constructs a list by invoking the provided `init` function for each index from `0`
     * to `size - 1`. The resulting elements are collected into a list. If the size is negative, a constraint
     * violation occurs, and the list creation is aborted.
     *
     * @param size The size of the list to create. Must be non-negative.
     * @param init The initializer function that generates an element for each index.
     * @return A list of elements created by the `init` function.
     * @throws CompositeException containing the constraint violations.
     */
    override suspend operator fun invoke(size: Int, init: suspend (index: Int) -> T): List<T> {
        constrained {
            "Cannot create a list with a negative size"(::InvalidSizeException) { size must BePositive }
        }.onLeft { throw it }
        return List(size) { index -> init(index) }
    }
}
