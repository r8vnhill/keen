/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import kotlin.time.ExperimentalTime

/**
 * A listener for evolutionary processes that prints detailed statistics of each generation.
 * This class extends [AbstractEvolutionListener] and provides insights into each generation's
 * performance and results at specified intervals.
 *
 * ### Functionality:
 * - Tracks and calculates the duration of each generation, allowing performance analysis over time.
 * - Sorts the population based on fitness and updates the resulting population records.
 * - Computes and tracks the number of steady generations (generations without significant fitness change).
 * - Prints a summary of each generation's statistics, including time metrics, steady generation count, and fitness
 *   metrics.
 *
 * ### Usage:
 * To use this listener, add it to your evolutionary algorithm configuration:
 * ```kotlin
 * val printer = EvolutionPrinter<Int, IntGene>(every = 10)
 * val engine = engine(...) {
 *   ...
 *   listeners += printer
 * }
 * engine.evolve()
 * ```
 * This will print detailed statistics of every 10th generation during the evolution process.
 *
 * ### Time Metrics:
 * - Average, maximum, and minimum generation times are calculated and displayed, providing insights into the time
 *   efficiency of the evolutionary process.
 *
 * ### Fitness Metrics:
 * - Best, worst, and average fitness of the population in each generation.
 * - The genotype of the fittest individual.
 *
 * ### Output Format:
 * The output is formatted for easy reading, with clear delineation and organized information.
 * Example output snippet:
 * ```
 * === Generation 10 ===
 * --> Average generation time: 1234 ms
 * --> Max generation time: 2345 ms
 * --> Min generation time: 123 ms
 * ...
 * ```
 *
 * @param DNA The type of the gene's value.
 * @param G The type of the gene.
 * @property every The frequency (in generations) at which the statistics are printed.
 *
 * @see [AbstractEvolutionListener] for base functionality.
 */
class EvolutionPrinter<DNA, G : Gene<DNA, G>>(val every: Int) :
    AbstractEvolutionListener<DNA, G>() {

    @ExperimentalTime
    override fun onGenerationFinished(population: Population<DNA, G>) {
        currentGenerationRecord.duration = currentGenerationRecord.startTime.elapsedNow().inWholeNanoseconds
        evolution.generations += currentGenerationRecord
        // Sort population and set resulting
        currentGenerationRecord.population.resulting =
            EvolutionListener.computePopulation(optimizer, population)
        // Calculate steady generations
        generations.lastOrNull()?.let { lastGeneration ->
            EvolutionListener.computeSteadyGenerations(optimizer, evolution)
        }
        // Add current generation to the list of generations
        currentGenerationRecord.also { evolution.generations += it }
        if (currentGeneration.generation % every == 0) {
            println(toString())
        }
    }

    @ExperimentalTime
    override fun onGenerationStarted(population: Population<DNA, G>) {
        currentGenerationRecord = GenerationRecord<DNA, G>(generations.size + 1).apply {
            startTime = timeSource.markNow()
        }
    }

    override fun toString(): String = if (evolution.generations.isEmpty()) {
        "No generations have been processed yet."
    } else {
        """ === Generation $generation ===
        |--> Average generation time: ${
            evolution.generations.map { it.duration }.average()
        } ms
        |--> Max generation time: ${
            evolution.generations.maxOfOrNull { it.duration }
        } ms
        |--> Min generation time: ${
            evolution.generations.minOfOrNull { it.duration }
        } ms
        |--> Steady generations: ${generations.last().steady}
        |--> Best fitness: ${generations.last().population.resulting.first().fitness}
        |--> Worst fitness: ${generations.last().population.resulting.last().fitness}
        |--> Average fitness: ${
            generations.last().population.resulting.map { it.fitness }.average()
        }
        |--> Fittest: ${generations.last().population.resulting.first().genotype}
        |<<<>>>
        """.trimMargin()
    }
}
