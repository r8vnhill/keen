/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive

/**
 * Limits the number of generations the evolution will run.
 *
 * @param i The number of generations to run.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
data class GenerationCount(private val i: Int) : MatchLimit({ generation >= i }) {
    init {
        constraints { "Generation count [$i] must be at least 1" { i must BePositive } }
    }
}
