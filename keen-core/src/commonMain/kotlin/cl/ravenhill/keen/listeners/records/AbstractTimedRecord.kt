/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeMark

/**
 * Abstract class representing a timed record in the Keen evolutionary computation framework.
 *
 * The `AbstractTimedRecord` class provides a structure for recording the start time and duration of events within
 * the evolutionary algorithm. It is designed to be extended by other classes that require timing information.
 *
 * ## Usage:
 * This class is intended to be used as a base class for other timed records in the framework. Subclasses can use
 * the `startTime` and `duration` properties to record and manage timing information for various events.
 *
 * ### Example 1: Extending AbstractTimedRecord
 * ```kotlin
 * class MyTimedRecord : AbstractTimedRecord() {
 *     fun start() {
 *         startTime = TimeSource.Monotonic.markNow()
 *     }
 *
 *     fun stop() {
 *         duration = startTime.elapsedNow().toLong(DurationUnit.NANOSECONDS)
 *     }
 * }
 * ```
 *
 * @property startTime The time mark indicating when the event started.
 * @property duration The duration of the event in nanoseconds.
 */
abstract class AbstractTimedRecord {

    lateinit var startTime: TimeMark

    var duration: Long = Duration.ZERO.toLong(DurationUnit.NANOSECONDS)
}
