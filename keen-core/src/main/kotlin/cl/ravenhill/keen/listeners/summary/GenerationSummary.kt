package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener.Companion.computeSteadyGenerations
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.utils.Box
import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * A class that summarizes information about each generation in the evolutionary computation process.
 *
 * ## Usage:
 * This class implements the `GenerationListener` interface to handle events occurring at the start and end of each
 * generation, and records relevant information about the generation.
 *
 * ### Example 1: Creating a Generation Summary
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val generationSummary = GenerationSummary(config)
 * ```
 *
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property evolution the record of the evolution process
 * @property timeSource the source of time
 * @property ranker the ranker used for ranking individuals
 * @property precision the function used to measure the duration
 * @property currentGeneration the current generation record
 *
 * @see ListenerConfiguration for details on configuring listeners
 */
class GenerationSummary<T, G>(
    configuration: ListenerConfiguration<T, G>
) : GenerationListener<T, G> where G : Gene<T, G> {

    private val evolution: EvolutionRecord<T, G> = configuration.evolution
    private val timeSource: TimeSource = configuration.timeSource
    private val ranker: IndividualRanker<T, G> = configuration.ranker
    private val precision: Duration.() -> Long = configuration.precision
    private val currentGeneration: Box<GenerationRecord<T, G>?> = configuration.currentGeneration

    /**
     * Called when a generation starts. This method initializes a new generation record, sets the start time, and
     * records the initial state of the population's parents.
     *
     * ## Usage:
     * This method is used within the `GenerationSummary` class to handle the initialization of a new generation.
     *
     * ### Example 1: Creating a Generation Summary
     * ```
     * val config = ListenerConfiguration<Int, MyGene>()
     * val generationSummary = GenerationSummary(config)
     *
     * val state = EvolutionState(
     *     generation = 1,
     *     ranker = FitnessMaxRanker(),
     *     population = listOf(Individual(...), Individual(...), Individual(...))
     * )
     * generationSummary.onGenerationStarted(state)
     * // The generation summary now contains information about the initial population
     * ```
     *
     * @param state the current state of the evolution process, containing information about the population at the start
     *  of the generation
     */
    override fun onGenerationStarted(state: EvolutionState<T, G>) {
        currentGeneration.value = GenerationRecord<T, G>(evolution.generations.size + 1).apply {
            startTime = timeSource.markNow()
            this.population.parents = List(state.population.size) {
                IndividualRecord(state.population[it].genotype, state.population[it].fitness)
            }
        }
        evolution.generations += currentGeneration.value!!
    }

    /**
     * Called when a generation ends. This method updates the current generation record with the duration and offspring
     * information.
     *
     * ## Usage:
     * This method is used within the `GenerationSummary` class to handle the finalization of a generation, recording
     * the duration of the generation and the state of the population's offspring.
     *
     * ### Example 1: Ending a Generation
     * ```
     * val config = ListenerConfiguration<Int, MyGene>()
     * val generationSummary = GenerationSummary(config)
     *
     * val state = EvolutionState(
     *     generation = 1,
     *     ranker = FitnessMaxRanker(),
     *     population = listOf(Individual(...), Individual(...), Individual(...))
     * )
     * generationSummary.onGenerationStarted(state)
     * // Perform evolution steps...
     * generationSummary.onGenerationEnded(state)
     * // The generation summary now contains information about the offspring population and duration of the generation
     * ```
     *
     * @param state the current state of the evolution process, containing information about the population at the end
     *  of the generation
     */
    override fun onGenerationEnded(state: EvolutionState<T, G>) = mapGeneration(currentGeneration) {
        duration = startTime.elapsedNow().precision()
        population.offspring = List(state.population.size) { index ->
            IndividualRecord(state.population[index].genotype, state.population[index].fitness)
        }
        steady = computeSteadyGenerations(ranker, evolution)
    }
}
