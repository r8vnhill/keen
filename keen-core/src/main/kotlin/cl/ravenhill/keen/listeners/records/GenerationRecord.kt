/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Data class representing a generation record in the Keen evolutionary computation framework.
 *
 * The `GenerationRecord` class encapsulates information about a specific generation in the evolutionary process,
 * including the generation number, steady counter, and detailed records for various stages of the process. It extends
 * the `AbstractTimedRecord` class, providing timing information for the generation.
 *
 * ## Usage:
 * This class is used to track and manage information about individual generations during the evolutionary process.
 * It includes records for alterations, evaluations, selections, and the population.
 *
 * ### Example 1: Creating a GenerationRecord
 * ```kotlin
 * val generationRecord = GenerationRecord<MyType, MyFeature, MyRepresentation>(generations = 1)
 * generationRecord.steady = 0
 * ```
 *
 * ### Example 2: Accessing Population Records
 * ```kotlin
 * val populationRecord = GenerationRecord.PopulationRecord(
 *     parents = listOf(parentIndividual),
 *     offspring = listOf(offspringIndividual)
 * )
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property generation The generation number, which must not be negative.
 * @constructor Creates an instance of `GenerationRecord` with the specified generation number.
 * @throws CompositeException if any of the constraints are violated.
 * @throws IntConstraintException if the generation number is negative.
 */
data class GenerationRecord<T, F, R>(val generation: Int) :
    AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F> {
    init {
        constraints {
            "The generation number ($generation) must not be negative" { generation mustNot BeNegative }
        }
    }

    /**
     * The steady counter, which must not be negative.
     */
    var steady = 0
        set(value) {
            constraints {
                "The steady counter ($value) must not be negative" { value mustNot BeNegative }
            }
            field = value
        }

    /**
     * Record for tracking alterations during the generation.
     */
    class AlterationRecord : AbstractTimedRecord()

    /**
     * Record for tracking evaluations during the generation.
     */
    class EvaluationRecord : AbstractTimedRecord()

    /**
     * Record for tracking selections during the generation.
     */
    class SelectionRecord : AbstractTimedRecord()

    /**
     * Data class representing the population record for a generation.
     *
     * @param T The type of the value held by the features.
     * @param F The type of the feature, which must extend [Feature].
     * @param R The type of the representation, which must extend [Representation].
     * @property parents The list of parent individuals in the population.
     * @property offspring The list of offspring individuals in the population.
     * @constructor Creates an instance of `PopulationRecord` with the specified parents and offspring.
     */
    data class PopulationRecord<T, F, R>(
        val parents: List<IndividualRecord<T, F, R>>,
        val offspring: List<IndividualRecord<T, F, R>>
    ) : AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F>
}
