/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.statistics

import org.jetbrains.kotlinx.dataframe.math.mean


class StatisticPrinter<DNA>(private val every: Int) : AbstractStatistic<DNA>() {
    override var generation: Int = 0
        set(value) {
            field = value
            if (value % every == 0) {
                println(toString())
            }
        }
    override fun toString(): String {
        return """ === Generation $generation ===
        |--> Average generation time: ${generationTimes.mean()} ms
        |--> Max generation time: ${generationTimes.maxOrNull()} ms
        |--> Min generation time: ${generationTimes.minOrNull()} ms
        |--> Steady generations: $steadyGenerations
        |--> Fittest: $fittest
        |--> Best fitness: $bestFitness
        |<<<>>>""".trimMargin()
    }
}