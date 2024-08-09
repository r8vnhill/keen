/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Foldable
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents the state of an evolutionary process.
 *
 * The `EvolutionState` interface defines the core structure and operations for managing the state in an evolutionary
 * algorithm. This state includes the population of individuals, a ranker for evaluating fitness, the current generation
 * number, and the size of the population. Implementations of this interface are responsible for maintaining and
 * updating this information as the evolutionary process progresses.
 *
 * ## Usage:
 * Implement this interface to represent the state of the population in an evolutionary algorithm. The state tracks the
 * individuals, their fitness evaluations, and other essential metadata required for driving the evolutionary process.
 *
 * ### Example:
 * Implementing a simple evolutionary state:
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
 *
 *     override fun withPopulation(population: Population<T, F, R>) = copy(population = population)
 * }
 * ```
 *
 * @param T The type of the value held by the features in the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property size The number of individuals in the population.
 * @property population The current population of individuals in this state.
 * @property ranker The ranker used to evaluate and compare individuals within the population.
 * @property generation The current generation number in the evolutionary process.
 */
interface EvolutionState<T, F, R> : FlatMappable<T>, Foldable<T> where F : Feature<T, F>, R : Representation<T, F> {

    val size: Int
        get() = population.size

    val population: Population<T, F, R>

    val ranker: IndividualRanker<T, F, R>

    val generation: Int

    /**
     * Checks if the state is empty, i.e., if the population contains no individuals.
     *
     * @return `true` if the population is empty, `false` otherwise.
     */
    fun isEmpty(): Boolean = population.isEmpty()

    /**
     * Applies a transformation function to each individual in the population and returns a new state with the
     * transformed population.
     *
     * @param f The transformation function to apply to each individual.
     * @return A new `EvolutionState` instance with the transformed population.
     */
    fun map(f: (Individual<T, F, R>) -> Individual<T, F, R>): EvolutionState<T, F, R> =
        withPopulation(population.map(f))

    /**
     * Folds the elements of the population into a single value.
     *
     * This method applies a binary operation to an initial value and each element of the population, accumulating a
     * result. It's useful for operations like summing fitness values or aggregating features.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each element of the population.
     * @return The final accumulated result.
     */
    override fun <R> fold(initial: R, operation: (R, T) -> R): R =
        population.fold(initial) { acc, individual -> individual.fold(acc, operation) }

    /**
     * Flattens the population by combining all the individuals' representations into a single list.
     *
     * This method traverses through each individual in the population and flattens their representations (e.g., genes
     * within chromosomes) into a single, unified list. The result is a list that contains all the flattened elements
     * from every individual in the population.
     *
     * @return A list containing all the flattened elements from the population.
     */
    override fun flatten(): List<T> = population.flatMap { it.flatten() }

    /**
     * Creates a new state with the specified population.
     *
     * This method is used to generate a new instance of `EvolutionState` with an updated population, preserving other
     * aspects of the current state.
     *
     * @param population The new population for the state.
     * @return A new `EvolutionState` instance with the updated population.
     */
    fun withPopulation(population: Population<T, F, R>): EvolutionState<T, F, R>
}
