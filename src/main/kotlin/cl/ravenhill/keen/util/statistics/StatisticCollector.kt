/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.statistics



class StatisticCollector<DNA> : AbstractStatistic<DNA>() {
    override fun toString() = """
        ------------ Statistics Collector -------------
        -------------- Selection Times ----------------
        |--> Offspring Selection
        |   |--> Average: ${offspringSelectionTime.average()} ms
        |   |--> Max: ${offspringSelectionTime.maxOrNull()} ms
        |   |--> Min: ${offspringSelectionTime.minOrNull()} ms
        |--> Survivor Selection
        |   |--> Average: ${survivorSelectionTime.average()} ms
        |   |--> Max: ${survivorSelectionTime.maxOrNull()} ms
        |   |--> Min: ${survivorSelectionTime.minOrNull()} ms
        --------------- Alteration Times --------------
        |--> Average: ${alterTime.average()} ms
        |--> Max: ${alterTime.maxOrNull()} ms
        |--> Min: ${alterTime.minOrNull()} ms
        -------------- Evolution Results --------------
        |--> Total time: $evolutionTime ms
        |--> Average generation time: ${generationTimes.average()} ms
        |--> Max generation time: ${generationTimes.maxOrNull()} ms
        |--> Min generation time: ${generationTimes.minOrNull()} ms
        |--> Generation: $generation
        |--> Steady generations: $steadyGenerations
        |--> Fittest: ${population.first()}
        |--> Best fitness: ${bestFitness.last()}
        """.trimIndent()
}