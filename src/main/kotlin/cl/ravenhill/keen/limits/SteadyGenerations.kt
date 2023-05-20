package cl.ravenhill.keen.limits

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive

/**
 * A [Match] limit that checks if the population has remained steady for a given number of
 * generations.
 *
 * @property n The number of steady generations required to satisfy the limit.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
data class SteadyGenerations(val n: Int) : Match({ steadyGenerations >= n }) {
    init {
        enforce { "Steady generations must be positive" { n must BePositive } }
    }

    /// Documentation inherited from [Any]
    override fun toString() = "SteadyGenerations($n)"
}