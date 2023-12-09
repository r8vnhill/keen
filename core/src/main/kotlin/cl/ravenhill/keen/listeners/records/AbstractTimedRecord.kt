/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeMark

/**
 * An abstract base class for recording time-related data.
 *
 * This class provides a foundational structure for capturing and storing time measurements, particularly useful in
 * scenarios where tracking the duration of certain events or operations is required. It includes mechanisms for marking
 * the start time and calculating durations.
 *
 * ## Key Components:
 * - **Start Time**: The moment when the time recording begins.
 * - **Duration**: The total time elapsed since the start time, measured in nanoseconds.
 *
 * ## Usage:
 * Subclasses should initialize the `startTime` at the beginning of the event or operation being timed.
 * The `duration` is typically calculated at the end of the event or operation, representing the total time elapsed.
 *
 * ### Example:
 * Implementing a class to record the execution time of a function:
 * ```kotlin
 * class ExecutionTimer : AbstractTimedRecord() {
 *     fun start() {
 *         startTime = TimeMark.now()
 *     }
 *
 *     fun stop() {
 *         duration = TimeMark.now().elapsedNow().toLong(DurationUnit.NANOSECONDS)
 *     }
 * }
 *
 * val timer = ExecutionTimer()
 * timer.start()
 * // Execute some function
 * timer.stop()
 * println("Execution time: ${timer.duration} nanoseconds")
 * ```
 * In this example, `ExecutionTimer` extends `AbstractTimedRecord`. It marks the start time before a function
 * execution and calculates the duration after the function completes. The total execution time in nanoseconds
 * is then printed.
 *
 * @property startTime The time mark representing when the time measurement started. It must be initialized
 *   at the beginning of the timed event or operation.
 * @property duration The duration in nanoseconds from the `startTime` to the end of the measured event or
 *   operation. It represents the total time elapsed and is initialized to zero.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
abstract class AbstractTimedRecord {
    lateinit var startTime: TimeMark

    var duration: Long = Duration.ZERO.toLong(DurationUnit.NANOSECONDS)
}
