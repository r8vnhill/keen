/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.statistics

import cl.ravenhill.keen.core.Genotype
import org.jetbrains.kotlinx.dataframe.math.mean


class StatisticCollector<DNA> {
    var evolutionTime: Long = Long.MAX_VALUE
    var generationTimes: MutableList<Long> = mutableListOf()
    val alterTime: MutableList<Long> = mutableListOf()
    var selectionTime: MutableList<Long> = mutableListOf()
    lateinit var fittest: Genotype<DNA>
    var bestFitness = Double.NaN
    var steadyGenerations: Int = 0
    var generation: Int = 0

    override fun toString() = """
        -------- Statistics Collector ---------
        ---------- Selection Times ------------
        |--> Average: ${selectionTime.mean()} ms
        |--> Max: ${selectionTime.maxOrNull()} ms
        |--> Min: ${selectionTime.minOrNull()} ms
        ----------- Alteration Times ----------
        |--> Average: ${alterTime.mean()} ms
        |--> Max: ${alterTime.maxOrNull()} ms
        |--> Min: ${alterTime.minOrNull()} ms
        ---------- Evolution Results ----------
        |--> Total time: $evolutionTime ms
        |--> Average generation time: ${generationTimes.mean()} ms
        |--> Max generation time: ${generationTimes.maxOrNull()} ms
        |--> Min generation time: ${generationTimes.minOrNull()} ms
        |--> Generation: $generation
        |--> Steady generations: $steadyGenerations
        |--> Fittest: $fittest
        |--> Best fitness: $bestFitness
        """.trimIndent()
}