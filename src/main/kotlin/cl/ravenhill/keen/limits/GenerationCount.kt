/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.util.validateAtLeast

/**
 * Limits the number of generations the evolution will run.
 *
 * @param i The number of generations to run.
 */
class GenerationCount(private val i: Int) : Match({ generation >= i }) {
    init {
        i.validateAtLeast(1) { "Generation count [$i] must be at least 1" }
    }
}
