/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators

import arrow.core.Either
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.OperatorInvocationException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import kotlin.random.Random

/**
 * Represents a generic operator in an evolutionary algorithm that supports both synchronous and asynchronous execution.
 *
 * The `Operator` interface defines a contract for operators that can be applied to an evolutionary state within an
 * evolutionary algorithm. These operators are responsible for transforming the state, typically by selecting, mutating,
 * or recombining individuals in the population to produce a new state. The interface supports both synchronous and
 * asynchronous operations, thanks to the use of Kotlin's `suspend` functions.
 *
 * ## Usage:
 * Implement this interface to create custom operators that manipulate the evolutionary state. The `invoke` function,
 * marked as `suspend`, allows the operation to be performed asynchronously if needed, but can also be used in a
 * synchronous context without requiring asynchronous behavior. This flexibility makes the interface suitable for a
 * variety of use cases, from simple, synchronous operations to complex, concurrent tasks.
 *
 * ### Example: Implementing a Custom Operator
 * ```kotlin
 * class MyOperator : Operator<Int, MyFeature, MyRepresentation> {
 *     override suspend fun <S> invoke(
 *         state: S,
 *         outputSize: Int,
 *         buildState: (List<Individual<Int, MyFeature, MyRepresentation>>) -> S,
 *         random: Random
 *     ): Result<S> where S : EvolutionState<Int, MyFeature, MyRepresentation> {
 *         // Custom logic to transform the state, can be synchronous or asynchronous
 *         // Ensure the output size is valid and matches the resulting population size
 *     }
 * }
 * ```
 *
 * ## Implementation Requirements:
 * When implementing this interface, it's crucial to ensure the following:
 * - **Validate Output Size**: Implementers must validate that the `outputSize` parameter is appropriate for the
 *   operation. The output size must not exceed the population size or be negative.
 * - **Match Output Size**: The resulting population size after the operation should exactly match the specified
 *   `outputSize`. This is essential to maintain consistency within the evolutionary algorithm.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 */
interface Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Applies the operator to the given evolutionary state to produce a new state, supporting both synchronous and
     * asynchronous execution.
     *
     * This `suspend` function is the core operation of the `Operator` interface, transforming the current evolutionary
     * state by selecting, mutating, or recombining individuals in the population. The transformation is based on the
     * provided parameters and a random number generator. The function is marked as `suspend` to support asynchronous
     * operations, but it can also be used in a synchronous context when asynchronous behavior is not required.
     *
     * ## Implementation Note:
     * Implementers are responsible for validating that the `outputSize` is appropriate and ensuring that the new
     * population's size matches the `outputSize`. This is crucial for maintaining the integrity of the evolutionary
     * process and preventing runtime errors.
     *
     * @param S The type of the evolutionary state, which must extend [EvolutionState].
     * @param state The current evolutionary state.
     * @param outputSize The number of individuals to include in the resulting state.
     * @param buildState A function that constructs the new evolutionary state from a list of individuals.
     * @return A [Result] containing the new evolutionary state, or an exception if the operation fails.
     */
    suspend operator fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, F, R>>) -> S
    ): Either<OperatorInvocationException, S> where S : EvolutionState<T, F, R>
}
