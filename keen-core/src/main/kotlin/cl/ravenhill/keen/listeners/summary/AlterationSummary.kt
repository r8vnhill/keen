package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.AlterationListener

/**
 * A class that summarizes information about the alteration phase in the evolutionary computation process.
 *
 * ## Usage:
 * This class implements the `AlterationListener` interface to handle events occurring at the start and end of the
 * alteration phase, and records relevant information about the alteration.
 *
 * ### Example 1: Creating an Alteration Summary
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val alterationSummary = AlterationSummary(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * alterationSummary.onAlterationStarted(state)
 * // Perform alteration steps...
 * alterationSummary.onAlterationEnded(state)
 * // The alteration summary now contains information about the duration of the alteration phase
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property timeSource the source of time
 * @property currentGeneration the current generation record
 * @property precision the function used to measure the duration
 */
class AlterationSummary<T, G>(config: ListenerConfiguration<T, G>) : AlterationListener<T, G> where G : Gene<T, G> {

    private val timeSource = config.timeSource
    private val currentGeneration = config.currentGeneration
    private val precision = config.precision

    /**
     * Called when the alteration phase starts. Sets the start time of the alteration phase.
     *
     * @param state the current state of the evolution process
     */
    override fun onAlterationStarted(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        alteration.startTime = timeSource.markNow()
    }

    /**
     * Called when the alteration phase ends. Updates the alteration record with the duration.
     *
     * @param state the current state of the evolution process
     */
    override fun onAlterationEnded(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        alteration.duration = alteration.startTime.elapsedNow().precision()
    }
}
