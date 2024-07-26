/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.repr.Representation


/**
 * Represents a record of an individual in the evolutionary process.
 *
 * The `IndividualRecord` data class encapsulates the representation and fitness of an individual, serving as a
 * lightweight record that can be easily converted back to an `Individual` object.
 *
 * ## Usage:
 * Use this class to store and manage individual records in evolutionary algorithms. It provides a convenient way to
 * encapsulate the state of an individual and convert it back to a fully-fledged `Individual` when needed.
 *
 * ### Example:
 * ```kotlin
 * val record = IndividualRecord(representation = myRepresentation, fitness = 42.0)
 * val individual = record.toIndividual()
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
     * Converts the individual record to an `Individual` object.
     *
     * @return An `Individual` object with the same representation and fitness as this record.
     */
    fun toIndividual() = Individual(representation, fitness)
}
