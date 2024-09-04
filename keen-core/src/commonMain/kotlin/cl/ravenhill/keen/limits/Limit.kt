/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a configurable limit that applies a predicate function to an [EvolutionState] to evaluate if certain
 * conditions are met during the evolutionary process.
 *
 * ## Usage:
 * This class allows creating limits that determine whether specific evolutionary states fulfill a set of conditions.
 * It operates by using a listener to evaluate the evolution state and a predicate function that applies the evaluation
 * logic.
 *
 * ### Example 1: Creating a Limit
 * ```kotlin
 * val limit = Limit({ config -> CustomListener(config) }) { state ->
 *     state.generation < 100
 * }
 * val isWithinLimit = limit(evolutionState)
 * ```
 *
 * ### Example 2: Extending the Limit class
 * ```kotlin
 * class CustomLimit<T, F, R, S, L>(
 *     listener: L,
 *     predicate: L.(S) -> Boolean
 * ) : Limit<T, F, R, S, L>(listener, predicate)
 *     where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S>, L : Listener {
 *
 *     fun customCheck(state: S): Boolean {
 *         return invoke(state) && state.someCustomCondition()
 *     }
 *
 *     // ... other custom methods ...
 * }
 *
 * val customLimit = CustomLimit({ config -> CustomListener(config) }) { state ->
 *     state.generation < 50
 * }
 * val result = customLimit.customCheck(evolutionState)
 * ```
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature stored in a representation, which must implement [Feature].
 * @param R The type of representation used by the individual, which must implement [Representation].
 * @param S The type representing the current state of evolution, which must implement [EvolutionState].
 * @param L The type of listener responsible for evaluating the evolution process, which must implement [Listener].
 *
 * @property listener The [Listener] responsible for evaluating the evolution state.
 * @property predicate A predicate function that takes the current [EvolutionState] and returns a boolean indicating
 *   whether the limit conditions are satisfied.
 */
open class Limit<T, F, R, S, out L> protected constructor(
    val listener: L,
    private val predicate: L.(S) -> Boolean
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S>, L : Listener {

    /**
     * Applies the predicate function to the given [EvolutionState].
     *
     * @param state The current [EvolutionState] to evaluate.
     * @return `true` if the limit conditions are met, otherwise `false`.
     */
    operator fun invoke(state: S): Boolean = listener.predicate(state)

    companion object {
        /**
         * Creates a new [Limit] instance using the provided listener configuration and predicate function.
         *
         * @param builder A function that constructs a [Listener] from the given [ListenerConfiguration].
         * @param predicate A predicate function that the listener will use to evaluate the [EvolutionState].
         * @return A new [Limit] instance.
         */
        operator fun <T, F, R, S, L> invoke(
            builder: (ListenerConfiguration<T, F, R>) -> L,
            predicate: L.(S) -> Boolean
        )
                where F : Feature<T, F>,
                      R : Representation<T, F>,
                      S : EvolutionState<T, F, R, S>,
                      L : Listener = { config: ListenerConfiguration<T, F, R> ->
            Limit(builder(config), predicate)
        }
    }
}
