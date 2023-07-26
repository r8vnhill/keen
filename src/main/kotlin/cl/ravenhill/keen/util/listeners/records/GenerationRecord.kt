/*
 * Copyright (c) 2023, Ravenhill.CL.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BeNegative
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark

/**
 * Represents a generation in a genetic algorithm run, including timing and generation number.
 *
 * This data class holds details about a generation of a genetic algorithm.
 * It stores the generation number and timing details, such as when the evaluation for this
 * generation started, the total duration of the generation, and the time at which this generation
 * was initiated.
 *
 * @property generation The generation number.
 * @property evaluationStartTime The time at which the evaluation for this generation started.
 * @property duration The total duration of the generation.
 *                    It must be initialized before being used.
 * @property initTime The time at which this generation was initiated.
 *
 * @constructor Creates a new [GenerationRecord] object and validates that the generation number
 *              is positive.
 *
 * @since 2.0.0
 * @version 2.0.0
 */
@OptIn(ExperimentalTime::class)
@Serializable
data class GenerationRecord(val generation: Int) {
    lateinit var initTime: TimeMark
    var duration: Duration by Delegates.notNull()
    val evaluation = EvaluationRecord()

    init {
        enforce {
            "The generation number [$generation] must be positive" { generation mustNot BeNegative }
        }
    }

    @Serializable
    class EvaluationRecord {
        lateinit var startTime: TimeMark
        var duration: Duration by Delegates.notNull()
    }
}
