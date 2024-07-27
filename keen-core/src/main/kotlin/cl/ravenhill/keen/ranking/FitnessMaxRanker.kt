/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A ranker that prioritizes individuals with higher fitness values.
 *
 * The `FitnessMaxRanker` class implements the [IndividualRanker] interface, providing a comparator that ranks
 * individuals based on their fitness values in descending order. This ranker is commonly used in evolutionary
 * algorithms where higher fitness values indicate better individuals.
 *
 * ## Usage:
 * Use this ranker to sort populations or compare individuals based on their fitness values. It ensures that individuals
 * with higher fitness are ranked higher.
 *
 * ### Example:
 * ```kotlin
 * val ranker = FitnessMaxRanker<MyType, MyFeature, MyRepresentation>()
 * val sortedPopulation = ranker.sort(population)
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @constructor Creates an instance of `FitnessMaxRanker`.
 */
class FitnessMaxRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Compares two individuals based on their fitness values.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer as the first individual has less than, equal to,
     * or greater fitness than the second.
     */
    override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
        first.fitness.compareTo(second.fitness)
}
