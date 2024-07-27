/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.ExperimentalJakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeAtLeast
import cl.ravenhill.jakt.constraints.doubles.BeNaN
import cl.ravenhill.keen.Domain.random
import cl.ravenhill.keen.Domain.toStringMode
import kotlin.random.Random

/**
 * The `Domain` object functions as a centralized configuration resource for the evolutionary computation framework.
 *
 * It offers globally accessible settings and tools that are pivotal in shaping the behavior and effectiveness of
 * evolutionary algorithms. This centralization of configurations promotes uniform behavior across various segments
 * of the system and simplifies the modification of key parameters.
 *
 * ## Usage:
 * Adjusting `Domain` settings can have a wide-reaching impact on the evolutionary computation framework. For
 * instance, modifying the `random` instance affects how randomness is integrated throughout the system.
 *
 * ### Example:
 * ```kotlin
 * // Customizing the random instance for predictable results
 * Domain.random = Random(1234L)
 *
 * // Accessing the standard population size
 * val popSize = Domain.DEFAULT_POPULATION_SIZE
 * ```
 * Here, the global random instance is assigned a specific seed for consistent stochastic behaviors across runs.
 * The default population size is also demonstrated as a readily available constant.
 *
 * @property toStringMode The mode used for converting objects to their string representation, defaults to
 *   [ToStringMode.DEFAULT].
 * @property random A universal [Random] instance used for stochastic processes, ensuring consistent randomization
 *   strategies and allowing for reproducibility when using a specific seed.
 */
object Domain {
    const val DEFAULT_EQUALITY_THRESHOLD = 0.0001

    @OptIn(ExperimentalJakt::class)
    var equalityThreshold = DEFAULT_EQUALITY_THRESHOLD
        set(value) {
            constraints {
                "The equality threshold ($value) must be greater than or equal to zero" {
                    value must BeAtLeast(0.0)
                    value mustNot BeNaN
                }
            }
            field = value
        }

    var random: Random = Random.Default

    var toStringMode = ToStringMode.DEFAULT
}
