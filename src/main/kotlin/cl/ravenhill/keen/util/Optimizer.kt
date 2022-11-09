/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util

/**
 * Generic optimization strategy to determine which of two values is better.
 */
interface Optimizer {

    /**
     * Returns true if the first value is better than the second one.
     */
    operator fun invoke(a: Double, b: Double): Boolean
}
