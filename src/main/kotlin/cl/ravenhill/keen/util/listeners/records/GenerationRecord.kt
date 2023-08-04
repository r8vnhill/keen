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

/**
 * Represents a generation in a genetic algorithm run, including timing, generation number and
 * different stages of the genetic algorithm.
 *
 * This data class encapsulates all information pertaining to a single generation during the
 * execution of a genetic algorithm.
 * It stores the generation number and timing details, including the starting time of the evaluation
 * phase, the total duration of the entire generation, and the time at which the generation was
 * initiated.
 *
 * Each generation in the genetic algorithm typically consists of evaluation and selection stages.
 * The [EvaluationRecord] and [SelectionRecord] inner classes represent these stages and also extend
 * the [AbstractRecord] to include timing information.
 *
 * @property generation The generation number, which must be a positive integer.
 * @property duration The total duration of the generation, which must be initialized before being
 *                    accessed.
 * @property startTime The time at which the initiation of this generation occurred.
 * @property evaluation The record of the evaluation phase of the genetic algorithm for this
 *                      generation.
 * @property survivorSelection The record of the survivor selection phase of the genetic algorithm
 *                             for this generation.
 * @property offspringSelection The record of the offspring selection phase of the genetic algorithm
 *                              for this generation.
 *
 * @constructor Creates a new [GenerationRecord] instance and validates that the generation number
 *              is positive.
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

    init {
        enforce {
            "The generation number [$generation] must be positive" { generation mustNot BeNegative }
        }
    }

    /**
     * This class represents the evaluation phase of a generation in a genetic algorithm.
     * It extends [AbstractRecord], meaning it also includes start time and duration properties.
     */
    @Serializable
    class EvaluationRecord : AbstractTimedRecord()

    /**
     * This class represents a selection phase (either survivor or offspring selection) of a
     * generation in a genetic algorithm.
     * It extends [AbstractTimedRecord], meaning it also includes start time and duration properties.
     */
    @Serializable
    class SelectionRecord : AbstractTimedRecord()

    @Serializable
    class AlterationRecord : AbstractTimedRecord()

    @Serializable
    class PopulationRecord : AbstractRecord() {
        lateinit var resulting: List<PhenotypeRecord>
    }
}
