package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.EvaluationListener

/**
 * A class that summarizes information about the evaluation phase in the evolutionary computation process.
 *
 * ## Usage:
 * This class implements the `EvaluationListener` interface to handle events occurring at the start and end of the
 * evaluation phase, and records relevant information about the evaluation.
 *
 * ### Example 1: Creating an Evaluation Summary
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val evaluationSummary = EvaluationSummary(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * evaluationSummary.onEvaluationStarted(state)
 * // Perform evaluation steps...
 * evaluationSummary.onEvaluationEnded(state)
 * // The evaluation summary now contains information about the duration of the evaluation
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property timeSource the source of time
 * @property currentGeneration the current generation record
 * @property precision the function used to measure the duration
 */
class EvaluationSummary<T, G>(configuration: ListenerConfiguration<T, G>) :
    EvaluationListener<T, G> where G : Gene<T, G> {

    private val timeSource = configuration.timeSource
    private val currentGeneration = configuration.currentGeneration
    private val precision = configuration.precision

    /**
     * Called when the evaluation phase starts. Sets the start time of the evaluation.
     *
     * @param state the current state of the evolution process
     */
    override fun onEvaluationStarted(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        evaluation.startTime = timeSource.markNow()
    }

    /**
     * Called when the evaluation phase ends. Updates the evaluation record with the duration.
     *
     * @param state the current state of the evolution process
     */
    override fun onEvaluationEnded(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        evaluation.duration = evaluation.startTime.elapsedNow().precision()
    }
}
