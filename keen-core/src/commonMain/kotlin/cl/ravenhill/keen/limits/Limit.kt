/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
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
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolution state, which must extend [EvolutionState].
 * @property listener The event listener used to evaluate the predicate.
 * @property predicate The predicate evaluated by the listener to determine if the limit condition is met.
 * @constructor Creates an instance of `ListenLimit` with the specified listener and predicate.
 */
open class Limit<T, F, R, S>(
    private val listener: EvolutionListener<T, F, R, S>,
    private val predicate: EvolutionListener<T, F, R, S>.(S) -> Boolean
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * Evaluates the limit condition based on the current state.
     *
     * This method determines whether the evolutionary process should stop based on the predicate evaluated by the
     * listener.
     *
     * @param state The current state of the evolutionary process.
     * @return `true` if the limit condition is met and the process should stop, `false` otherwise.
     */
    operator fun invoke(state: S) = listener.predicate(state)
}

/**
 * Creates a limit for the evolutionary process based on a listener and a predicate.
 *
 * The `limit` function generates a [Limit] instance using the provided listener builder and predicate. The limit is
 * defined by the listener's behavior and the condition specified by the predicate.
 *
 * ## Usage:
 * This function is useful for setting constraints or stopping conditions in the evolutionary algorithm. It allows
 * defining custom limits based on specific conditions evaluated by the listener.
 *
 * ### Example 1: Creating a Generation Limit
 * ```kotlin
 * val generationLimit = limit(
 *     builder = { config -> MyGenerationListener(config) },
 *     predicate = { state -> state.generation >= MAX_GENERATIONS }
 * )
 * val limitInstance = generationLimit(listenerConfig)
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param builder A function to create an [EvolutionListener] using the given configuration.
 * @param predicate A predicate function that defines the condition for the limit.
 * @return A function that creates a [Limit] using the given configuration.
 */
fun <T, F, R, S> limit(
    builder: (ListenerConfiguration<T, F, R>) -> EvolutionListener<T, F, R, S>,
    predicate: EvolutionListener<T, F, R, S>.(S) -> Boolean
): (ListenerConfiguration<T, F, R>) -> Limit<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> = { config ->
    Limit(builder(config), predicate)
}
