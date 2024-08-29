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
        makeCopy(population = population.map(f))

    /**
     * Folds the values in the population from left to right, accumulating a result.
     *
     * The `fold` function allows you to reduce the entire population to a single value by applying a binary operation
     * to an initial value and each element (i.e., each individual) in the population. The operation is applied
     * sequentially from the first individual to the last, which makes it suitable for operations where the order
     * of accumulation follows the sequence of individuals.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each value in the population.
     * @return The final accumulated result after processing all individuals from left to right.
     */
    override fun <R> fold(initial: R, operation: (R, T) -> R): R =
        population.fold(initial) { acc, individual -> individual.fold(acc, operation) }

    /**
     * Folds the values in the population from right to left, accumulating a result.
     *
     * The `foldRight` function allows you to reduce the entire population to a single value by applying a binary
     * operation to each element (i.e., each individual) and an initial value, processing elements from the last
     * individual to the first. This is useful for operations where the order of processing should start from the
     * end of the population and move towards the beginning, such as when building a result in reverse order.
     *
     * ## Efficiency Considerations:
     * - **Folding Left (`fold`)**: Efficient for operations where accumulation naturally follows the sequence of
     *   individuals from first to last, such as summing values or combining results in the original order.
     * - **Folding Right (`foldRight`)**: More efficient for operations where accumulation needs to start from the
     *   last individual and work towards the first, such as when constructing a result that depends on the order
     *   starting from the end.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to each value in the population and the accumulator.
     * @return The final accumulated result after processing all individuals from right to left.
     */
    override fun <R> foldRight(initial: R, operation: (T, R) -> R): R =
        population.foldRight(initial) { individual, acc -> individual.foldRight(acc, operation) }

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
     * Creates a new `EvolutionState` instance with the specified properties, while preserving the other properties
     * from the current instance.
     *
     * This function allows you to create a new state by modifying some of the properties (such as population, ranker,
     * or generation) while keeping the rest unchanged. It is particularly useful for scenarios where you want to update
     * part of the state without altering the rest, such as during evolutionary algorithm iterations.
     *
     * @param population The new population for the state. If not provided, the current population is used.
     * @param ranker The new ranker for evaluating individuals. If not provided, the current ranker is used.
     * @param generation The new generation number. If not provided, the current generation number is used.
     * @return A new `EvolutionState` instance with the updated properties.
     */
    fun makeCopy(
        population: Population<T, F, R> = this.population,
        ranker: IndividualRanker<T, F, R> = this.ranker,
        generation: Int = this.generation
    ): EvolutionState<T, F, R>
}
