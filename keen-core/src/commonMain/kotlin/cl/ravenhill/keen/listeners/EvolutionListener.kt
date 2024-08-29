/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface representing a listener for events in an evolutionary algorithm.
 *
 * The `EvolutionListener` interface extends the base [Listener] interface and is designed to observe key events during
 * the execution of an evolutionary algorithm. It provides hooks for responding to the start and end of the evolutionary
 * process, allowing developers to monitor or react to these significant points in the algorithm's lifecycle.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @see Listener The base interface that `EvolutionListener` extends.
 */
interface EvolutionListener<T, F, R, S> :
    Listener where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * Called at the start of the evolutionary process.
     *
     * This method is invoked before the evolutionary algorithm begins its process. It can be used to perform any
     * necessary setup or logging before the evolution starts.
     */
    fun onEvolutionStart()

    /**
     * Called at the end of the evolutionary process.
     *
     * This method is invoked after the evolutionary algorithm has completed its process. It receives the final state
     * of the evolution, which can be used for final logging, cleanup, or other post-evolution tasks.
     *
     * @param state The final state of the evolutionary process.
     */
    fun onEvolutionEnd(state: S)
}
