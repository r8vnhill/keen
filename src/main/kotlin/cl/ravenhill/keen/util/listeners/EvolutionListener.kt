package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlin.reflect.KProperty
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Type alias for a list of [EvolutionListener] instances.
 */
typealias Listeners<DNA, G> = List<EvolutionListener<DNA, G>>

/**
 * Interface for a listener that monitors the evolution process in a genetic algorithm.
 *
 * The [EvolutionListener] provides hooks for various points in the evolution process,
 * allowing for fine-grained monitoring and recording of various statistics and state.
 *
 * @property evolution The current state of the evolution.
 * @property evolutionResult The result of the evolution after each generation.
 * @property population The current population of candidate solutions.
 * @property optimizer The phenotype optimizer used in the evolution.
 * @property survivorSelectionTime The list of times taken for survivor selection across
 *          generations.
 * @property offspringSelectionTime The list of times taken for offspring selection across
 *          generations.
 * @property alterTime The list of times taken for the alteration phase across generations.
 * @property evolutionTime The total time taken for the evolution so far.
 * @property bestFitness The list of best fitness values across generations.
 * @property worstFitness The list of worst fitness values across generations.
 * @property averageFitness The list of average fitness values across generations.
 * @property fittest The fittest phenotype in the current generation.
 * @property steadyGenerations The number of generations with no improvement.
 * @property generation The current generation.
 * @property currentGeneration The [GenerationRecord] for the current generation.
 * @property timeSource The source of time measurement.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface EvolutionListener<DNA, G : Gene<DNA, G>> {
    var evolution: EvolutionRecord<DNA, G>
    var evolutionResult: EvolutionResult<DNA, G>
    var population: Population<DNA, G>
    var optimizer: PhenotypeOptimizer<DNA, G>
    val survivorSelectionTime: MutableList<Long>
    val offspringSelectionTime: MutableList<Long>
    val alterTime: MutableList<Long>
    var evolutionTime: Long
    var bestFitness: MutableList<Double>
    var worstFitness: MutableList<Double>
    var averageFitness: MutableList<Double>
    val fittest: Phenotype<DNA, G>?
    var steadyGenerations: Int
    var generation: Int
    val currentGeneration: GenerationRecord
    @ExperimentalTime
    var timeSource: TimeSource

    /**
     * Called whenever the result of the evolution is updated.
     */
    fun onResultUpdated()

    /**
     * Called whenever the evolution process is over (the termination criteria is met).
     */
    fun onEvolutionEnded() { /* Do nothing */ }

    fun onGenerationShift(prop: KProperty<*>, old: Int, new: Int) { /* Do nothing */ }

    fun onGenerationStarted(generation: Int) { /* Do nothing */ }

    fun onGenerationFinished() { /* Do nothing */ }

    fun onInitializationStarted() { /* Do nothing */ }

    fun onInitializationFinished() { /* Do nothing */ }

    fun onEvaluationStarted() { /* Do nothing */ }

    fun onEvaluationFinished() { /* Do nothing */ }
}
