package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.ranking.FitnessRanker

/**
 * Represents a state in the evolutionary process.
 *
 * The `State` interface defines the basic structure and operations for an evolutionary state, which includes the
 * size of the state and a method to check if the state is empty. This interface is typically implemented by classes
 * that manage the state of a population in an evolutionary algorithm.
 *
 * ## Usage:
 * This interface is used to represent the current state of the population in an evolutionary algorithm. Implementing
 * classes should provide the logic to manage and query the state.
 *
 * ### Example:
 * ```kotlin
 * data class MyState<T, F>(
 *     override val population: List<FitnessEvaluable>,
 *     override val generation: Int,
 * ) : State<T, F> where F : Feature<T, F> {
 *
 *     override val size: Int
 *         get() = population.size
 *
 *     override fun isEmpty() = population.isEmpty()
 *
 *     override val ranker = FitnessMaxRanker<T, F>()
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property size The size of the state, typically representing the number of individuals in the population.
 * @property population The list of individuals in the state.
 * @property ranker The [FitnessRanker] used to rank individuals in the state.
 * @property generation The current generation of the state.
 */
interface State<T, F> where F : Feature<T, F> {

    val size: Int

    val population: List<FitnessEvaluable>

    val ranker: FitnessRanker<T, F>

    val generation: Int

    /**
     * Checks if the state is empty.
     *
     * @return `true` if the state is empty, `false` otherwise.
     */
    fun isEmpty(): Boolean
}
