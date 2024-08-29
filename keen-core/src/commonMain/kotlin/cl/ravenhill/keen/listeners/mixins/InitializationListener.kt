/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Listener interface for monitoring the initialization phase of an evolutionary algorithm.
 *
 * The `InitializationListener` interface defines two methods that are called at the start and end of the initialization
 * phase of an evolutionary algorithm. Implementations of this interface can be used to perform specific actions or
 * monitoring tasks during the initialization process, such as logging, setting up initial conditions, or validating the
 * state of the algorithm before it begins evolving.
 *
 * @param T The type of the value held by the features in the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface InitializationListener<T, F, R, S> :
    Listener where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * Called when the initialization process starts.
     *
     * This method is invoked at the beginning of the initialization phase of an evolutionary algorithm. It can be used
     * to perform actions such as logging, setting up necessary resources, or conducting pre-initialization checks.
     *
     * @param state The current evolutionary state at the start of the initialization process.
     */
    fun onInitializationStart(state: S)

    /**
     * Called when the initialization process ends.
     *
     * This method is invoked at the end of the initialization phase of an evolutionary algorithm. It can be used to
     * perform actions such as final validation, resource cleanup, or logging the results of the initialization.
     *
     * @param state The current evolutionary state at the end of the initialization process.
     */
    fun onInitializationEnd(state: S)
}
