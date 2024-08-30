/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface for listening to the evaluation process in an evolutionary algorithm.
 *
 * The `EvaluationListener` interface defines methods for monitoring the evaluation stage of an evolutionary algorithm.
 * It allows external observers or components to be notified when the evaluation process starts and ends. This is useful
 * for logging, analysis, or modifying behavior based on the results of evaluation cycles.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface EvaluationListener<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * Called when the evaluation process starts.
     *
     * This method is triggered at the beginning of the evaluation phase, providing an opportunity to perform
     * preparatory tasks or logging before the evaluation of the population takes place.
     *
     * @param state The current evolutionary state at the start of the evaluation.
     */
    fun onEvaluationStart(state: S)

    /**
     * Called when the evaluation process ends.
     *
     * This method is triggered at the end of the evaluation phase, allowing post-evaluation tasks such as logging,
     * analysis of the population, or other actions that need to occur after individuals have been evaluated.
     *
     * @param state The current evolutionary state after the evaluation is complete.
     */
    fun onEvaluationEnd(state: S)
}
