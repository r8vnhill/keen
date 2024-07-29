/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface for listening to generation events in an evolutionary algorithm.
 *
 * The `GenerationListener` interface provides methods that are called at the start and end of each generation
 * during the evolutionary process. Implementations of this interface can be used to perform actions or log information
 * at specific points in the generation cycle.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface GenerationListener<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * Called at the start of each generation.
     *
     * This method is invoked at the beginning of a generation. Implementations can override this method to
     * perform actions or logging at the start of a generation.
     *
     * @param state The current evolutionary state.
     */
    fun onGenerationStart(state: S) = Unit

    /**
     * Called at the end of each generation.
     *
     * This method is invoked at the end of a generation. Implementations can override this method to
     * perform actions or logging at the end of a generation.
     *
     * @param state The current evolutionary state.
     */
    fun onGenerationEnd(state: S) = Unit
}
