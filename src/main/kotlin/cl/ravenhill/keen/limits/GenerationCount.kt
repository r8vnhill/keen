/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive

/**
 * Limits the number of generations the evolution will run.
 *
 * @param i The number of generations to run.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
data class GenerationCount(private val i: Int) : Match({ generation >= i }) {
    init {
        enforce { "Generation count [$i] must be at least 1" { i must BePositive } }
    }
}
