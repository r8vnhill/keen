/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.doubles.BeNaN
import cl.ravenhill.jakt.constraints.doubles.BeNegative
import cl.ravenhill.keen.Domain.DEFAULT_CONSOLE_WIDTH
import cl.ravenhill.keen.Domain.DEFAULT_EQUALITY_THRESHOLD
import cl.ravenhill.keen.Domain.dispatcher
import cl.ravenhill.keen.Domain.equalityThreshold
import cl.ravenhill.keen.Domain.fallbackConsoleWidth
import cl.ravenhill.keen.Domain.random
import cl.ravenhill.keen.Domain.toStringMode
import cl.ravenhill.keen.evolution.executors.construction.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.construction.SequentialConstructor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random

/**
 * A singleton object that encapsulates global configuration and settings for the evolutionary algorithm domain.
 *
 * The `Domain` object provides a centralized place for managing global parameters and configurations that are commonly
 * used throughout the evolutionary algorithm framework. These include settings such as the equality threshold for
 * comparisons, the default coroutine dispatcher for concurrent operations, and the random number generator used for
 * stochastic processes.
 *
 * ## Example:
 * ```kotlin
 * // Set a custom equality threshold
 * Domain.equalityThreshold = 1E-6
 *
 * // Access the default random number generator
 * val randomValue = Domain.random.nextInt()
 *
 * // Set a custom coroutine dispatcher
 * Domain.dispatcher = Dispatchers.IO
 * ```
 *
 * @property DEFAULT_EQUALITY_THRESHOLD The default threshold for comparing floating-point numbers for equality. It is
 *   set to a very small value to account for precision errors in floating-point arithmetic.
 * @property dispatcher The default coroutine context used for concurrent operations. This can be overridden to
 *   customize the execution context.
 * @property equalityThreshold The threshold used for comparing floating-point numbers for equality. This value must be
 *   non-negative and not NaN.
 * @property random The default random number generator used throughout the framework. This can be overridden to
 *   customize random behavior.
 * @property toStringMode The mode that determines how objects are converted to strings. Useful for debugging and
 *   logging.
 * @property DEFAULT_CONSOLE_WIDTH The default width of the console output. This is used for formatting text and tables.
 * @property fallbackConsoleWidth The default width of the console output. This is used for formatting text and tables.
 */
object Domain {

    const val DEFAULT_EQUALITY_THRESHOLD = 1E-10

    var dispatcher: CoroutineDispatcher = Dispatchers.Default

    var equalityThreshold = DEFAULT_EQUALITY_THRESHOLD
        set(value) {
            constrained {
                "The equality threshold ($value) must be at least 0.0" {
                    value mustNot BeNegative
                }
                "The equality threshold ($value) must be a number" {
                    value mustNot BeNaN
                }
            }.onLeft { throw it }
            field = value
        }

    var random: Random = Random.Default

    const val DEFAULT_CONSOLE_WIDTH = 120

    var fallbackConsoleWidth = DEFAULT_CONSOLE_WIDTH

    var toStringMode = ToStringMode.DEFAULT

    /**
     * Returns a default constructor executor that generates objects sequentially.
     */
    fun <T> defaultConstructor(): ConstructorExecutor<T> = SequentialConstructor()
}
