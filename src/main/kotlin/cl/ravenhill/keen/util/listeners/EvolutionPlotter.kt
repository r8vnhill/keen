/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.plotly.Plot
import tech.tablesaw.plotly.components.Figure
import tech.tablesaw.plotly.components.Layout
import tech.tablesaw.plotly.traces.ScatterTrace

/**
 * A class for plotting the evolution fitness statistics gathered during the evolutionary process.
 * The statistics are gathered by the `AbstractStatistic` class during the evolutionary process.
 * The statistics are then plotted using the [Plot] class from the
 * [Tablesaw](https://jtablesaw.github.io/tablesaw/) library.
 *
 * @param DNA The type of the DNA.
 * @param G The gene type, which contains [DNA] type data and conforms to [Gene].
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionPlotter<DNA, G : Gene<DNA, G>> : AbstractEvolutionListener<DNA, G>() {

    /**
     * Displays a plot of the fitness of the population throughout evolution.
     *
     * @param before A function to apply to each fitness value before plotting.
     */
    fun displayFitness(before: (Double) -> Double = { it }) {
        // Get the traces for the best, worst, and average fitness.
        val (best, worst, average) = fitnessScatterTraces(before)
        // Display the plot.
        Plot.show(
            Figure.builder()
                .addTraces(best, worst, average)
                .layout(Layout.builder("Evolution fitness").build())
                .build()
        )
    }

    /**
     * Generates traces for the best, worst, and average fitness of the population.
     *
     * @param before A function to apply to each fitness value before plotting.
     * @return A triple of the best, worst, and average fitness traces.
     */
    private fun fitnessScatterTraces(
        before: (Double) -> Double,
    ): Triple<ScatterTrace, ScatterTrace, ScatterTrace> {
        val generations = evolution.generations
        // Create columns for the best, worst, and average fitness.
        val bestColumn = DoubleColumn.create(
            "Best fitness",
            generations.map { it.population.resulting.first().fitness }.map(before)
        )
        val worstColumn = DoubleColumn.create("Worst fitness", generations.map {
            it.population.resulting.last().fitness
        }.map(before))
        val averageColumn =
            DoubleColumn.create("Average fitness", generations.map {
                it.population.resulting.map { p ->
                    p.fitness
                }.average()
            }.map(before))
        // Create a column for the generation number.
        val generationColumn = DoubleColumn.create("Generation", (0..generation).toList())
        // Create traces for the best, worst, and average fitness.
        val bestFitnessScatter = ScatterTrace.builder(generationColumn, bestColumn)
            .name("Best fitness")
            .mode(ScatterTrace.Mode.LINE)
            .build()
        val worstFitnessScatter = ScatterTrace.builder(generationColumn, worstColumn)
            .name("Worst fitness")
            .mode(ScatterTrace.Mode.LINE)
            .build()
        val averageFitnessScatter = ScatterTrace.builder(generationColumn, averageColumn)
            .name("Average fitness")
            .mode(ScatterTrace.Mode.LINE)
            .build()
        // Return a triple of the best, worst, and average fitness traces.
        return Triple(bestFitnessScatter, worstFitnessScatter, averageFitnessScatter)
    }

    override fun onGenerationStarted(generation: Int, population: Population<DNA, G>) {
        currentGenerationRecord = GenerationRecord(generation)
    }

    override fun onGenerationFinished(population: Population<DNA, G>) {
        val sorted = optimizer.sort(population)
        currentGenerationRecord.population.resulting = List(sorted.size) {
            IndividualRecord(
                sorted[it].genotype,
                sorted[it].fitness
            )
        }
        evolution.generations += currentGenerationRecord
    }
}
