/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import arrow.core.Either
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.InitializationException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface representing an initialization engine for evolutionary algorithms.
 *
 * The `InitializerEngine` interface defines the contract for components responsible for initializing the evolutionary
 * state in an evolutionary algorithm. This typically involves generating an initial population or setting up other
 * state-related data structures that the algorithm will use in subsequent generations.
 *
 * ## Responsibilities:
 * Implementations of this interface are expected to:
 *
 * - **Initialization**: Provide logic to initialize the evolutionary state, typically by generating a population of
 *   individuals or configuring other necessary components.
 * - **Error Handling**: Use the `Either` type to handle potential failures during initialization, encapsulating
 *   errors in an [InitializationException] if something goes wrong.
 * - **Asynchronous Execution**: Support asynchronous operations through the `suspend` modifier, allowing the
 *   initialization process to be non-blocking and easily integrated into coroutine-based workflows.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface InitializerEngine<T, F, R, S> where F : Feature<T, F>,
                                              R : Representation<T, F>,
                                              S : EvolutionState<T, F, R> {

    /**
     * Initializes the evolutionary state.
     *
     * This method is responsible for setting up the initial conditions of the evolutionary algorithm, such as
     * generating the initial population. The process is expected to be asynchronous, making use of coroutines for
     * non-blocking execution.
     *
     * The method returns an `Either` type to handle potential failures:
     * - On success, the initialized state is returned.
     * - On failure, an [InitializationException] is returned, providing details about the error.
     *
     * @param state The current state that needs initialization.
     * @return An [Either] containing the initialized state or an [InitializationException].
     */
    suspend fun initialize(state: S): Either<InitializationException, S>
}
