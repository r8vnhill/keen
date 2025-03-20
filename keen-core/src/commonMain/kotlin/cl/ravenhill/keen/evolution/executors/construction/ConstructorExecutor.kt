/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

/**
 * Interface for constructing a list of elements either concurrently or sequentially.
 *
 * The `ConstructorExecutor` interface provides a blueprint for creating lists of elements of type [T]. Implementations
 * of this interface can determine whether the construction process occurs sequentially or concurrently. This is
 * particularly valuable in scenarios like evolutionary algorithms, where elements such as chromosomes or genotypes are
 * generated or initialized based on their index in the list.
 *
 * ## Usage:
 * To use the `ConstructorExecutor`, implement the interface and define the [invoke] method to control how elements
 * are constructed. The method generates a list of elements, where each element is initialized by a provided function.
 *
 * ### Example Implementations:
 * - **Sequential Execution**:
 * ```kotlin
 * class SequentialConstructorExecutor<T> : ConstructorExecutor<T> {
 *     override suspend fun invoke(size: Int, init: suspend (index: Int) -> T) =
 *         List(size) { index -> init(index) }
 * }
 * ```
 *
 * - **Concurrent Execution**:
 * ```kotlin
 * class ConcurrentConstructorExecutor<T> : ConstructorExecutor<T> {
 *     override suspend fun invoke(size: Int, init: suspend (index: Int) -> T): List<T> =
 *         coroutineScope {
 *             (0 until size).map { index ->
 *                 async { init(index) }
 *             }.awaitAll()
 *         }
 * }
 * ```
 *
 * In the examples above, `SequentialConstructorExecutor` constructs the list elements one after the other, while
 * `ConcurrentConstructorExecutor` uses coroutines to construct elements in parallel, potentially improving performance
 * in scenarios with expensive initialization logic.
 *
 * @param T The type of elements that the list will contain.
 */
interface ConstructorExecutor<T> {

    /**
     * Constructs a list of elements of the specified size, initializing each element using the provided function.
     *
     * This method generates a list of [size] elements, with each element created by invoking the provided [init]
     * function at its corresponding index. The execution strategy—whether sequential or concurrent—is determined by
     * the implementation of the interface.
     *
     * @param size The number of elements to construct.
     * @param init A suspending function that takes an index as input and returns an element of type [T]. This function
     *   is responsible for initializing or creating each element in the list.
     * @return A list of elements of type [T], where each element is initialized according to the provided [init]
     *   function.
     */
    suspend operator fun invoke(size: Int?, init: suspend (index: Int) -> T): List<T>
}
