/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a state in the evolutionary process.
 *
 * The `EvolutionState` interface defines the basic structure and operations for an evolutionary state, which includes
 * the population of individuals, the ranker used for evaluating fitness, the current generation number, and the size of
 * the state.
 *
 * ## Usage:
 * Use this interface to define the state of the population in an evolutionary algorithm. The state keeps track of the
 * individuals, their fitness evaluations, and other necessary metadata for the evolutionary process.
 *
 * ### Example:
 * Implementing a simple state:
 * ```kotlin
 * data class SimpleState<T, F, R>(
 *     override val population: Population<T, F, R>,
 *     override val ranker: IndividualRanker<T, F, R>,
 *     override val generation: Int
 * ) : EvolutionState<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *     override val size: Int
 *         get() = population.size
 *
 *     override fun isEmpty() = population.isEmpty()
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property size The size of the state, which is the number of individuals in the population.
 * @property population The population of individuals in the current state.
 * @property ranker The ranker used to evaluate and compare individuals in the population.
 * @property generation The current generation number in the evolutionary process.
 */
interface EvolutionState<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    val size: Int
        get() = population.size

    val population: Population<T, F, R>

    val ranker: IndividualRanker<T, F, R>

    val generation: Int

    /**
     * Checks if the state is empty.
     *
     * @return `true` if the state has no individuals, `false` otherwise.
     */
    fun isEmpty(): Boolean = population.isEmpty()
}
