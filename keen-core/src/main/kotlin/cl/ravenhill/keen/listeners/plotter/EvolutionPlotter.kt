/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.plotter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.skia.compose.PlotPanel

/**
 * A class that plots the progress of the evolutionary computation process, including the best, worst, and average
 * fitness values over generations.
 *
 * ## Usage:
 * This class extends `AbstractEvolutionListener` and implements `GenerationListener` to handle events occurring at the
 * start and end of each generation. It uses the `GenerationPlotListener` to record generation information and provides
 * methods to compute fitness statistics and create a fitness plot using Lets-Plot.
 *
 * ### Example 1: Creating an Evolution Plotter
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val evolutionPlotter = EvolutionPlotter(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * evolutionPlotter.onGenerationStarted(state)
 * // Perform generation steps...
 * evolutionPlotter.onGenerationEnded(state)
 * // Display the plot of the evolution process
 * evolutionPlotter.display()
 * ```
 *
 * ### Example 2: Extending the Evolution Plotter
 * ```
 * class CustomEvolutionPlotter<T, G>(config: ListenerConfiguration<T, G>) : EvolutionPlotter<T, G>(config)
 *     where G : Gene<T, G> {
 *
 *     override fun display() {
 *         super.display()
 *         println("Custom message: Evolution process displayed.")
 *     }
 * }
 *
 * val customPlotter = CustomEvolutionPlotter<Int, MyGene>(config)
 * customPlotter.onGenerationStarted(state)
 * // Perform generation steps...
 * customPlotter.onGenerationEnded(state)
 * // Display the custom plot of the evolution process
 * customPlotter.display()
 * ```
 *
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
open class EvolutionPlotter<T, G>(private val configuration: ListenerConfiguration<T, G> = ListenerConfiguration()) :
    AbstractEvolutionListener<T, G>(),
    GenerationListener<T, G> by GenerationPlotListener(configuration)
        where G : Gene<T, G> {
    @Deprecated("This property will be removed in future versions. Use configuration objects instead.")
    override val ranker = configuration.ranker

    @Deprecated("This property will be removed in future versions. Use configuration objects instead.")
    override val evolution = configuration.evolution

    /**
     * Displays a window with a plot of the evolution process, showing the best, worst, and average fitness values over
     * generations.
     */
    override fun display() = application {
        val generations = evolution.generations
        val (bestFitness, worstFitness, averageFitness) = computeFitnessTriplet(generations)

        Window(onCloseRequest = ::exitApplication, title = "Lets-Plot in Compose Desktop (min)") {
            MaterialTheme {
                Column(
                    modifier = Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                ) {
                    PlotPanel(
                        figure = createFitnessFigure(bestFitness, worstFitness, averageFitness),
                        modifier = Modifier.fillMaxSize()
                    ) { computationMessages ->
                        computationMessages.forEach { println("[DEMO APP MESSAGE] $it") }
                    }
                }
            }
        }
    }

    /**
     * Computes the best, worst, and average fitness values for each generation.
     *
     * @param generations the list of generation records
     * @return a triple containing lists of best, worst, and average fitness values
     */
    protected fun computeFitnessTriplet(
        generations: List<GenerationRecord<T, G>>
    ): Triple<List<Double>, List<Double>, List<Double>> {
        val sorted = generations.map { ranker.sort(it.population.offspring.map { record -> record.toIndividual() }) }
        val bestFitness = sorted.map { it.first().fitness }
        val worstFitness = sorted.map { it.last().fitness }
        val averageFitness = sorted.map { population -> population.map { it.fitness }.average() }
        return Triple(bestFitness, worstFitness, averageFitness)
    }

    /**
     * Creates a fitness plot using Lets-Plot, showing the best, worst, and average fitness values over generations.
     *
     * @param best the list of best fitness values for each generation
     * @param worst the list of worst fitness values for each generation
     * @param average the list of average fitness values for each generation
     * @return a Lets-Plot figure
     */
    protected fun createFitnessFigure(best: List<Double>, worst: List<Double>, average: List<Double>): Figure {
        val categoryLabel = "Fitness"
        val data = mapOf<String, Any>(
            categoryLabel to List(best.size) { "Best" } + List(worst.size) { "Worst" }
                    + List(average.size) { "Average" },
            "fitness" to best + worst + average,
            "generations" to best.indices + worst.indices + average.indices
        )

        return letsPlot(data) {
            x = "generations"
            color = "Fitness"
        } + geomLine { y = "fitness" }
    }
}
