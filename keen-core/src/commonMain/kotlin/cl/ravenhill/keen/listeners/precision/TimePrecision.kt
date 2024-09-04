/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.precision

import kotlin.time.Duration

/**
 * Interface representing precision handling in time-related calculations.
 *
 * The `Precision` interface defines a contract for specifying how to apply a certain level of precision to a duration
 * and for providing a unit of measurement associated with that precision. This is particularly useful in contexts where
 * time durations need to be manipulated or displayed with specific precision, such as in evolutionary algorithms,
 * performance monitoring, or any system that deals with timing data.
 *
 * @property unit The unit of measurement associated with the precision (e.g., "ms", "ns").
 * @property withPrecision A function that applies the specified precision to a [Duration] and returns a `Long` value.
 */
sealed interface TimePrecision {

    val unit: String

    val withPrecision: Duration.() -> Long
}
