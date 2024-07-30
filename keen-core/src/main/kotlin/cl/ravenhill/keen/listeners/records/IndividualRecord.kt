/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Data class representing an individual record in the Keen evolutionary computation framework.
 *
 * The `IndividualRecord` class encapsulates the representation and fitness of an individual. It provides utility
 * functions to convert between `Individual` and `IndividualRecord` objects.
 *
 * ## Usage:
 * This class is used to store and manage the representation and fitness of individuals in a more lightweight form
 * compared to the `Individual` class. It also provides conversion functions to and from `Individual` objects.
 *
 * ### Example 1: Creating an IndividualRecord
 * ```kotlin
 * val representation = MyRepresentation(...)
 * val fitness = 1.0
 * val individualRecord = IndividualRecord(representation, fitness)
 * ```
 *
 * ### Example 2: Converting to Individual
 * ```kotlin
 * val individual = individualRecord.toIndividual()
 * ```
 *
 * ### Example 3: Creating IndividualRecord from Individual
 * ```kotlin
 * val individual = Individual(representation, fitness)
 * val individualRecord = IndividualRecord.fromIndividual(individual)
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property representation The representation of the individual.
 * @property fitness The fitness value of the individual.
 * @constructor Creates an instance of `IndividualRecord` with the specified representation and fitness.
 */
data class IndividualRecord<T, F, R>(
    val representation: R,
    val fitness: Double
) where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Converts this record to an `Individual` object.
     *
     * @return An `Individual` object with the same representation and fitness.
     */
    fun toIndividual() = Individual(representation, fitness)

    companion object {
        /**
         * Creates an `IndividualRecord` from an `Individual` object.
         *
         * @param individual The individual to convert to a record.
         * @return An `IndividualRecord` with the same representation and fitness.
         */
        fun <T, F, R> fromIndividual(individual: Individual<T, F, R>)
                where F : Feature<T, F>, R : Representation<T, F> =
            IndividualRecord(individual.representation, individual.fitness)
    }
}
