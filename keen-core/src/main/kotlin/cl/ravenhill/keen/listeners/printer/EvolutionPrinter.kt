/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.printer

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.GenerationListener


/**
 * A class that prints detailed information about the evolutionary computation process at specified intervals.
 * This class extends `AbstractEvolutionListener` and uses `GenerationPrinterListener` to handle generation events and
 * print information.
 *
 * ## Usage:
 * This class implements the `GenerationListener` interface to handle events occurring at the end of each generation,
 * and prints relevant information about the evolution process at specified intervals.
 *
 * ### Example 1: Creating an Evolution Printer
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val evolutionPrinter = EvolutionPrinter(10, config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * evolutionPrinter.onGenerationStarted(state)
 * // Perform generation steps...
 * evolutionPrinter.onGenerationEnded(state)
 * // The evolution printer will print information every 10 generations
 * ```
 * @param every the interval at which to print information
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property every the interval at which to print information
 * @property generationPrinter the listener for generation events
 */
class EvolutionPrinter<T, G> private constructor(
    private val every: Int,
    configuration: ListenerConfiguration<T, G>,
    private val generationPrinter: GenerationPrinterListener<T, G>
) : AbstractEvolutionListener<T, G>(),
    GenerationListener<T, G> by GenerationPrinterListener(configuration)
        where G : Gene<T, G> {
    @Deprecated("This property will be removed in future versions. Use configuration objects instead.")
    override val ranker = configuration.ranker

    @Deprecated("This property will be removed in future versions. Use configuration objects instead.")
    override val evolution = configuration.evolution
    private val generations by lazy { evolution.generations }

    /**
     * Initializes the printer with the specified interval and configuration.
     *
     * @param every the interval at which to print information
     * @param configuration the configuration for the listener
     */
    constructor(every: Int, configuration: ListenerConfiguration<T, G> = ListenerConfiguration()) : this(
        every,
        configuration,
        GenerationPrinterListener(configuration)
    )

    /**
     * Called when a generation ends. Updates the current generation record and prints information if the generation
     * number is a multiple of the specified interval.
     *
     * @param state the current state of the evolution process
     */
    override fun onGenerationEnded(state: EvolutionState<T, G>) {
        generationPrinter.onGenerationEnded(state)
        if (state.generation % every == 0) {
            display()
        }
    }

    /**
     * Prints a detailed summary of the evolution process, including statistics for the latest generation such as
     * average, maximum, and minimum generation times, number of steady generations, and fitness values of the
     * population. The summary provides insights into the evolution's progress and performance.
     *
     * ## Usage:
     * Call this method to print the current state of the evolution process.
     *
     * ### Example 1: Displaying the Evolution Summary
     * ```
     * val config = ListenerConfiguration<Int, MyGene>()
     * val evolutionPrinter = EvolutionPrinter(10, config)
     *
     * val state = EvolutionState(
     *     generation = 1,
     *     ranker = FitnessMaxRanker(),
     *     population = listOf(Individual(...), Individual(...), Individual(...))
     * )
     * evolutionPrinter.onGenerationStarted(state)
     * // Perform generation steps...
     * evolutionPrinter.onGenerationEnded(state)
     * // The evolution printer will print information every 10 generations
     * evolutionPrinter.display()
     * ```
     */
    override fun display() = println(
        if (evolution.generations.isEmpty()) {
            "No generations have been processed yet."
        } else {
            val lastGeneration = generations.last()
            """
        === Generation ${evolution.generations.size} ===
        |--> Average generation time: ${evolution.generations.map { it.duration }.average()} ns
        |--> Max generation time: ${evolution.generations.maxOfOrNull { it.duration }} ns
        |--> Min generation time: ${evolution.generations.minOfOrNull { it.duration }} ns
        |--> Steady generations: ${lastGeneration.steady}
        |--> Best fitness: ${lastGeneration.population.offspring.first().fitness}
        |--> Worst fitness: ${lastGeneration.population.offspring.last().fitness}
        |--> Average fitness: ${lastGeneration.population.offspring.map { it.fitness }.average()}
        |--> Fittest: ${lastGeneration.population.offspring.first().genotype}
        |<<<>>>
        """.trimMargin()
        }
    )
}
