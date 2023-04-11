/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BePositive

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
        enforce { "Generation count [$i] must be at least 1" { i should BePositive } }
    }
}
