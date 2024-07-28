/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a limit in the evolutionary algorithm that triggers based on an event listener and a predicate.
 *
 * The `Limit` class provides a mechanism to stop the evolutionary process based on a condition evaluated by an event
 * listener. This allows for custom stopping conditions that can be defined using listeners.
 *
 * ## Usage:
 * Use this class to define custom stopping conditions for the evolutionary algorithm based on events or states observed
 * by listeners. This is useful for scenarios where the stopping condition depends on specific events or complex state
 * evaluations.
 *
 * ### Example:
 * ```kotlin
 * val limit = ListenLimit(
 *     listener = EvolutionSummary(listenerConfig),
 *     predicate = { state -> state.generation >= 100 }
 * )
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param I The type of the individuals in the state, which must extend [FitnessEvaluable].
 * @property listener The event listener used to evaluate the predicate.
 * @property predicate The predicate evaluated by the listener to determine if the limit condition is met.
 * @constructor Creates an instance of `ListenLimit` with the specified listener and predicate.
 */
open class Limit<T, F, R, S>(
    private val listener: EvolutionListener<T, F, R, S>,
    private val predicate: EvolutionListener<T, F, R, S>.(S) -> Boolean
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {
    operator fun invoke(state: S) = listener.predicate(state)
}
