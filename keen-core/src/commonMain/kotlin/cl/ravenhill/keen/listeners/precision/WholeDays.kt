package cl.ravenhill.keen.listeners.precision

import kotlin.time.Duration

data object WholeDays : TimePrecision {
    override val unit = "d"
    override val withPrecision = Duration::inWholeDays
}
