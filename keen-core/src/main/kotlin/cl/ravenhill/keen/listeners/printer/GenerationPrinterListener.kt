package cl.ravenhill.keen.listeners.printer

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord

/**
 * A listener class that prints information about each generation in the evolutionary computation process.
 * This listener records the start and end times of each generation and updates the evolution record.
 *
 * ## Usage:
 * This class implements the `GenerationListener` interface to handle events occurring at the start and end of each
 * generation, and records relevant information about the generation.
 *
 * ### Example 1: Creating a Generation Printer Listener
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val generationPrinterListener = GenerationPrinterListener(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * generationPrinterListener.onGenerationStarted(state)
 * // Perform generation steps...
 * generationPrinterListener.onGenerationEnded(state)
 * // The generation printer listener now contains information about the generation
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property currentGeneration the current generation record
 * @property timeSource the source of time
 * @property evolution the record of the evolution process
 * @property ranker the ranker used for ranking individuals
 */
class GenerationPrinterListener<T, G>(configuration: ListenerConfiguration<T, G>) :
    GenerationListener<T, G> where G : Gene<T, G> {

    private val currentGeneration = configuration.currentGeneration
    private val timeSource = configuration.timeSource
    private val evolution = configuration.evolution
    private val ranker = configuration.ranker

    /**
     * Called when a generation starts. Initializes a new generation record and sets the start time.
     *
     * @param state the current state of the evolution process
     */
    override fun onGenerationStarted(state: EvolutionState<T, G>) {
        currentGeneration.value = GenerationRecord<T, G>(state.generation).apply {
            startTime = timeSource.markNow()
        }
    }

    /**
     * Called when a generation ends. Updates the current generation record with the duration and offspring information.
     *
     * @param state the current state of the evolution process
     */
    override fun onGenerationEnded(state: EvolutionState<T, G>) {
        mapGeneration(currentGeneration) {
            duration = startTime.elapsedNow().inWholeNanoseconds
            evolution.generations += this
            population.offspring = List(state.population.size) {
                IndividualRecord(
                    state.population[it].genotype,
                    state.population[it].fitness
                )
            }
            steady = EvolutionListener.computeSteadyGenerations(ranker, evolution)
        }
    }
}
