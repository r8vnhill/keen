/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.core.Genotype
import cl.ravenhill.keen.util.Optimizer

interface Selector<DNA> {
    operator fun invoke(
        population: List<Genotype<DNA>>,
        count: Int,
        optimizer: Optimizer
    ): List<Genotype<DNA>>
}