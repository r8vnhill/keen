package cl.ravenhill.keen.listeners.precision

import kotlin.time.Duration

/**
 * A [TimePrecision] implementation that provides precision in whole hours.
 *
 * @property unit The unit of time precision, represented as `"h"`.
 * @property withPrecision A function that converts a [Duration] to its equivalent in whole hours.
 */
data object WholeHours : TimePrecision {
    override val unit = "h"
    override val withPrecision = Duration::inWholeHours
}
