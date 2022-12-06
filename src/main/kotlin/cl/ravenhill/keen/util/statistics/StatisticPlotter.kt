package cl.ravenhill.keen.util.statistics

import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.plotly.Plot
import tech.tablesaw.plotly.components.Figure
import tech.tablesaw.plotly.traces.ScatterTrace


class StatisticPlotter<DNA> : AbstractStatistic<DNA>() {
    fun displayFitness(before: (Double) -> Double = { it }) {
        val (best, worst, average) = fitnessScatterTraces(before)
        Plot.show(
            Figure.builder()
                .addTraces(best, worst, average)
                .build()
        )
    }

    private fun fitnessScatterTraces(
        before: (Double) -> Double
    ): Triple<ScatterTrace, ScatterTrace, ScatterTrace> {
        val bestColumn = DoubleColumn.create("Best fitness", bestFitness.map { before(it) })
        val worstColumn = DoubleColumn.create("Worst fitness", worstFitness.map { before(it) })
        val averageColumn =
            DoubleColumn.create("Average fitness", averageFitness.map { before(it) })
        val generationColumn = DoubleColumn.create("Generation", (0..generation).toList())
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
        return Triple(bestFitnessScatter, worstFitnessScatter, averageFitnessScatter)
    }
}