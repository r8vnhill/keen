/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface for initializing the state of an evolutionary algorithm.
 *
 * The `InitializerEngine` interface defines the contract for classes responsible for setting up the initial state in an
 * evolutionary algorithm. This includes tasks such as generating an initial population of individuals, assigning
 * initial values or configurations, and ensuring that the state is ready for the evolutionary process to begin.
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
     * Initializes the given evolutionary state.
     *
     * This method is responsible for preparing the initial state for the evolutionary process. It might include
     * generating the initial population, setting initial values, or performing any setup required before the
     * evolutionary process begins. The method is `suspend` to support asynchronous initialization, which is especially
     * useful in scenarios involving large populations or complex initialization logic.
     *
     * @param state The initial evolutionary state to be initialized.
     * @return The initialized state, ready for the evolutionary process to start.
     */
    suspend fun initialize(state: S): S
}
