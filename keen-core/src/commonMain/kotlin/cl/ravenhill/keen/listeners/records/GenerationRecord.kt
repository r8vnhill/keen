/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import arrow.core.getOrElse
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a record of a single generation in an evolutionary algorithm.
 *
 * The `GenerationRecord` class captures detailed information about a specific generation within an evolutionary
 * algorithm. This includes the generation number, the steady counter (indicating how many consecutive generations have
 * maintained the same fittest individual), and various timed records related to key evolutionary operations such as
 * alteration, evaluation, and selection.
 *
 * @param T The type of the value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @property generation The generation number, which must not be negative.
 * @property alteration Tracks the timing information related to the alteration process in the evolutionary algorithm.
 * @property evaluation Tracks the timing information related to the evaluation process.
 * @property parentSelection Tracks the timing information related to the parent selection process.
 * @property survivorSelection Tracks the timing information related to the survivor selection process.
 * @property population Holds the record of the population, including the parents and offspring for this generation.
 * @throws CompositeException if any of the constraints are violated.
 */
data class GenerationRecord<T, F, R>(val generation: Int) :
    AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F> {

    init {
        constrained {
            "The generation number ($generation) must not be negative" { generation mustNot BeNegative }
        }.onLeft { throw it }
    }

    /**
     * The counter for steady generations. This tracks how many consecutive generations have remained steady, meaning no
     * significant changes in fitness values.  It must not be negative.
     *
     * @throws CompositeException if the counter is negative.
     */
    var steady = 0
        set(value) {
            field = value.constrainedTo {
                "The steady counter ($value) must not be negative" { value mustNot BeNegative }
            }.getOrElse { throw it }
        }

    val alteration: AlterationRecord = AlterationRecord()

    val evaluation: EvaluationRecord = EvaluationRecord()

    val parentSelection: SelectionRecord = SelectionRecord()

    val survivorSelection: SelectionRecord = SelectionRecord()

    val population: PopulationRecord<T, F, R> = PopulationRecord()

    /**
     * Tracks timing information related to the alteration phase.
     */
    class AlterationRecord : AbstractTimedRecord()

    /**
     * Tracks timing information related to the evaluation phase.
     */
    class EvaluationRecord : AbstractTimedRecord()

    /**
     * Tracks timing information related to the selection phases (both parent and survivor selection).
     */
    class SelectionRecord : AbstractTimedRecord()

    /**
     * Holds the population details for a specific generation, including the parents and offspring.
     *
     * @param T The type of the value held by the features.
     * @param F The type of feature, which must extend [Feature].
     * @param R The type of representation, which must extend [Representation].
     * @param parents A list of parent individuals.
     * @param offspring A list of offspring individuals.
     */
    data class PopulationRecord<T, F, R>(
        val parents: List<IndividualRecord<T, F, R>> = emptyList(),
        var offspring: List<IndividualRecord<T, F, R>> = emptyList()
    ) : AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F>
}
