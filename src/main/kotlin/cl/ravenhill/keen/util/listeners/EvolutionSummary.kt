/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import kotlin.time.ExperimentalTime


/**
 * A collector of statistics to be used in the evolutionary algorithm.
 *
 * @param DNA  The type of the gene's value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 1.0.0
 */
@OptIn(ExperimentalTime::class)
class EvolutionSummary<DNA, G : Gene<DNA, G>> : AbstractEvolutionListener<DNA, G>() {
    override fun toString() = """
        ------------ Evolution Summary ---------------
        |--> Initialization time: ${evolution.initializationDuration.inWholeMilliseconds} ms
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
        |--> Average generation time: ${evolution.generationTimes.average()} ms
        |--> Max generation time: ${evolution.generationTimes.maxOrNull()} ms
        |--> Min generation time: ${evolution.generationTimes.minOrNull()} ms
        |--> Generation: $generation
        |--> Steady generations: $steadyGenerations
        |--> Fittest: ${population.firstOrNull().toString().replace("\n", "; ")}
        |--> Best fitness: ${bestFitness.lastOrNull()}
        """.trimIndent()

    @ExperimentalTime
    override fun onGenerationStarted(generation: Int) {
        _currentGeneration = GenerationRecord(generation).apply {
            initTime = timeSource.markNow()
        }
    }

    override fun onGenerationFinished() {
        _currentGeneration.duration = _currentGeneration.initTime.elapsedNow()
        evolution.generations += _currentGeneration
    }

    override fun onInitializationStarted() {
        evolution.initializationStartTime = timeSource.markNow()
    }

    override fun onInitializationFinished() {
        evolution.initializationDuration = evolution.initializationStartTime.elapsedNow()
    }
}