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
 * Interface for listening to events during the survivor selection process in an evolutionary algorithm.
 *
 * The `SurvivorSelectorListener` interface defines a contract for listeners that want to observe and respond to
 * specific events during the survivor selection phase of an evolutionary algorithm. This phase is crucial as it
 * determines which individuals from the current generation will survive and be carried over to the next generation.
 *
 * @param T The type of value held by the features within the individuals.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 */
interface SurvivorSelectorListener<T, F, R, S> : Listener where F : Feature<T, F>,
                                                                R : Representation<T, F>,
                                                                S : EvolutionState<T, F, R, S> {

    /**
     * Called at the start of the survivor selection process.
     *
     * This method is invoked before the survivor selection process begins. It allows for actions such as logging,
     * monitoring, or pre-selection adjustments based on the state of the population at this stage.
     *
     * @param state The current evolutionary state, including the population of individuals.
     */
    fun onSurvivorSelectionStart(state: S)

    /**
     * Called at the end of the survivor selection process.
     *
     * This method is invoked after the survivor selection process has completed. It allows for actions such as
     * logging, monitoring, or post-selection adjustments based on the state of the population after selection.
     *
     * @param state The updated evolutionary state after survivor selection has been applied.
     */
    fun onSurvivorSelectionEnd(state: S)
}
