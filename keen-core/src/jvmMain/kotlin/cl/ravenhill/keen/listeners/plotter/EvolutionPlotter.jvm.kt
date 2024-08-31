/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.plotter

import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries

/**
 * Plots the fitness progression over generations in an evolutionary algorithm.
 *
 * This function generates a visual plot that tracks the best, worst, and average fitness values across all generations
 * of an evolutionary process. It uses the provided `EvolutionRecord` and `IndividualRanker` to sort and evaluate the
 * fitness of individuals within each generation.
 *
 * @param evolution The record of the entire evolutionary process, containing data about each generation.
 * @param ranker The ranker used to sort individuals based on their fitness, necessary for determining best, worst,
 *   and average fitness values.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 */
internal actual suspend fun <T, F : Feature<T, F>, R : Representation<T, F>> plot(
    evolution: EvolutionRecord<T, F, R>,
    ranker: IndividualRanker<T, F, R>
) {
    val generations = evolution.generations
    val (bestFitness, worstFitness, averageFitness) = computeFitnessTriplet(generations.toList(), ranker)
    val chart = createFitnessChart(bestFitness, worstFitness, averageFitness)
    showChart(chart)
}

/**
 * Computes the best, worst, and average fitness values for each generation.
 *
 * @param generations A list of generation records from the evolutionary process.
 * @param ranker The ranker used to sort individuals by their fitness values.
 * @return A triple containing lists of best, worst, and average fitness values.
 */
private suspend fun <T, F, R> computeFitnessTriplet(
    generations: List<GenerationRecord<T, F, R>>,
    ranker: IndividualRanker<T, F, R>
): Triple<List<Double>, List<Double>, List<Double>> where F : Feature<T, F>, R : Representation<T, F> {
    val sorted = generations.map { generation ->
        ranker.sort(generation.population.offspring.map { it.toIndividual() })
    }
    val bestFitness = sorted.map { it.first().fitness }
    val worstFitness = sorted.map { it.last().fitness }
    val averageFitness = sorted.map { population ->
        population.sumOf { it.fitness } / population.size
    }
    return Triple(bestFitness, worstFitness, averageFitness)
}

/**
 * Creates a line chart for fitness values over generations.
 *
 * @param best A list of the best fitness values for each generation.
 * @param worst A list of the worst fitness values for each generation.
 * @param average A list of the average fitness values for each generation.
 * @return An XYChart object representing the fitness progression over generations.
 */
private fun createFitnessChart(
    best: List<Double>,
    worst: List<Double>,
    average: List<Double>
): XYChart {
    val chart = XYChartBuilder()
        .width(800)
        .height(600)
        .title("Fitness Over Generations")
        .xAxisTitle("Generations")
        .yAxisTitle("Fitness")
        .build()

    // Add series for best fitness
    val bestSeries = chart.addSeries("Best Fitness", best.indices.map { it.toDouble() }, best)
    bestSeries.xySeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line

    // Add series for worst fitness
    val worstSeries = chart.addSeries("Worst Fitness", worst.indices.map { it.toDouble() }, worst)
    worstSeries.xySeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line

    // Add series for average fitness
    val averageSeries = chart.addSeries("Average Fitness", average.indices.map { it.toDouble() }, average)
    averageSeries.xySeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line

    return chart
}

/**
 * Displays the generated fitness chart.
 *
 * @param chart The fitness chart to be displayed.
 */
private fun showChart(chart: XYChart) {
    val chartWrapper = SwingWrapper(chart)
    chartWrapper.displayChart()
}
