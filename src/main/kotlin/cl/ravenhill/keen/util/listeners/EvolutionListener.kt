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
 * A generic interface for a statistic in an evolutionary algorithm.
 *
 * @property evolutionResult the current result of the evolution.
 * @property population the current population of the evolution.
 * @property optimizer the optimizer used to create new phenotypes.
 * @property survivorSelectionTime the time taken for survivor selection in each generation.
 * @property offspringSelectionTime the time taken for offspring selection in each generation.
 * @property alterTime the time taken for the alteration phase in each generation.
 * @property evolutionTime the total time taken for the evolution so far.
 * @property generationTimes the time taken for each generation so far.
 * @property bestFitness the best fitness value in each generation.
 * @property worstFitness the worst fitness value in each generation.
 * @property averageFitness the average fitness value in each generation.
 * @property fittest the fittest phenotype in the current generation.
 * @property steadyGenerations the number of steady generations so far.
 * @property generation the current generation.
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
    val generationTimes: MutableList<Long>
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
    fun onEvolutionEnded() {
        // Do nothing
    }

    /**
     * Called 
     */
    fun onGenerationShift(prop: KProperty<*>, old: Int, new: Int) {
        // Do nothing
    }

    fun onGenerationStarted(generation: Int) {
        // Do nothing
    }

    fun onGenerationFinished() {
        // Do nothing
    }
}
