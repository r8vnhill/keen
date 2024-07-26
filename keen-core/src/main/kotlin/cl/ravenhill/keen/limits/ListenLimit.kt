/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.engines.Evolver
import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.mixins.FitnessEvaluable


/**
 * Represents a limit in the evolutionary algorithm that triggers based on an event listener and a predicate.
 *
 * The `ListenLimit` class implements the `Limit` interface and provides a mechanism to stop the evolutionary process
 * based on a condition evaluated by an event listener. This allows for custom stopping conditions that can be defined
 * using listeners.
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
 * val engine = geneticAlgorithm(...) {
 *   limits += limit
 *   // ...
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param I The type of the individuals in the state, which must extend [FitnessEvaluable].
 * @property listener The event listener used to evaluate the predicate.
 * @property predicate The predicate evaluated by the listener to determine if the limit condition is met.
 * @constructor Creates an instance of `ListenLimit` with the specified listener and predicate.
 */
open class ListenLimit<T, F, I>(
    private val listener: EvolutionListener<T, F>,
    private val predicate: EvolutionListener<T, F>.(State<T, F, I>) -> Boolean
) : Limit<T, F, I> where F : Feature<T, F>, I : FitnessEvaluable {

    /**
     * The engine associated with this limit.
     *
     * This property is deprecated and will be removed in future versions.
     */
    @Deprecated("This property will be removed in future versions.")
    override var engine: Evolver<T, F, I>? = null

    /**
     * Evaluates the limit condition based on the current state.
     *
     * This method determines whether the evolutionary process should stop based on the predicate evaluated by the
     * listener.
     *
     * @param state The current state of the evolutionary process.
     * @return `true` if the limit condition is met and the process should stop, `false` otherwise.
     */
    override fun invoke(state: State<T, F, I>) = listener.predicate(state)
}
