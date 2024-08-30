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
 * A limit condition for evolutionary algorithms based on the maximum number of generations.
 *
 * The `MaxGenerations` class defines a stopping condition for an evolutionary algorithm that halts the process once
 * the number of generations reaches or exceeds a specified maximum. This limit is particularly useful in scenarios
 * where the evolutionary process needs to be bounded by a fixed number of iterations, regardless of the fitness levels
 * achieved.
 *
 * ### Recommendation:
 * It is recommended to use the `maxGenerations` curried function instead of directly instantiating the `MaxGenerations`
 * class. The curried function provides a more concise and flexible way to configure the limit, allowing for partial
 * application and easier reuse in different contexts.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param maxGenerations The maximum number of generations allowed before stopping the evolutionary process.
 * @param configuration The configuration object that contains listeners and other settings required by the listener.
 */
class MaxGenerations<T, F, R, S>(
    val maxGenerations: Int,
    configuration: ListenerConfiguration<T, F, R>
) : Limit<T, F, R, S, MaxGenerationsListener<T, F, R, S>>(
    MaxGenerationsListener(configuration),
    { state -> state.generation >= maxGenerations }
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S>

/**
 * Listener for the `MaxGenerations` limit condition.
 *
 * The `MaxGenerationsListener` class serves as a placeholder listener that is associated with the [MaxGenerations]
 * limit. It implements the [Listener] interface, providing the necessary functionality for monitoring the evolutionary
 * process based on the number of generations. The class primarily exists to ensure that the `Limit` class has a valid
 * listener type, and it can be customized further if additional behavior is needed.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param configuration The configuration object that contains listeners and other settings required by the listener.
 */
class MaxGenerationsListener<T, F, R, S>(
    private val configuration: ListenerConfiguration<T, F, R>
) : Listener where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * Creates a copy of the current `MaxGenerationsListener` instance with the same configuration.
     *
     * @return A new `MaxGenerationsListener` instance with the same configuration as the original.
     */
    override fun copy() = MaxGenerationsListener<_, _, _, S>(configuration)
}

/**
 * Factory function for creating a `MaxGenerations` limit condition.
 *
 * The `maxGenerations` function is a convenient way to create instances of the [MaxGenerations] class. It simplifies
 * the process of defining a generation-based limit in evolutionary algorithms, making the code more readable and
 * reducing the chance of errors.
 *
 * ### Example: Using the `maxGenerations` Factory Function
 * ```kotlin
 * val limitFactory = maxGenerations<MyType, MyFeature, MyRepresentation, MyState>(maxGenerations = 100)
 * val limit = limitFactory(myListenerConfiguration)
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param maxGenerations The maximum number of generations allowed before stopping the evolutionary process.
 * @return A function that returns a `MaxGenerations` instance when invoked with a listener configuration.
 */
fun <T, F, R, S> maxGenerations(
    maxGenerations: Int
): (ListenerConfiguration<T, F, R>) -> MaxGenerations<T, F, R, S> where F : Feature<T, F>,
                                                                        R : Representation<T, F>,
                                                                        S : EvolutionState<T, F, R, S> = { config ->
    MaxGenerations(maxGenerations, config)
}
