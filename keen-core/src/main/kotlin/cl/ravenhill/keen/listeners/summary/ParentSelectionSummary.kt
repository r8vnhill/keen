package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.ParentSelectionListener

/**
 * A class that summarizes information about the parent selection phase in the evolutionary computation process.
 *
 * ## Usage:
 * This class implements the `ParentSelectionListener` interface to handle events occurring at the start and end of the
 * parent selection phase, and records relevant information about the selection.
 *
 * ### Example 1: Creating a Parent Selection Summary
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val parentSelectionSummary = ParentSelectionSummary(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * parentSelectionSummary.onParentSelectionStarted(state)
 * // Perform parent selection steps...
 * parentSelectionSummary.onParentSelectionEnded(state)
 * // The parent selection summary now contains information about the duration of the parent selection
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property timeSource the source of time
 * @property currentGeneration the current generation record
 * @property precision the function used to measure the duration
 */
class ParentSelectionSummary<T, G>(configuration: ListenerConfiguration<T, G>) :
    ParentSelectionListener<T, G> where G : Gene<T, G> {

    private val timeSource = configuration.timeSource
    private val currentGeneration = configuration.currentGeneration
    private val precision = configuration.precision

    /**
     * Called when the parent selection phase starts. Sets the start time of the parent selection.
     *
     * @param state the current state of the evolution process
     */
    override fun onParentSelectionStarted(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        parentSelection.startTime = timeSource.markNow()
    }

    /**
     * Called when the parent selection phase ends. Updates the parent selection record with the duration.
     *
     * @param state the current state of the evolution process
     */
    override fun onParentSelectionEnded(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        parentSelection.duration = parentSelection.startTime.elapsedNow().precision()
    }
}
