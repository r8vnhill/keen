/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector


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
 * val ranker = FitnessMinRanker<MyGeneType, MyFeatureType>()
 * val sortedPopulation = ranker.sort(population)
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 */
class FitnessMinRanker<T, G> : Ranker<T, G> where G : Gene<T, G> {

    /**
     * Compares two individuals based on their fitness values.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the fitness of the second individual is less than,
     *  equal to, or greater than the fitness of the first individual, respectively.
     */
    override fun invoke(first: FitnessEvaluable, second: FitnessEvaluable) = second.fitness.compareTo(first.fitness)

    /**
     * Transforms a list of fitness values by inverting them.
     *
     * This method maps each fitness value to the difference between the sum of all fitness values and the individual
     * fitness value. This transformation is useful in scenarios where minimizing fitness values is desired.
     *
     * @param fitness The list of fitness values to transform.
     * @return The transformed list of fitness values.
     * @see RouletteWheelSelector.probabilities for an example of using this transformation.
     */
    override fun fitnessTransform(fitness: List<Double>): List<Double> {
        val sum = fitness.sum()
        return fitness.map { sum - it }
    }
}
