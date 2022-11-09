/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util


/**
 * [Optimizer] that prioritizes the minimum of two values.
 */
class Minimizer : Optimizer {

    override fun invoke(a: Double, b: Double) = a < b

    override fun toString() = "Minimizer"
}