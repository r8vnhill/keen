/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Interface for ranking individuals in an evolutionary algorithm.
 *
 * The `IndividualRanker` interface defines the contract for ranking individuals based on their fitness. It includes
 * methods for comparing two individuals, sorting a population, and optionally transforming fitness values.
 *
 * ## Usage:
 * Implement this interface to define custom ranking logic for individuals in an evolutionary algorithm. The default
 * implementation of the `comparator` property uses the `invoke` operator function for comparing individuals.
 *
 * ### Example:
 * ```
 * class MyIndividualRanker<T, F : Feature<T, F>, R : Representation<T, F>> : IndividualRanker<T, F, R> {
 *     override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
 *         first.fitness.compareTo(second.fitness)
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * A comparator for ranking individuals.
     *
     * This property provides a default comparator that uses the `invoke` operator function to compare individuals.
     */
    val comparator
        get() = Comparator(::invoke)

    /**
     * Compares two individuals based on their fitness.
     *
     * This operator function defines the logic for comparing two individuals. It should return a negative integer,
     * zero, or a positive integer if the first individual is less than, equal to, or greater than the second
     * individual, respectively.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer as the first individual is less than, equal to, or
     *  greater than the second individual.
     */
    operator fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>): Int

    /**
     * Sorts a population of individuals based on their fitness.
     *
     * This method sorts the given population in descending order of fitness using the default comparator.
     *
     * @param population The population to sort.
     * @return A sorted list of individuals.
     */
    fun sort(population: Population<T, F, R>) = population.sortedWith(comparator.reversed())

    /**
     * Transforms a list of fitness values.
     *
     * This method can be overridden to apply custom transformations to the fitness values. The default implementation
     * returns the fitness values unchanged.
     *
     * @param fitness A list of fitness values.
     * @return The transformed list of fitness values.
     */
    fun fitnessTransform(fitness: List<Double>) = fitness
}
