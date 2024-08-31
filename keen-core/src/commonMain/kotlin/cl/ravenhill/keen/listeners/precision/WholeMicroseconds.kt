/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.precision

import kotlin.time.Duration

/**
 * A time precision object that represents durations in whole microseconds.
 *
 * The `WholeMicroseconds` object implements the `TimePrecision` interface, providing a way to measure and represent
 * time durations with microsecond precision. This is useful in scenarios where fine-grained time measurements are
 * necessary, such as in high-performance computing or precise benchmarking tasks.
 *
 * @property unit The string representation of the time unit, which is `"μs"` for microseconds.
 * @property withPrecision A lambda function that converts a `Duration` to the number of whole microseconds it
 *   represents.
 */
data object WholeMicroseconds : TimePrecision {
    override val unit: String = "μs"
    override val withPrecision: Duration.() -> Long = { inWholeMicroseconds }
}
