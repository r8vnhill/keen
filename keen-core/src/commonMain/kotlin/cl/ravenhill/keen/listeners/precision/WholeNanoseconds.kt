package cl.ravenhill.keen.listeners.precision

import kotlin.time.Duration

/**
 * A [TimePrecision] implementation that provides precision in whole nanoseconds.
 *
 * @property unit The unit of time precision, represented as `"ns"`.
 * @property withPrecision A function that converts a [Duration] to its equivalent in whole nanoseconds.
 */
data object WholeNanoseconds : TimePrecision {
    override val unit = "ns"
    override val withPrecision = Duration::inWholeNanoseconds
}
