package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.InitializationListener

/**
 * A class that summarizes information about the initialization phase in the evolutionary computation process.
 *
 * ## Usage:
 * This class implements the `InitializationListener` interface to handle events occurring at the start and end of the
 * initialization phase, and records relevant information about the initialization.
 *
 * ### Example 1: Creating an Initialization Summary
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val initializationSummary = InitializationSummary(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * initializationSummary.onInitializationStarted(state)
 * // Perform initialization steps...
 * initializationSummary.onInitializationEnded(state)
 * // The initialization summary now contains information about the duration of the initialization
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property evolution the record of the evolution process
 * @property timeSource the source of time
 * @property precision the function used to measure the duration
 */
class InitializationSummary<T, G>(configuration: ListenerConfiguration<T, G>) :
    InitializationListener<T, G, Any?> where G : Gene<T, G> {

    private val evolution = configuration.evolution
    private val timeSource = configuration.timeSource
    private val precision = configuration.precision

    /**
     * Called when the initialization phase starts. This method sets the start time of the initialization process.
     *
     * ## Usage:
     * This method is used within the `InitializationSummary` class to mark the beginning of the initialization phase.
     *
     * ### Example 1: Starting Initialization
     * ```
     * val config = ListenerConfiguration<Int, MyGene>()
     * val initializationSummary = InitializationSummary(config)
     *
     * val state = EvolutionState(
     *     generation = 1,
     *     ranker = FitnessMaxRanker(),
     *     population = listOf(Individual(...), Individual(...), Individual(...))
     * )
     * initializationSummary.onInitializationStarted(state)
     * // The start time of the initialization is now recorded
     * ```
     *
     * @param state the current state of the evolution process, containing information about the population at the start
     *  of the initialization
     */
    override fun onInitializationStarted(state: GeneticEvolutionState<T, G>) {
        evolution.initialization.startTime = timeSource.markNow()
    }

    /**
     * Called when the initialization phase ends. This method updates the initialization record with the duration of the
     * initialization process.
     *
     * ## Usage:
     * This method is used within the `InitializationSummary` class to mark the end of the initialization phase and
     * record its duration.
     *
     * ### Example 1: Ending Initialization
     * ```
     * val config = ListenerConfiguration<Int, MyGene>()
     * val initializationSummary = InitializationSummary(config)
     *
     * val state = EvolutionState(
     *     generation = 1,
     *     ranker = FitnessMaxRanker(),
     *     population = listOf(Individual(...), Individual(...), Individual(...))
     * )
     * initializationSummary.onInitializationStarted(state)
     * // Perform initialization steps...
     * initializationSummary.onInitializationEnded(state)
     * // The initialization duration is now recorded
     * ```
     *
     * @param state the current state of the evolution process, containing information about the population at the end
     *  of the initialization
     */
    override fun onInitializationEnded(state: GeneticEvolutionState<T, G>) {
        evolution.initialization.duration = evolution.initialization.startTime.elapsedNow().precision()
    }
}
