package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import kotlin.time.ExperimentalTime


/**
 * A statistic printer that prints out the evolutionary results at a given interval.
 *
 * @param every The interval at which to print out the evolutionary results.
 * @param DNA The type of the entities being evolved.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class EvolutionPrinter<DNA, G : Gene<DNA, G>>(private val every: Int) :
        AbstractEvolutionListener<DNA, G>() {

    override fun onResultUpdated() {
        super.onResultUpdated()
        if (evolutionResult.generation % every == 0) {
            println(toString())
        }
    }

    /**
     * Called when the current generation finishes, records the duration of the generation.
     */
    @ExperimentalTime
    override fun onGenerationFinished(population: Population<DNA, G>) {
        _currentGeneration.duration = _currentGeneration.startTime.elapsedNow()
        evolution.generations += _currentGeneration
    }

    @ExperimentalTime
    override fun onGenerationStarted(generation: Int, population: Population<DNA, G>) {
        _currentGeneration = GenerationRecord(generation).apply {
            startTime = timeSource.markNow()
        }
    }

    override fun toString(): String {
        return """ === Generation $generation ===
        |--> Average generation time: ${
            evolution.generations.map { it.duration.inWholeMilliseconds }.average()
        } ms
        |--> Max generation time: ${
            evolution.generations.maxOfOrNull { it.duration.inWholeMilliseconds }
        } ms
        |--> Min generation time: ${
            evolution.generations.minOfOrNull { it.duration.inWholeMilliseconds }
        } ms
        |--> Steady generations: $steadyGenerations
        |--> Best fitness: ${bestFitness.lastOrNull()}
        |--> Worst fitness: ${worstFitness.lastOrNull()}
        |--> Average fitness: ${averageFitness.lastOrNull()}
        |--> Fittest: ${population.firstOrNull()}
        |<<<>>>""".trimMargin()
    }
}