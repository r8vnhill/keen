/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A ranker implementation that ranks individuals based on their fitness, favoring higher fitness values.
 *
 * `FitnessMaxRanker` is a concrete implementation of [IndividualRanker] that uses the fitness values of individuals
 * to determine their ranking. It compares individuals based on their fitness, with higher fitness values being
 * considered better. This ranker is typically used in scenarios where the goal is to maximize fitness.
 *
 * ## Functionality:
 * - **Fitness-Based Ranking**: Individuals are compared and ranked based on their fitness values. The one with the
 *   higher fitness is considered superior.
 * - **Maximization**: This ranker is suited for optimization problems where the objective is to maximize a fitness
 *   function.
 *
 * ## Usage:
 * `FitnessMaxRanker` can be used in any evolutionary algorithm where individuals need to be sorted or selected based
 * on their fitness. It is particularly useful in contexts where higher fitness scores represent better solutions.
 *
 * ### Example:
 * Using `FitnessMaxRanker` in a genetic algorithm:
 * ```kotlin
 * val ranker: IndividualRanker<MyDataType, MyGene> = FitnessMaxRanker()
 * // Assuming a population of individuals
 * val sortedPopulation = ranker.sort(population)
 * ```
 * In this example, `FitnessMaxRanker` is used to sort a population of individuals in descending order of fitness.
 * The highest fitness individuals will be at the beginning of the sorted list.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 */
class FitnessMaxRanker<T, G> : IndividualRanker<T, G> where G : Gene<T, G> {

    /**
     * Compares two individuals based on their fitness values.
     *
     * This method implements a straightforward fitness comparison between two individuals. It is designed to be used
     * in contexts where a higher fitness value indicates a better or more optimal individual. This comparison is
     * fundamental in selection processes within evolutionary algorithms, particularly in scenarios aiming to maximize
     * fitness.
     *
     * ## Comparison Logic:
     * - If the first individual's fitness is higher than the second's, a positive integer is returned.
     * - If the second individual's fitness is higher than the first's, a negative integer is returned.
     * - If both individuals have equal fitness, zero is returned.
     *
     * ## Usage in Evolutionary Algorithms:
     * This method can be used as a key component in selection mechanisms, such as tournament selection or rank-based
     * selection, where individuals are chosen based on their fitness superiority.
     *
     * ```kotlin
     * val ranker: IndividualRanker<MyDataType, MyGene> = FitnessMaxRanker()
     * // Assuming a pair of individuals
     * val better = ranker(individual1, individual2) > 0
     * ```
     *
     * In this example, `FitnessMaxRanker` is used to compare two individuals. The result of the comparison is used to
     * determine if the first individual is better than the second.
     *
     * @param first The first individual to be compared. Its fitness is evaluated against the second individual.
     * @param second The second individual to be compared.
     * @return An integer value representing the comparison result: (1) A positive value indicates that the first
     *   individual has higher fitness, (2) a negative value indicates that the second individual has higher fitness,
     *   and (3) zero indicates that both individuals have equal fitness.
     */
    override fun invoke(first: Individual<T, G>, second: Individual<T, G>) = first.fitness.compareTo(second.fitness)

    override fun toString() = "FitnessMaxRanker"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FitnessMaxRanker<*, *>) return false
        return true
    }

    override fun hashCode() = FitnessMaxRanker::class.hashCode()
}
