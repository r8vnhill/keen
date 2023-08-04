package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import kotlin.time.ExperimentalTime


/**
 * The `EvolutionPrinter` class provides functionality to print out results of an evolutionary
 * process periodically. This is useful for tracking the progress of the genetic algorithm,
 * including the duration of generations, and the fitness of populations within each generation.
 *
 * @param every The interval (in terms of generations) at which the evolutionary results should be printed.
 * @param DNA The type parameter representing the entities being evolved.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionPrinter<DNA, G : Gene<DNA, G>>(private val every: Int) :
        AbstractEvolutionListener<DNA, G>() {

    /**
     * Updates the result of evolution and checks if the generation meets the interval for printing.
     * If so, the evolutionary results are printed.
     */
    override fun onResultUpdated() {
        super.onResultUpdated()
        if (evolutionResult.generation % every == 0) {
            println(toString())
        }
    }

    /**
     * Invoked when the current generation finishes its execution. This method records the duration of
     * the generation, sorts the population and updates the steady generations count. It also adds the
     * current generation to the list of processed generations.
     */
    @ExperimentalTime
    override fun onGenerationFinished(population: Population<DNA, G>) {
        _currentGeneration.duration = _currentGeneration.startTime.elapsedNow()
        evolution.generations += _currentGeneration
        // Sort population and set resulting
        _currentGeneration.population.resulting =
            EvolutionListener.computePopulation(optimizer, population)
        // Calculate steady generations
        generations.lastOrNull()?.let { lastGeneration ->
            EvolutionListener.computeSteadyGenerations(lastGeneration, _currentGeneration)
        }
        // Add current generation to the list of generations
        _currentGeneration.also { evolution.generations += it }
    }

    /**
     * Invoked when a new generation starts. It creates a new GenerationRecord instance and
     * marks the start time of the generation.
     */
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
        |--> Steady generations: ${generations.last().steady}
        |--> Best fitness: ${generations.last().population.resulting.first().fitness}
        |--> Worst fitness: ${generations.last().population.resulting.last().fitness}
        |--> Average fitness: ${
            generations.last().population.resulting.map { it.fitness }.average()
        }
        |--> Fittest: ${generations.last().population.resulting.first().genotype}
        |<<<>>>""".trimMargin()
    }
}