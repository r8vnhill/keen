/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.util.listeners.records

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeMark

/**
 * An abstract representation of a record that can be serialized.
 * This base class provides foundational attributes and behaviors for any derived serializable
 * record.
 */
@Serializable
abstract class AbstractRecord

/**
 * Extends [AbstractRecord] to represent a time-stamped record.
 *
 * The `AbstractTimedRecord` class provides attributes to capture the start time and duration
 * of an event, making it apt for tasks such as event logging, monitoring, or other activities
 * that require tracking the span of time.
 *
 * @property startTime Represents the initiation timestamp of the event or activity.
 *                     By default, it's set to the current time mark.
 * @property duration Duration that the event or activity lasted.
 *                    Initialized to zero duration by default.
 */
@Serializable
abstract class AbstractTimedRecord : AbstractRecord() {
    @Transient
    lateinit var startTime: TimeMark

    var duration: Long = Duration.ZERO.toLong(DurationUnit.NANOSECONDS)
}
