/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Domain.environments
import cl.ravenhill.keen.Domain.equalityThreshold
import cl.ravenhill.keen.Domain.maxProgramDepth
import cl.ravenhill.keen.Domain.random
import cl.ravenhill.keen.exceptions.constraints.BeNaN
import cl.ravenhill.keen.prog.Environment
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.utils.eq
import kotlin.random.Random
import cl.ravenhill.jakt.constraints.doubles.BeAtLeast as DoubleBeAtLeast


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
 * @property DEFAULT_POPULATION_SIZE The standard size for populations in genetic algorithms, set to 50.
 * @property DEFAULT_SURVIVAL_RATE The standard rate of survival for individuals in a population, set to 0.4.
 * @property equalityThreshold The threshold for floating-point equality comparisons, set to 0.0001. This value is
 *   used in scenarios where floating-point values are compared for equality, such as in the [Double.eq] method.
 * @property toStringMode The mode used for converting objects to their string representation, defaults to
 *   [ToStringMode.DEFAULT].
 * @property random A universal [Random] instance used for stochastic processes, ensuring consistent randomization
 *   strategies and allowing for reproducibility when using a specific seed.
 * @property environments A map of environments by their identifiers, providing access to shared variables and
 *   configurations across different components of the library.
 * @property maxProgramDepth The maximum depth for program trees, defaults to 7. This value is used to prevent the
 *   creation of overly complex and computationally expensive programs.
 */
object Domain {
    @Deprecated(
        "Use the constant associated with the engine instead",
        ReplaceWith("EvolutionEngine.Factory.DEFAULT_POPULATION_SIZE")
    )
    const val DEFAULT_POPULATION_SIZE = 50

    @Deprecated(
        "Use the constant associated with the engine instead",
        ReplaceWith("EvolutionEngine.Factory.DEFAULT_SURVIVAL_RATE")
    )
    const val DEFAULT_SURVIVAL_RATE = 0.4

    const val DEFAULT_EQUALITY_THRESHOLD = 0.0001
    var equalityThreshold = DEFAULT_EQUALITY_THRESHOLD
        set(value) {
            constraints {
                "The equality threshold ($value) must be greater than or equal to zero" {
                   value must DoubleBeAtLeast(0.0)
                   value mustNot BeNaN
                }
            }
            field = value
        }

    var random: Random = Random.Default

    var toStringMode = ToStringMode.DEFAULT

    @ExperimentalKeen
    val environments = mutableMapOf<String, Environment<*>>()

    @ExperimentalKeen
    var maxProgramDepth = Program.DEFAULT_MAX_DEPTH
        set(value) {
            constraints { "The maximum program depth [$value] must be positive" { value must BePositive } }
            field = value
        }

}
