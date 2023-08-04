/*
 * Copyright (c) 2023, Ravenhill.CL.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BeNegative
import cl.ravenhill.keen.util.listeners.records.GenerationRecord.EvaluationRecord
import cl.ravenhill.keen.util.listeners.records.GenerationRecord.SelectionRecord
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

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
    var steady:Int = 0

    init {
        enforce {
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
     * This class is used to hold the result of the population of a specific generation.
     * It contains a list of [PhenotypeRecord] that represents the phenotypes of the population after the generation process.
     */
    @Serializable
    class PopulationRecord : AbstractRecord() {
        lateinit var resulting: List<PhenotypeRecord>
    }
}
