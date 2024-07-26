/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.repr.Representation


/**
 * Represents a record of a generation in the evolutionary process.
 *
 * The `GenerationRecord` data class encapsulates various detailed records for different stages of the evolutionary
 * process within a generation. This includes alteration, evaluation, parent selection, survivor selection, and
 * population records.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property generation The generation number.
 * @property alteration A timed record for the alteration stage.
 * @property evaluation A timed record for the evaluation stage.
 * @property parentSelection A timed record for the parent selection stage.
 * @property survivorSelection A timed record for the survivor selection stage.
 * @property population A record of the population in the generation.
 * @property steady The counter for steady generations.
 * @constructor Creates an instance of `GenerationRecord` with the specified generation number.
 * @throws CompositeException if any of the constraints are violated.
 * @throws IntConstraintException if the generation number is negative.
 */
data class GenerationRecord<T, F, R>(val generation: Int) :
    AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F> {

    init {
        constraints {
            "The generation number [$generation] must not be negative" { generation mustNot BeNegative }
        }
    }

    // Detailed records for various stages of the evolutionary process within the generation.
    val alteration = AlterationRecord()
    val evaluation = EvaluationRecord()
    val parentSelection = SelectionRecord()
    val survivorSelection = SelectionRecord()
    val population = PopulationRecord<T, F, R>()

    /**
     * The steady counter, representing the number of steady generations.
     *
     * This counter tracks how many consecutive generations have remained steady, meaning no significant changes in
     * fitness values. It must not be negative.
     */
    var steady: Int = 0
        set(value) {
            constraints {
                "The steady counter [$value] must not be negative" { value mustNot BeNegative }
            }
            field = value
        }

    /**
     * Represents a timed record for the alteration stage in the evolutionary process.
     */
    class AlterationRecord : AbstractTimedRecord()

    /**
     * Represents a timed record for the evaluation stage in the evolutionary process.
     */
    class EvaluationRecord : AbstractTimedRecord()

    /**
     * Represents a timed record for the selection stage in the evolutionary process.
     */
    class SelectionRecord : AbstractTimedRecord()

    /**
     * Represents a record of the population in the evolutionary process.
     *
     * The `PopulationRecord` class stores the parents and offspring of a population in a generation.
     *
     * @param T The type of the value held by the features.
     * @param F The type of the feature, which must extend [Feature].
     * @param R The type of the representation, which must extend [Representation].
     * @property parents The list of parent individual records.
     * @property offspring The list of offspring individual records.
     */
    data class PopulationRecord<T, F, R>(
        var parents: List<IndividualRecord<T, F, R>> = emptyList(),
        var offspring: List<IndividualRecord<T, F, R>> = emptyList(),
    ) where F : Feature<T, F>, R : Representation<T, F>
}
