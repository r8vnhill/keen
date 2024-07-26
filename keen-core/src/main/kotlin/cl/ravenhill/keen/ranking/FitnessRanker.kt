/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.mixins.FitnessEvaluable

/**
 * Represents a ranker for evaluating and comparing the fitness of individuals in an evolutionary algorithm.
 *
 * The `FitnessRanker` interface provides methods for comparing individuals based on their fitness, sorting populations,
 * and transforming fitness values. Implementations of this interface define how to compare fitness values and rank
 * individuals accordingly.
 *
 * ## Usage:
 * Use this interface to define custom ranking strategies for evolutionary algorithms. The ranker can be used to sort
 * populations, compare individuals, and apply transformations to fitness values.
 *
 * ### Example:
 * Implementing a simple fitness ranker:
 * ```kotlin
 * class SimpleFitnessRanker : FitnessRanker {
 *     override fun invoke(first: FitnessEvaluable, second: FitnessEvaluable) =
 *         first.fitness.compareTo(second.fitness)
 * }
 * val ranker = SimpleFitnessRanker()
 * val sortedPopulation = ranker.sort(population)
 * val transformedFitness = ranker.fitnessTransform(fitnessValues)
 * ```
 *
 * @property comparator The comparator used for comparing two fitness-evaluable individuals.
 */
interface FitnessRanker {

    val comparator
        get() = Comparator(::invoke)

    /**
     * Compares two fitness-evaluable individuals.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the first individual's fitness is less than,
     *         equal to, or greater than the second individual's fitness, respectively.
     */
    operator fun invoke(first: FitnessEvaluable, second: FitnessEvaluable): Int

    /**
     * Sorts a population based on the fitness values of the individuals.
     *
     * @param population The population to sort.
     * @return A sorted list of individuals in descending order of their fitness values.
     */
    fun sort(population: List<FitnessEvaluable>) = population.sortedWith(comparator.reversed())

    /**
     * Transforms a list of fitness values.
     *
     * @param fitness The list of fitness values to transform.
     * @return The transformed list of fitness values.
     */
    fun fitnessTransform(fitness: List<Double>) = fitness
}
