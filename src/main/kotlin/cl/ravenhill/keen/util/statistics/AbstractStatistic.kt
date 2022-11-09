/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.statistics

import cl.ravenhill.keen.core.Genotype


abstract class AbstractStatistic<DNA> {
    var evolutionTime: Long = Long.MAX_VALUE
    var generationTimes: MutableList<Long> = mutableListOf()
    val alterTime: MutableList<Long> = mutableListOf()
    var selectionTime: MutableList<Long> = mutableListOf()
    lateinit var fittest: Genotype<DNA>
    var bestFitness = Double.NaN
    var steadyGenerations: Int = 0
    var generation: Int = 0
}