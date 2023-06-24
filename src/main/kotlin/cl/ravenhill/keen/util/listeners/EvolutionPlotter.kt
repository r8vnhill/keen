package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord
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
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class EvolutionPlotter<DNA, G: Gene<DNA, G>> : AbstractEvolutionListener<DNA, G>() {

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
        before: (Double) -> Double
    ): Triple<ScatterTrace, ScatterTrace, ScatterTrace> {
        // Create columns for the best, worst, and average fitness.
        val bestColumn = DoubleColumn.create("Best fitness", bestFitness.map { before(it) })
        val worstColumn = DoubleColumn.create("Worst fitness", worstFitness.map { before(it) })
        val averageColumn = DoubleColumn.create("Average fitness", averageFitness.map { before(it) })
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
}