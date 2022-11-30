/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.statistics

import cl.ravenhill.keen.genetic.Phenotype


abstract class AbstractStatistic<DNA> : Statistic<DNA> {
    override var evolutionTime: Long = Long.MAX_VALUE
    override var generationTimes: MutableList<Long> = mutableListOf()
    override val alterTime: MutableList<Long> = mutableListOf()
    override var offspringSelectionTime: MutableList<Long> = mutableListOf()
    override val survivorSelectionTime: MutableList<Long> = mutableListOf()
    override var fittest: Phenotype<DNA>? = null
    override var bestFitness = Double.NaN
    override var steadyGenerations: Int = 0
    override var generation: Int = 0
}