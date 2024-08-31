/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.printer

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.consoleWidth
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import kotlinx.coroutines.CoroutineDispatcher

/**
 * A listener that prints detailed information about the evolutionary process at specified intervals.
 *
 * The `EvolutionPrinter` class is a specialized listener designed to monitor and display the progress of an
 * evolutionary algorithm. It extends the functionality of the `GenerationPrinterListener` by printing information
 * about the evolutionary process after a specified number of generations have been processed.
 *
 * ## Usage:
 * This class can be used to gain insights into the evolutionary process by providing periodic updates on key metrics
 * such as generation time, fitness statistics, and the number of steady generations. It is configured to print these
 * details every `n` generations, as specified by the `every` parameter.
 *
 * ### Example: Configuring an `EvolutionPrinter` to Print Every 5 Generations
 * ```kotlin
 * val evolutionPrinter = EvolutionPrinter<T, F, R, S>(5)(configuration)
 * ```
 *
 * In this example, the `EvolutionPrinter` will display the evolution details every 5 generations.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property every The interval (in generations) at which the evolution details are printed.
 * @property generationPrinter An instance of [GenerationPrinterListener] used to record generation details.
 */
class EvolutionPrinter<T, F, R, S> private constructor(
    private val every: Int,
    configuration: ListenerConfiguration<T, F, R>,
    private val generationPrinter: GenerationPrinterListener<T, F, R, S> = GenerationPrinterListener(configuration)
) : GenerationListener<T, F, R, S> by generationPrinter
        where F : Feature<T, F>,
              R : Representation<T, F>,
              S : EvolutionState<T, F, R, S> {

    private val evolution by lazy { configuration.evolution }

    private val ranker by lazy { configuration.ranker }

    private val timeUnit by lazy { configuration.precision.unit }

    /**
     * Called at the end of each generation.
     *
     * This method delegates the generation end processing to the `GenerationPrinterListener` and checks if the current
     * generation is a multiple of `every`. If so, it prints the evolution details.
     *
     * @param state The current evolutionary state at the end of the generation.
     */
    override suspend fun onGenerationEnd(state: S) {
        generationPrinter.onGenerationEnd(state)
        if (state.generation % every == 0) {
            display()
        }
    }

    /**
     * Displays the evolution details.
     *
     * This method prints out the statistics of the evolutionary process, including generation times, fitness metrics,
     * and the fittest individual. It is invoked periodically based on the [every] interval.
     */
    override suspend fun display() {
        val consoleWidth = consoleWidth()
        val generations = evolution.generations

        val tableContent = if (generations.isEmpty()) {
            "No generations have been processed yet."
        } else {
            val lastGeneration = generations.last()
            val sortedOffspring = ranker.sort(lastGeneration.population.offspring.map { it.toIndividual() })

            // Build the table content
            val content = listOf(
                "Generation ${evolution.generations.size}",
                "Average generation time: ${evolution.generations.map { it.duration }.average()} $timeUnit",
                "Max generation time: ${evolution.generations.maxOfOrNull { it.duration }} $timeUnit",
                "Min generation time: ${evolution.generations.minOfOrNull { it.duration }} $timeUnit",
                "Steady generations: ${lastGeneration.steady}",
                "Best fitness: ${sortedOffspring.last().fitness}",
                "Worst fitness: ${sortedOffspring.first().fitness}",
                "Average fitness: ${lastGeneration.population.offspring.map { it.fitness }.average()}",
                "Fittest: ${sortedOffspring.last().representation}"
            )

            // Calculate the maximum length of the lines and adjust it based on console width
            val maxLength = content.maxOf { it.length }
            val adjustedMaxLength = minOf(maxLength, consoleWidth - 4) // Account for borders and padding

            // Generate borders
            val border = "-".repeat(adjustedMaxLength + 2) // Adjust for padding

            // Format the content with borders
            val borderedContent = content.joinToString("\n") { "|| ${it.padEnd(adjustedMaxLength)} |" }

            // Combine everything into a single output
            """
            |+${border}+
            $borderedContent
            |+${border}+
            """.trimMargin()
        }
        println(tableContent)
    }

    companion object {
        /**
         * Factory function for creating an `EvolutionPrinter` with the specified interval.
         *
         * @param every The interval (in generations) at which the evolution details should be printed.
         * @return A function that takes a [ListenerConfiguration] and returns an [EvolutionPrinter] instance.
         */
        operator fun <T, F, R> invoke(
            every: Int
        ): (ListenerConfiguration<T, F, R>) -> EvolutionPrinter<T, F, R, out EvolutionState<T, F, R, *>>
                where F : Feature<T, F>,
                      R : Representation<T, F> = { configuration -> EvolutionPrinter(every, configuration) }
    }
}
