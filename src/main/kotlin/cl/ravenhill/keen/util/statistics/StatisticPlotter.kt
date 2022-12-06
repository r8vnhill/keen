package cl.ravenhill.keen.util.statistics

import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.plotly.Plot
import tech.tablesaw.plotly.components.Figure
import tech.tablesaw.plotly.traces.ScatterTrace


class StatisticPlotter<DNA> : AbstractStatistic<DNA>() {
    fun displayFitness() {
        val (best, worst, average) = fitnessScatterTraces()
        Plot.show(
            Figure.builder()
                .addTraces(best, worst, average)
                .build()
        )
    }

    private fun fitnessScatterTraces(): Triple<ScatterTrace, ScatterTrace, ScatterTrace> {
        val bestFitnessColumn = DoubleColumn.create("Best fitness", bestFitness)
        val worstFitnessColumn = DoubleColumn.create("Worst fitness", worstFitness)
        val averageFitnessColumn = DoubleColumn.create("Average fitness", averageFitness)
        val generationColumn = DoubleColumn.create("Generation", (0..generation).toList())
        val bestFitnessScatter = ScatterTrace.builder(generationColumn, bestFitnessColumn)
            .name("Best fitness")
            .mode(ScatterTrace.Mode.LINE)
            .build()
        val worstFitnessScatter = ScatterTrace.builder(generationColumn, worstFitnessColumn)
            .name("Worst fitness")
            .mode(ScatterTrace.Mode.LINE)
            .build()
        val averageFitnessScatter = ScatterTrace.builder(generationColumn, averageFitnessColumn)
            .name("Average fitness")
            .mode(ScatterTrace.Mode.LINE)
            .build()
        return Triple(bestFitnessScatter, worstFitnessScatter, averageFitnessScatter)
    }
}