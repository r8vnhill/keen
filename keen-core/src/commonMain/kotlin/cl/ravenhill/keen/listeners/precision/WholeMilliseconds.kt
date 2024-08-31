/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.precision

import cl.ravenhill.keen.listeners.precision.WholeMilliseconds.unit
import cl.ravenhill.keen.listeners.precision.WholeMilliseconds.withPrecision
import kotlin.time.Duration

/**
 * A singleton object representing time precision in whole milliseconds.
 *
 * The `WholeMilliseconds` object implements the `TimePrecision` interface, providing a mechanism to apply millisecond
 * precision to time durations. This object is useful when you need to work with durations that should be rounded or
 * truncated to whole milliseconds.
 *
 * @property unit The unit of measurement for this precision level, which is `"ms"` (milliseconds).
 * @property withPrecision A function that applies millisecond precision to a [Duration], returning the duration in
 *   whole milliseconds as a `Long`.
 */
data object WholeMilliseconds : TimePrecision {
    override val unit = "ms"
    override val withPrecision = Duration::inWholeMilliseconds
}
