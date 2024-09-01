/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.exceptions.InvalidSizeException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

/**
 * A concurrent constructor for generating sequences of values using Kotlin coroutines in an evolutionary algorithm.
 *
 * The `CoroutineConcurrentConstructor` class is an implementation of the [ConstructorExecutor] interface that leverages
 * Kotlin coroutines to generate sequences of values concurrently. This approach is well-suited for parallel processing
 * in large-scale or computationally intensive tasks, making it ideal for evolutionary algorithms where sequence
 * generation can be performed asynchronously.
 *
 * ## Usage:
 * This class is designed to be used in scenarios where concurrent generation of sequences is required. It provides
 * an efficient way to generate elements in parallel by utilizing a [CoroutineScope] and asynchronous tasks.
 *
 * ### Example: Using `CoroutineConcurrentConstructor` to Create a Sequence
 * ```kotlin
 * val constructor = CoroutineConcurrentConstructor<Int>()
 * val sequence = runBlocking {
 *     constructor(5) { index -> index * 2 }
 * }
 * println(sequence) // Output: [0, 2, 4, 6, 8]
 * ```
 *
 * @constructor Initializes a new instance of `CoroutineConcurrentConstructor` with the specified `CoroutineScope`.
 * @param scope The `CoroutineScope` in which the concurrent operations are executed. Defaults to
 *   `CoroutineScope(Dispatchers.Default)`.
 */
class CoroutineConcurrentConstructor<T>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : ConstructorExecutor<T> {

    /**
     * Concurrently generates a sequence of values using coroutines.
     *
     * This `invoke` function is the primary method for generating sequences of values concurrently. It uses the
     * provided `CoroutineScope` to launch asynchronous tasks for each element in the sequence. The function enforces
     * that the size of the sequence must be positive, throwing an [InvalidSizeException] if this constraint is
     * violated.
     *
     * ## Constraints:
     * - **Size Must Be Positive**: The size of the sequence must be greater than 0. If the size is negative, an
     *   [InvalidSizeException] will be thrown.
     *
     * @param size The number of elements to generate in the sequence.
     * @param init A function that takes an index and returns a value of type `T`, used to initialize each element in
     *             the sequence.
     * @return A list of values generated concurrently.
     * @throws CompositeException If any of the constraints are violated.
     * @throws InvalidSizeException If the size of the sequence is negative; wrapped in a [CompositeException].
     */
    override suspend operator fun invoke(size: Int, init: suspend (index: Int) -> T): List<T> {
        constrained {
            "Cannot create a sequence with a negative size."(::InvalidSizeException) {
                size mustNot BeNegative
            }
        }.onLeft { throw it }
        val batchSize = (size / numProcessors).coerceAtLeast(1)
        val ranges = (0 until size).chunked(batchSize)
        // Collect all the deferred results
        val deferredResults = ranges.map { range ->
            scope.async {
                range.map { index -> init(index) }
            }
        }
        // Await all deferred results and then flatten the lists
        return deferredResults.awaitAll().flatten()
    }
}

/**
 * The number of available processors, with platform-specific implementation.
 *
 * The `numProcessors` property is an expected value that retrieves the number of processors (or CPU cores) available
 * on the current platform. The actual implementation of this property will vary depending on the platform (e.g., JVM,
 * JavaScript, Native) to return the appropriate number of processors for that environment.
 *
 * ## Notes:
 * - The value of `numProcessors` may differ significantly across platforms depending on how the property is
 *   implemented.
 * - On platforms without direct support for determining processor count (e.g., JavaScript), a default value is
 *   provided to approximate the level of concurrency.
 *
 * @return The number of available processors as determined by the platform-specific implementation.
 */
internal expect val numProcessors: Int
