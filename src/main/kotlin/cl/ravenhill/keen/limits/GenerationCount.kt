package cl.ravenhill.keen.limits

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast

/**
 * Limits the number of generations the evolution will run.
 *
 * @param i The number of generations to run.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class GenerationCount(private val i: Int) : Match({ generation >= i }) {
    init {
        enforce { i should BeAtLeast(1) { "Generation count [$i] must be at least 1" } }
    }
}
