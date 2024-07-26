/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable


/**
 * Represents a fitness ranker that ranks individuals by maximizing their fitness values.
 *
 * The `FitnessMaxRanker` class implements the `FitnessRanker` interface and provides a comparator that ranks
 * individuals based on their fitness values in ascending order. This means individuals with higher fitness values are
 * considered better.
 *
 * ## Usage:
 * This class is used to rank individuals in an evolutionary algorithm where higher fitness values are preferred. It
 * implements the comparison, equality, and hash code methods.
 *
 * ### Example:
 * ```kotlin
 * val ranker = FitnessMaxRanker<MyGeneType, MyFeatureType>()
 * val sortedPopulation = ranker.sort(population)
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param F The type of the feature, which must extend [Feature].
 * @property comparator A comparator for comparing individuals based on their fitness.
 */
class FitnessMaxRanker<T, F> : Ranker<T, F> where F : Feature<T, F> {

    /**
     * Compares two individuals based on their fitness values.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the fitness of the first individual is less than,
     * equal to, or greater than the fitness of the second individual, respectively.
     */
    override fun invoke(first: FitnessEvaluable, second: FitnessEvaluable) = first.fitness.compareTo(second.fitness)

    /**
     * Returns a string representation of the `FitnessMaxRanker`.
     *
     * @return The string "FitnessMaxRanker".
     */
    override fun toString() = "FitnessMaxRanker"

    /**
     * Checks if this `FitnessMaxRanker` is equal to another object.
     *
     * @param other The other object to compare.
     * @return `true` if the other object is a `FitnessMaxRanker` and is equal to this one, `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FitnessMaxRanker<*, *>) return false
        return true
    }

    /**
     * Returns the hash code of this `FitnessMaxRanker`.
     *
     * @return The hash code of this `FitnessMaxRanker`.
     */
    override fun hashCode() = FitnessMaxRanker::class.hashCode()
}
