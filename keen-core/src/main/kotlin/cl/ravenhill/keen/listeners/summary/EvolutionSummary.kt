/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.fittest
import cl.ravenhill.keen.listeners.mixins.AlterationListener
import cl.ravenhill.keen.listeners.mixins.EvaluationListener
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.mixins.InitializationListener
import cl.ravenhill.keen.listeners.mixins.ParentSelectionListener
import cl.ravenhill.keen.listeners.mixins.SurvivorSelectionListener

/**
 * A comprehensive class that summarizes the entire evolutionary computation process, including initialization,
 * evaluation, parent selection, survivor selection, and alteration phases.
 *
 * ## Usage:
 * This class implements various listener interfaces to handle events occurring at different phases of the evolutionary
 * process and records relevant information. It also provides a method to display a summary of the evolution.
 *
 * ### Example 1: Creating an Evolution Summary
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val evolutionSummary = EvolutionSummary(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * evolutionSummary.onEvolutionStarted(state)
 * // Perform evolutionary steps...
 * evolutionSummary.onEvolutionEnded(state)
 * // Display the summary of the evolution process
 * evolutionSummary.display()
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property precision the function used to measure the duration
 */
class EvolutionSummary<T, G>(
    configuration: ListenerConfiguration<T, G> = ListenerConfiguration()
) : AbstractEvolutionListener<T, G>(configuration),
    GenerationListener<T, G> by GenerationSummary(configuration),
    InitializationListener<T, G> by InitializationSummary(configuration),
    EvaluationListener<T, G> by EvaluationSummary(configuration),
    ParentSelectionListener<T, G> by ParentSelectionSummary(configuration),
    SurvivorSelectionListener<T, G> by SurvivorSelectionSummary(configuration),
    AlterationListener<T, G> by AlterationSummary(configuration)
        where G : Gene<T, G> {

    private val precision = configuration.precision

    /**
     * Displays a detailed summary of the evolution process. The summary includes the times for various phases such as
     * initialization, evaluation, parent selection, survivor selection, alteration, and the overall evolution duration.
     *
     * The summary also provides statistics like the average, maximum, and minimum times for each phase, as well as
     * information about the fittest individual and the number of steady generations.
     *
     * ## Usage:
     * Call this method to print a formatted summary of the entire evolutionary process.
     *
     * ### Example 1: Displaying the Evolution Summary
     * ```
     * val config = ListenerConfiguration<Int, MyGene>()
     * val evolutionSummary = EvolutionSummary(config)
     *
     * val state = EvolutionState(
     *     generation = 1,
     *     ranker = FitnessMaxRanker(),
     *     population = listOf(Individual(...), Individual(...), Individual(...))
     * )
     * evolutionSummary.onEvolutionStarted(state)
     * // Perform evolutionary steps...
     * evolutionSummary.onEvolutionEnded(state)
     * // Display the summary of the evolution process
     * evolutionSummary.display()
     * ```
     */
    override fun display() {
        val generations = evolution.generations
        println(
            """
            ------------ Evolution Summary ---------------
            |--> Initialization time: ${evolution.initialization.duration} ms
            ------------- Evaluation Times ----------------
            |--> Average: ${generations.map { it.evaluation.duration }.average()} ms
            |--> Max: ${generations.maxOfOrNull { it.evaluation.duration }} ms
            |--> Min: ${generations.minOfOrNull { it.evaluation.duration }} ms
            -------------- Selection Times ----------------
            |   |--> Offspring Selection
            |   |   |--> Average: ${generations.map { it.parentSelection.duration }.average()} ms
            |   |   |--> Max: ${generations.maxOfOrNull { it.parentSelection.duration }} ms
            |   |   |--> Min: ${generations.minOfOrNull { it.parentSelection.duration }} ms
            |   |--> Survivor Selection
            |   |   |--> Average: ${generations.map { it.survivorSelection.duration }.average()} ms
            |   |   |--> Max: ${generations.maxOfOrNull { it.survivorSelection.duration }} ms
            |   |   |--> Min: ${generations.minOfOrNull { it.survivorSelection.duration }} ms
            --------------- Alteration Times --------------
            |--> Average: ${generations.map { it.alteration.duration }.average()} ms
            |--> Max: ${generations.maxOfOrNull { it.alteration.duration }} ms
            |--> Min: ${generations.minOfOrNull { it.alteration.duration }} ms
            -------------- Evolution Results --------------
            |--> Total time: ${evolution.duration} ms
            |--> Average generation time: ${generations.map { it.duration }.average()} ms
            |--> Max generation time: ${generations.maxOfOrNull { it.duration }} ms
            |--> Min generation time: ${generations.minOfOrNull { it.duration }} ms
            |--> Generation: ${evolution.generations.last().generation}
            |--> Steady generations: ${evolution.generations.last().steady}
            |--> Fittest: ${fittest(ranker, evolution).genotype}
            |--> Best fitness: ${fittest(ranker, evolution).fitness}
            """.trimIndent()
        )
    }

    /**
     * Called when the evolution phase starts. Sets the start time of the evolution process.
     *
     * @param state the current state of the evolution process
     */
    override fun onEvolutionStarted(state: GeneticEvolutionState<T, G>) {
        evolution.startTime = configuration.timeSource.markNow()
    }

    /**
     * Called when the evolution phase ends. Updates the evolution record with the duration.
     *
     * @param state the current state of the evolution process
     */
    override fun onEvolutionEnded(state: GeneticEvolutionState<T, G>) {
        evolution.duration = evolution.startTime.elapsedNow().precision()
    }
}
