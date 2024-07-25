package cl.ravenhill.keen.listeners.plotter

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord

/**
 * A listener class that records information about each generation in the evolutionary computation process.
 * This information can be used for plotting the progress of the evolution.
 *
 * ## Usage:
 * This class implements the `GenerationListener` interface to handle events occurring at the start and end of each
 * generation, and records relevant information about the generation.
 *
 * ### Example 1: Creating a Generation Plot Listener
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val generationPlotListener = GenerationPlotListener(config)
 *
 * val state = EvolutionState(
 *     generation = 1,
 *     ranker = FitnessMaxRanker(),
 *     population = listOf(Individual(...), Individual(...), Individual(...))
 * )
 * generationPlotListener.onGenerationStarted(state)
 * // Perform generation steps...
 * generationPlotListener.onGenerationEnded(state)
 * // The generation plot listener now contains information about the generation for plotting
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property currentGeneration the current generation record
 * @property evolution the record of the evolution process
 */
class GenerationPlotListener<T, G>(configuration: ListenerConfiguration<T, G>) :
    GenerationListener<T, G> where G : Gene<T, G> {

    private val currentGeneration = configuration.currentGeneration
    private val evolution = configuration.evolution

    /**
     * Called when a generation starts. Initializes a new generation record and adds it to the evolution record.
     *
     * @param state the current state of the evolution process
     */
    override fun onGenerationStarted(state: GeneticEvolutionState<T, G>) {
        currentGeneration.value = GenerationRecord(state.generation)
        evolution.generations += currentGeneration.value!!
    }

    /**
     * Called when a generation ends. Updates the current generation record with the offspring information.
     *
     * @param state the current state of the evolution process
     */
    override fun onGenerationEnded(state: GeneticEvolutionState<T, G>) = mapGeneration(currentGeneration) {
        population.offspring = List(state.population.size) {
            IndividualRecord(
                state.population[it].genotype,
                state.population[it].fitness
            )
        }
    }
}
