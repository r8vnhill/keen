package cl.ravenhill.keen.listeners.precision

import kotlin.time.Duration

/**
 * A [TimePrecision] implementation that provides precision in whole seconds.
 *
 * @property unit The unit of time precision, represented as `"s"`.
 * @property withPrecision A function that converts a [Duration] to its equivalent in whole seconds.
 */
data object WholeSeconds : TimePrecision {
    override val unit = "s"
    override val withPrecision = Duration::inWholeSeconds
}
