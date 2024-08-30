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
 * Interface for listening to parent selection events in an evolutionary algorithm.
 *
 * The `ParentSelectionListener` interface defines a set of callbacks that can be implemented to respond to events
 * during the parent selection phase of an evolutionary algorithm. These events include the start and end of the parent
 * selection process. Implementing this interface allows for monitoring and reacting to the selection of parents, which
 * is a critical step in the evolutionary cycle as it influences the genetic composition of the next generation.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface ParentSelectionListener<T, F, R, S> :
    Listener where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * Called at the start of the parent selection process.
     *
     * The `onParentSelectionStart` method is invoked when the parent selection phase begins. This provides an
     * opportunity to execute any setup or logging before parents are selected from the population.
     *
     * @param state The current evolutionary state at the start of the parent selection process.
     */
    fun onParentSelectionStart(state: S)

    /**
     * Called at the end of the parent selection process.
     *
     * The `onParentSelectionEnd` method is invoked when the parent selection phase completes. This provides an
     * opportunity to execute any cleanup, logging, or further processing after parents have been selected.
     *
     * @param state The current evolutionary state at the end of the parent selection process.
     */
    fun onParentSelectionEnd(state: S)
}
