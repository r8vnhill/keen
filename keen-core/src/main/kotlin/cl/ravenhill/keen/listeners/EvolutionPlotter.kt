/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.skia.compose.PlotPanel

/**
 * A specialized evolution listener that plots the progress of an evolutionary computation using Lets-Plot.
 *
 * `EvolutionPlotter` extends `AbstractEvolutionListener` and is designed to visualize the evolution of fitness over
 * generations in an evolutionary computation. It tracks the best, worst, and average fitness across generations and
 * displays them using a graphical plot. This class is particularly useful for analyzing the performance and behavior
 * of the evolutionary computation over time.
 *
 * ## Key Features:
 * - Visual representation of the evolution process, focusing on fitness metrics.
 * - Tracks and plots best, worst, and average fitness values across generations.
 * - Utilizes the Lets-Plot library for creating plots within a Compose Desktop window.
 *
 * ## Usage:
 * Extend or instantiate this class to add plotting capabilities to your evolutionary computation. Override `display` to
 * customize the visualization, and `onGenerationStarted` and `onGenerationEnded` to capture necessary data.
 *
 * ### Example:
 * ```kotlin
 * val plotter = EvolutionPlotter<MyDataType, MyGene>()
 * myGeneticAlgorithm.addEvolutionListener(plotter)
 * ```
 * In this example, `plotter` visualizes the evolution of a evolutionary computation, showing changes in fitness
 * values over generations.
 *
 * @param T The type of data used in the evolution process, typically representing the genetic information.
 * @param G The type of gene used in the evolution process, conforming to the `Gene` subtype.
 */
open class EvolutionPlotter<T, G> : AbstractEvolutionListener<T, G>() where G : Gene<T, G> {

    /**
     * Displays the evolution plot in a Compose Desktop window.
     *
     * This method initializes and shows a window with a plot created using Lets-Plot. The plot illustrates the best,
     * worst, and average fitness values across the generations of the evolutionary computation.
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
     * Computes a triplet of lists containing the best, worst, and average fitness values for each generation.
     *
     * @param generations The list of generation records to analyze.
     * @return A `Triple` of lists representing the best, worst, and average fitness values.
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
     * Creates a fitness plot figure representing the evolution of best, worst, and average fitness over generations.
     *
     * This protected method generates a [Figure] object using the Lets-Plot library, designed to visually represent
     * the evolution of fitness values in a evolutionary computation. It plots the best, worst, and average fitness
     * values across generations, allowing for a comprehensive view of the algorithm's performance and progress.
     *
     * ## Process:
     * 1. **Data Preparation**: Combines the fitness values into a single dataset, categorizing them as 'Best',
     *   'Worst', and 'Average' for distinct visualization.
     * 2. **Plot Configuration**: Configures the x-axis to represent generations and the y-axis to represent fitness
     *   values. Differentiates each fitness category using colors.
     * 3. **Figure Creation**: Utilizes Lets-Plot to generate a line plot that visualizes the fitness trends over
     *   generations.
     *
     * @param best A list of the best fitness values for each generation.
     * @param worst A list of the worst fitness values for each generation.
     * @param average A list of the average fitness values for each generation.
     *
     * @return A `Figure` object representing the fitness trends in a plot.
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

    /**
     * Callback function triggered at the start of each generation during the evolutionary computation's evolutionary
     * process.
     *
     * This method is called at the beginning of each generation in a evolutionary computation. It is responsible for
     * initializing the record for the current generation and adding it to the overall evolution history. This
     * tracking is crucial for analyzing the progress and effectiveness of the evolutionary computation over time.
     *
     * ## Functionality:
     * - **Generation Record Initialization**: Creates a new [GenerationRecord] instance to represent the current
     *   generation. This record includes the generation number and can be extended to contain additional details.
     * - **Evolution Tracking**: Adds the newly created `GenerationRecord` to the `evolution` object's list of
     *   generation records. This step is essential for maintaining a continuous history of the evolutionary process.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary computation framework at the start of each new
     * generation. It is a part of the evolutionary lifecycle and helps in maintaining a historical record of the
     * evolutionary computation's progress.
     *
     * ```kotlin
     * // Example of how this method might be called within a evolutionary computation framework
     * evolutionListeners.forEach { listener ->
     *     listener.onGenerationStarted(currentEvolutionState)
     * }
     * ```
     * In this example, `onGenerationStarted` is called for each registered listener in the evolutionary computation,
     * signaling the commencement of a new generation.
     *
     * @param state The current [EvolutionState] representing the start of the new generation. This state contains
     *   information about the generation number, the population, and other relevant details of the evolutionary
     *   computation's current status.
     */
    override fun onGenerationStarted(state: EvolutionState<T, G>) {
        currentGeneration = GenerationRecord(state.generation)
        evolution.generations += currentGeneration
    }

    /**
     * Callback function triggered at the conclusion of each generation during the evolutionary computation's
     * evolutionary process.
     *
     * This method is invoked at the end of each generation in a evolutionary computation. Its primary responsibility
     * is to update the record of the current generation with information about the offspring population. By capturing
     * the final state of the generation, this method contributes to a comprehensive historical record of the
     * evolutionary process, facilitating in-depth analysis and insights into the algorithm's performance.
     *
     * ## Key Activities:
     * - **Offspring Population Recording**: Updates the [currentGeneration] record with the offspring population
     *   generated during the generation. Each individual in the population is converted into an [IndividualRecord],
     *   capturing both the genotype and fitness.
     * - **Historical Data Completion**: This step finalizes the data for the current generation, ensuring that the
     *   evolutionary history includes complete information about each stage of the process.
     *
     * ## Usage:
     * This method is automatically called by the evolutionary computation framework at the end of each generation. It
     * serves as a crucial component of the evolutionary cycle, ensuring that the progress and outcomes of each
     * generation are accurately documented.
     *
     * ### Example Usage:
     * ```kotlin
     * // Within the evolutionary computation framework
     * evolutionListeners.forEach { it.onGenerationEnded(currentState) }
     * ```
     * In this example, `onGenerationEnded` is executed for each registered listener in the evolutionary computation,
     * signaling the end of a generation and triggering the update of the generation record with offspring data.
     *
     * @param state The current [EvolutionState] at the end of the generation. It includes the final population state
     *   and other relevant details of the evolutionary computation's progression.
     */
    override fun onGenerationEnded(state: EvolutionState<T, G>) {
        currentGeneration.population.offspring = List(state.population.size) {
            IndividualRecord(
                state.population[it].genotype,
                state.population[it].fitness
            )
        }
    }
}
