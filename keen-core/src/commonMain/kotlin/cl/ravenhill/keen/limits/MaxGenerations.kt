package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A limit condition based on the maximum number of generations in an evolutionary algorithm.
 *
 * The `MaxGenerations` class is a specific implementation of a limit condition that stops the evolutionary process once
 * a specified number of generations has been reached. It extends the [Limit] class and uses a [MaxGenerationsListener]
 * to observe and enforce the generation limit.
 *
 * ## Recommendation:
 * While this class can be used directly, it is recommended to use the [maxGenerations] function to create instances
 * of `MaxGenerations`. The factory function provides a more concise and expressive way to define this limit in
 * evolutionary configurations, improving code readability and maintainability.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @property maxGenerations The maximum number of generations allowed before stopping the evolutionary process.
 */
class MaxGenerations<T, F, R, S>(
    val maxGenerations: Int
) : Limit<T, F, R, S, MaxGenerationsListener<T, F, R, S>>(
    MaxGenerationsListener(),
    { state -> state.generation >= maxGenerations }
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>

/**
 * Listener that observes the evolutionary process to enforce the maximum generations limit.
 *
 * The `MaxGenerationsListener` class is a simple implementation of [EvolutionListener] that is used in conjunction
 * with the `MaxGenerations` limit. It ensures that the evolutionary process is monitored, and the limit is applied
 * when the maximum number of generations is reached.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
class MaxGenerationsListener<T, F, R, S> :
    EvolutionListener<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>

/**
 * Factory function to create a [MaxGenerations] limit condition.
 *
 * The `maxGenerations` function is a convenient way to create instances of the `MaxGenerations` class. It simplifies
 * the process of defining a generation-based limit in evolutionary algorithms, making the code more readable and
 * reducing the chance of errors.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param maxGenerations The maximum number of generations allowed before stopping the evolutionary process.
 * @return A function that returns a [MaxGenerations] instance when invoked.
 */
fun <T, F, R, S> maxGenerations(
    maxGenerations: Int
): (ListenerConfiguration<T, F, R>) -> MaxGenerations<T, F, R, S> where F : Feature<T, F>,
                                                                        R : Representation<T, F>,
                                                                        S : EvolutionState<T, F, R> =
    { MaxGenerations(maxGenerations) }
