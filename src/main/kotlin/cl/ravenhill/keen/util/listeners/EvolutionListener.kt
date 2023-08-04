package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.PhenotypeRecord
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlin.reflect.KProperty
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Type alias for a list of [EvolutionListener] instances.
 */
typealias Listeners<DNA, G> = List<EvolutionListener<DNA, G>>

/**
 * Interface for a listener that monitors and responds to different stages of the evolution process
 * in a genetic algorithm.
 *
 * The [EvolutionListener] interface is designed to provide hooks for various phases and events in
 * the genetic evolution process, enabling monitoring, logging, or custom handling of different
 * aspects of the algorithm. These hooks allow for detailed insights into the evolution's progress
 * and performance metrics.
 *
 * Implementing classes can use these hooks to track metrics, report progress, or modify behavior at
 * different stages.
 *
 * @property evolutionResult The result of the evolution after each generation.
 * @property population The current population of candidate solutions.
 * @property optimizer The phenotype optimizer used in the evolution.
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
    var evolutionResult: EvolutionResult<DNA, G>
    var population: Population<DNA, G>
    var optimizer: PhenotypeOptimizer<DNA, G>
    val fittest: Phenotype<DNA, G>?
    var steadyGenerations: Int
    var generation: Int
    val currentGeneration: GenerationRecord

    @ExperimentalTime
    var timeSource: TimeSource

    /**
     * Called whenever the result of the evolution is updated.
     */
    fun onResultUpdated() {
        // Intentionally left blank
    }

    /**
     * Called when the evolution process has ended (e.g., when the termination criteria are met).
     */
    fun onEvolutionEnded() {
        // Intentionally left blank
    }

    /**
     * Called when the generation value has changed.
     * @param prop The property that has changed.
     * @param old The old generation value.
     * @param new The new generation value.
     */
    fun onGenerationShift(prop: KProperty<*>, old: Int, new: Int) {
        // Intentionally left blank
    }

    /**
     * Called when a new generation has started.
     * @param generation The generation number.
     */
    fun onGenerationStarted(generation: Int, population: Population<DNA, G>) {
        // Intentionally left blank
    }

    /**
     * Called when the current generation has finished.
     */
    fun onGenerationFinished(population: Population<DNA, G>) {
        // Intentionally left blank
    }

    /**
     * Called when the initialization phase has started.
     */
    fun onInitializationStarted() {
        // Intentionally left blank
    }

    /**
     * Called when the initialization phase has finished.
     */
    fun onInitializationFinished() {
        // Intentionally left blank
    }

    /**
     * Called when the evaluation phase has started.
     */
    fun onEvaluationStarted() {
        // Intentionally left blank
    }

    /**
     * Called when the evaluation phase has finished.
     */
    fun onEvaluationFinished() {
        // Intentionally left blank
    }

    /**
     * Called when the offspring selection phase has started.
     */
    fun onOffspringSelectionStarted() {
        // Intentionally left blank
    }

    /**
     * Called when the offspring selection phase has finished.
     */
    fun onOffspringSelectionFinished() {
        // Intentionally left blank
    }

    /**
     * Called when the survivor selection phase has started.
     */
    fun onSurvivorSelectionStarted() {
        // Intentionally left blank
    }

    /**
     * Called when the survivor selection phase has finished.
     */
    fun onSurvivorSelectionFinished() {
        // Intentionally left blank
    }

    fun onAlterationStarted() {
        // Intentionally left blank
    }

    fun onAlterationFinished() {
        // Intentionally left blank
    }

    fun onEvolutionStart() {
        // Intentionally left blank
    }

    fun onEvolutionFinished() {
        // Intentionally left blank
    }

    companion object {
        fun computeSteadyGenerations(
            lastGeneration: GenerationRecord,
            currentGeneration: GenerationRecord,
        ) = lastGeneration.population.resulting.let { previous ->
            if (previous.first().fitness == currentGeneration.population.resulting.first().fitness) {
                lastGeneration.steady + 1
            } else {
                0
            }
        }

        fun <DNA, G : Gene<DNA, G>> computePopulation(
            optimizer: PhenotypeOptimizer<DNA, G>,
            population: Population<DNA, G>,
        ): List<PhenotypeRecord> {
            val sorted = optimizer.sort(population)
            return List(sorted.size) {
                PhenotypeRecord("${sorted[it].genotype}", sorted[it].fitness)
            }
        }
    }
}
