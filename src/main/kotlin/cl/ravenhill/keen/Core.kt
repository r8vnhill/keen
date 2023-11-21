/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Core.DEFAULT_MAX_PROGRAM_DEPTH
import cl.ravenhill.keen.Core.maxProgramDepth
import cl.ravenhill.keen.Core.random
import cl.ravenhill.keen.prog.Environment
import kotlin.random.Random

/**
 * The `Core` singleton provides global settings and utilities shared across the genetic programming library.
 *
 * It manages environmental configurations, randomization behaviors, and sets constraints for program trees
 * to ensure they do not exceed specified complexity limits.
 *
 * The `Core` settings play a critical role in the behavior and performance of evolutionary computation operations,
 * providing a centralized point of configuration and control.
 *
 * @property maxProgramDepth Specifies the maximum depth that program trees are allowed to reach. This limit
 * helps prevent the creation of overly complex and computationally expensive programs.
 * It must be a positive integer.
 * Defaults to [Core.DEFAULT_MAX_PROGRAM_DEPTH].
 * @property DEFAULT_MAX_PROGRAM_DEPTH Immutable default value for the maximum program tree depth.
 * @property random A centralized random number generator that provides a uniform source of randomness,
 *  ensuring reproducibility and coherence in random operations throughout the library.
 * @property environments Stores environments by their identifiers, providing access to shared variables
 *  and configurations across different components of the library.
 *
 * @author <a[href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
object Core {

    const val DEFAULT_MAX_PROGRAM_DEPTH = 7

    val environments: MutableMap<String, Environment<*>> = mutableMapOf()

    var maxProgramDepth = DEFAULT_MAX_PROGRAM_DEPTH
        set(value) {
            constraints { "The maximum program depth [$value] must be positive" { value must BePositive } }
            field = value
        }

    var random: Random = Random.Default
}
