/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.util.listeners.records.GenerationRecord.EvaluationRecord
import cl.ravenhill.keen.util.listeners.records.GenerationRecord.SelectionRecord
import kotlinx.serialization.Serializable

/**
 * A comprehensive representation of a generation in the execution of a genetic algorithm. This class
 * holds important information about each generation including its generation number, the duration it
 * took to complete, and detailed records of different stages in the algorithm's lifecycle.
 *
 * This record is particularly useful for analysis and reporting purposes, allowing for in-depth insights
 * into the performance and progression of the genetic algorithm over successive generations.
 *
 * @property generation The generation number, which must be a positive integer. It is the unique identifier of the generation.
 * @property duration The total time duration of the entire generation. This must be initialized before it can be accessed.
 * @property startTime The time at which the initiation of this generation occurred.
 * @property evaluation The [EvaluationRecord] representing the evaluation phase of the genetic algorithm for this generation.
 * @property survivorSelection The [SelectionRecord] representing the survivor selection phase of the genetic algorithm for this generation.
 * @property offspringSelection The [SelectionRecord] representing the offspring selection phase of the genetic algorithm for this generation.
 *
 * @constructor Creates a new instance of [GenerationRecord] while ensuring the generation number is a positive integer.
 *
 * @since 2.0.0
 * @version 2.0.0
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
@Serializable
data class GenerationRecord(val generation: Int) : AbstractTimedRecord() {
    val alteration = AlterationRecord()
    val evaluation = EvaluationRecord()
    val offspringSelection = SelectionRecord()
    val survivorSelection = SelectionRecord()
    val population = PopulationRecord()
    var steady: Int = 0
        set(value) {
            constraints {
                "The generation number [$value] must be positive" { value mustNot BeNegative }
            }
            field = value
        }

    init {
        constraints {
            "The generation number [$generation] must be positive" { generation mustNot BeNegative }
        }
    }

    /**
     * This class represents the evaluation phase of a generation in a genetic algorithm.
     * It inherits start time and duration properties from [AbstractRecord] to capture the timing details of this phase.
     */
    @Serializable
    class EvaluationRecord : AbstractTimedRecord()

    /**
     * This class represents a selection phase (either survivor or offspring selection) of a
     * generation in a genetic algorithm. It is used to capture the start time and duration of the selection phase.
     */
    @Serializable
    class SelectionRecord : AbstractTimedRecord()

    /**
     * This class represents the alteration phase of a generation in a genetic algorithm.
     * It inherits start time and duration properties from [AbstractRecord] to capture the timing details of this phase.
     */
    @Serializable
    class AlterationRecord : AbstractTimedRecord()

    /**
     * Represents a record of a population consisting of multiple individual records.
     *
     * The [PopulationRecord] data class encapsulates information about a population in terms of individual entities.
     * Each population record contains a list of [IndividualRecord] instances, representing members of that population.
     *
     * This class can be serialized and deserialized, making it suitable for data storage, transmission, and
     * processing tasks that involve object-to-text and text-to-object conversions.
     *
     * ### Example:
     * ```kotlin
     * val individual1 = IndividualRecord("Genotype1", 1.0)
     * val individual2 = IndividualRecord("Genotype2", 2.0)
     * val populationRecord = PopulationRecord(listOf(individual1, individual2))
     * ```
     *
     * @property resulting A list of [IndividualRecord] instances representing individuals in the population.
     *                     By default, it's an empty list.
     *
     * @see IndividualRecord
     * @see AbstractRecord
     */
    @Serializable
    data class PopulationRecord(var resulting: List<IndividualRecord> = listOf()) : AbstractRecord()
}
