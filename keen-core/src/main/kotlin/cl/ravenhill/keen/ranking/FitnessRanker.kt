/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable

/**
 * The `IndividualRanker` typealias is deprecated and should be replaced with [FitnessRanker]. It was used to rank
 * individuals in an evolutionary algorithm based on their fitness.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 */
@Deprecated("Use FitnessRanker instead", ReplaceWith("FitnessRanker"))
typealias IndividualRanker<T, F> = FitnessRanker<T, F>

/**
 * Represents a fitness ranker in an evolutionary algorithm.
 *
 * The `FitnessRanker` interface defines the basic structure and operations for ranking individuals based on their
 * fitness. This includes a comparator for sorting individuals and methods for transforming and sorting fitness values.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that rank individuals in an evolutionary algorithm based on
 * their fitness. Implementing classes should provide the logic for comparing individuals and transforming fitness
 * values.
 *
 * ### Example:
 * ```kotlin
 * class MyFitnessRanker<T, F> : FitnessRanker<T, F> where F : Feature<T, F> {
 *
 *     override fun invoke(first: FitnessEvaluable, second: FitnessEvaluable) =
 *         first.fitness.compareTo(second.fitness)
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property comparator A comparator for comparing individuals based on their fitness.
 */
interface FitnessRanker<T, F> where F : Feature<T, F> {

    val comparator
        get() = Comparator(::invoke)

    /**
     * Compares two individuals based on their fitness.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the fitness of the first individual is less than,
     *  equal to, or greater than the fitness of the second individual, respectively.
     */
    operator fun invoke(first: FitnessEvaluable, second: FitnessEvaluable): Int

    /**
     * Sorts a population of individuals based on their fitness in descending order.
     *
     * @param population The population of individuals to sort.
     * @return The sorted list of individuals.
     */
    fun sort(population: List<FitnessEvaluable>) = population.sortedWith(comparator.reversed())

    /**
     * Transforms a list of fitness values.
     *
     * This method can be used to apply transformations to the fitness values, such as scaling or normalizing.
     *
     * @param fitness The list of fitness values to transform.
     * @return The transformed list of fitness values.
     */
    fun fitnessTransform(fitness: List<Double>) = fitness
}
