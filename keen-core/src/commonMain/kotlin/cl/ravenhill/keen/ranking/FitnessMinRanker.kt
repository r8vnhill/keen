/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation


/**
 * Represents a fitness ranker that ranks individuals by minimizing their fitness values.
 *
 * The `FitnessMinRanker` class implements the `FitnessRanker` interface and provides a comparator that ranks
 * individuals based on their fitness values in descending order. This means individuals with lower fitness values are
 * considered better.
 *
 * ## Usage:
 * This class is used to rank individuals in an evolutionary algorithm where lower fitness values are preferred. It
 * implements the comparison method and provides a transformation function for fitness values.
 *
 * ### Example:
 * ```kotlin
 * val ranker = FitnessMinRanker<MyGeneType, MyFeatureType, MyRepresentationType>()
 * val sortedPopulation = ranker.sort(population)
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
class FitnessMinRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Compares two individuals based on their fitness values.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the fitness of the second individual is less than,
     *  equal to, or greater than the fitness of the first individual, respectively.
     */
    override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
        second.fitness.compareTo(first.fitness)

    /**
     * Transforms a list of fitness values by inverting them.
     *
     * This method maps each fitness value to the difference between the sum of all fitness values and the individual
     * fitness value. This transformation is useful in scenarios where minimizing fitness values is desired.
     *
     * @param fitness The list of fitness values to transform.
     * @return The transformed list of fitness values.
     */
    override fun fitnessTransform(fitness: List<Double>): List<Double> {
        val sum = fitness.sum()
        return fitness.map { sum - it }
    }
}
