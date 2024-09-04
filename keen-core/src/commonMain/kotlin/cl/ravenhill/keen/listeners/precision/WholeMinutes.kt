package cl.ravenhill.keen.listeners.precision

import kotlin.time.Duration

/**
 * A [TimePrecision] implementation that provides precision in whole minutes.
 *
 * @property unit The unit of time precision, represented as `"m"`.
 * @property withPrecision A function that converts a [Duration] to its equivalent in whole minutes.
 */
data object WholeMinutes : TimePrecision {
    override val unit = "m"
    override val withPrecision = Duration::inWholeMinutes
}
