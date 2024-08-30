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
 * A generic class representing a limit condition in an evolutionary algorithm.
 *
 * The `Limit` class defines a condition under which an evolutionary process should stop. This condition is specified
 * by a predicate function that evaluates the current state of the evolution and returns a boolean value indicating
 * whether the limit has been reached. The `Limit` class also incorporates a listener, which is used to observe the
 * evolutionary process and apply the limit condition.
 *
 * ## Usage:
 * This class is intended to be used in scenarios where you need to define custom stopping criteria for an evolutionary
 * algorithm. By providing a predicate function, you can control when the evolutionary process should terminate based
 * on specific conditions evaluated against the current state.
 *
 * ### Important Recommendation:
 * It is recommended to use the curried [limit] function to create `Limit` instances, as it provides a more flexible and
 * concise way to configure the limit condition. The curried function allows for partial application, enabling you to
 * pre-configure certain aspects of the limit and reuse the configuration across different evolutionary processes.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param L The type of the listener, which must extend [Listener].
 * @property listener The listener that observes the evolutionary process and applies the limit condition.
 * @property predicate The predicate function that evaluates the evolutionary state and determines whether the limit has
 *   been reached.
 */
open class Limit<T, F, R, S, out L>(
    val listener: L,
    private val predicate: L.(S) -> Boolean
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S>, L : Listener {

    /**
     * Evaluates the limit condition on the given state.
     *
     * This operator function applies the predicate function to the current state of the evolutionary process,
     * determining whether the limit has been reached.
     *
     * @param state The current state of the evolutionary process.
     * @return `true` if the limit condition is met, `false` otherwise.
     */
    operator fun invoke(state: S): Boolean = listener.predicate(state)
}

/**
 * Factory function for creating a [Limit] instance with a custom predicate.
 *
 * The `limit` function simplifies the creation of `Limit` instances by accepting a builder function for the listener
 * and a predicate function that defines the limit condition. This function is useful for creating custom stopping
 * criteria in evolutionary algorithms, with the flexibility to specify the listener and the predicate at runtime.
 *
 * ### Example: Creating a Limit with a Custom Predicate
 * ```kotlin
 * val myLimit = limit<MyType, MyFeature, MyRepresentation, MyState, MyListener>(
 *     builder = { config -> MyListener(config) },
 *     predicate = { state -> state.population.any { it.fitness >= 0.95 } }
 * )
 * ```
 *
 * In this example, `myLimit` will stop the evolutionary process once any individual's fitness in the population
 * reaches or exceeds 0.95.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param L The type of the listener, which must extend [Listener].
 * @param builder A function that constructs the listener based on the provided configuration.
 * @param predicate A predicate function that evaluates the state and determines whether the limit has been reached.
 * @return A function that, when provided with a listener configuration, returns a `Limit` instance.
 */
fun <T, F, R, S, L> limit(
    builder: (ListenerConfiguration<T, F, R>) -> L,
    predicate: L.(S) -> Boolean
): (ListenerConfiguration<T, F, R>) -> Limit<T, F, R, S, L>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S>, L : Listener = { config ->
    Limit(builder(config), predicate)
}
