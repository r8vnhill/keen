package keen.listeners.precision

import io.kotest.property.Arb
import io.kotest.property.arbitrary.element

/**
 * Provides an [Arb] (Arbitrary) generator for [TimePrecision] instances, randomly selecting one from the available
 * time precisions.
 *
 * @return An [Arb] that generates a random [TimePrecision], choosing from [WholeDays], [WholeHours],
 *   [WholeMicroseconds], [WholeMilliseconds], [WholeMinutes], [WholeNanoseconds], and [WholeSeconds].
 */
fun arbTimePrecision() = Arb.element(
    WholeDays,
    WholeHours,
    WholeMicroseconds,
    WholeMilliseconds,
    WholeMinutes,
    WholeNanoseconds,
    WholeSeconds
)
