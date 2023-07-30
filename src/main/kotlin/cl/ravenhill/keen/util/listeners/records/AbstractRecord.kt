/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import kotlinx.serialization.Serializable
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark

/**
 * This abstract class represents a generic record in a serializable format.
 * It includes a start time and a duration, making it suitable for any time-stamped record
 * keeping or logging activities.
 *
 * @property startTime The time when the event/record starts. The actual value must be set later,
 *                     hence it's marked with `lateinit`.
 *                     It uses the `TimeMark` class from the `kotlin.time` package.
 *                     Note: Since `TimeMark` is marked as `@ExperimentalTime`, this property is
 *                     annotated with `@OptIn(ExperimentalTime::class)` to acknowledge the usage of
 *                     this experimental API.
 * @property duration The duration of the event/record. It uses the `Duration` class from the
 *                    `kotlin.time` package.
 *                    The actual value must be set later.
 *                    Therefore, it's a delegated property and the delegate `Delegates.notNull()`
 *                    enforces that this property must be set (not null) before it gets accessed, or
 *                    it will throw an exception.
 */
@Serializable
abstract class AbstractRecord {
    @OptIn(ExperimentalTime::class)
    lateinit var startTime: TimeMark
    var duration: Duration by Delegates.notNull()
}
