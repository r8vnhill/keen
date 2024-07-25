package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.SurvivorSelectionListener

/**
 * A class that summarizes information about the survivor selection phase in the evolutionary computation process.
 *
 * ## Usage:
 * This class implements the `SurvivorSelectionListener` interface to handle events occurring at the start and end of
 * the survivor selection phase, and records relevant information about the selection.
 *
 * ### Example 1: Creating a Survivor Selection Summary
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val survivorSelectionSummary = SurvivorSelectionSummary(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * survivorSelectionSummary.onSurvivorSelectionStarted(state)
 * // Perform survivor selection steps...
 * survivorSelectionSummary.onSurvivorSelectionEnded(state)
 * // The survivor selection summary now contains information about the duration of the survivor selection
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property timeSource the source of time
 * @property currentGeneration the current generation record
 * @property precision the function used to measure the duration
 */
class SurvivorSelectionSummary<T, G>(configuration: ListenerConfiguration<T, G>) :
    SurvivorSelectionListener<T, G> where G : Gene<T, G> {

    private val timeSource = configuration.timeSource
    private val currentGeneration = configuration.currentGeneration
    private val precision = configuration.precision

    /**
     * Called when the survivor selection phase starts. Sets the start time of the survivor selection.
     *
     * @param state the current state of the evolution process
     */
    override fun onSurvivorSelectionStarted(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        survivorSelection.startTime = timeSource.markNow()
    }

    /**
     * Called when the survivor selection phase ends. Updates the survivor selection record with the duration.
     *
     * @param state the current state of the evolution process
     */
    override fun onSurvivorSelectionEnded(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        survivorSelection.duration = survivorSelection.startTime.elapsedNow().precision()
    }
}
