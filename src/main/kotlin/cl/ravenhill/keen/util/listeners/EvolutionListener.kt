package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.PhenotypeRecord
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlin.reflect.KProperty
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource


/**
 * Represents a list of [EvolutionListener] instances.
 */
typealias Listeners<DNA, G> = List<EvolutionListener<DNA, G>>

/**
 * An interface for monitoring and responding to the various stages of the
 * genetic algorithm's evolution process.
 *
 * It offers hooks at different phases of the evolution, enabling
 * enhanced monitoring, logging, or custom behaviors. It's an essential tool for
 * gaining deep insights into the evolution's progress and metrics.
 *
 * Classes implementing this interface can use these hooks for a variety of purposes
 * such as metrics tracking, progress reporting, or behavior modifications.
 *
 * @property evolutionResult Result after each evolution generation.
 * @property population Current set of candidate solutions.
 * @property optimizer Optimizer utilized during evolution.
 * @property fittest Most adapted phenotype in the current generation.
 * @property steadyGenerations Count of generations without improvements.
 * @property generation Current generation number.
 * @property currentGeneration Details of the current generation.
 * @property timeSource Time measurement source.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface EvolutionListener<DNA, G : Gene<DNA, G>> {
    var evolutionResult: EvolutionResult<DNA, G>
    var population: Population<DNA, G>
    var optimizer: PhenotypeOptimizer<DNA, G>
    val fittest: Individual<DNA, G>?
    var steadyGenerations: Int
    var generation: Int
    val currentGeneration: GenerationRecord

    @ExperimentalTime
    var timeSource: TimeSource

    /**
     * Called whenever the result of the evolution is updated.
     */
    fun onResultUpdated() = Unit

    /**
     * Called when the evolution process has ended (e.g., when the termination criteria are met).
     */
    fun onEvolutionEnded() = Unit

    /**
     * Called when the generation value has changed.
     * @param prop The property that has changed.
     * @param old The old generation value.
     * @param new The new generation value.
     */
    fun onGenerationShift(prop: KProperty<*>, old: Int, new: Int) = Unit

    /**
     * Called when a new generation has started.
     * @param generation The generation number.
     */
    fun onGenerationStarted(generation: Int, population: Population<DNA, G>) = Unit

    /**
     * Called when the current generation has finished.
     */
    fun onGenerationFinished(population: Population<DNA, G>) = Unit

    /**
     * Called when the initialization phase has started.
     */
    fun onInitializationStarted() = Unit

    /**
     * Called when the initialization phase has finished.
     */
    fun onInitializationFinished() = Unit

    /**
     * Called when the evaluation phase has started.
     */
    fun onEvaluationStarted() = Unit

    /**
     * Called when the evaluation phase has finished.
     */
    fun onEvaluationFinished() = Unit

    /**
     * Called when the offspring selection phase has started.
     */
    fun onOffspringSelectionStarted() = Unit

    /**
     * Called when the offspring selection phase has finished.
     */
    fun onOffspringSelectionFinished() = Unit

    /**
     * Called when the survivor selection phase has started.
     */
    fun onSurvivorSelectionStarted() = Unit

    /**
     * Called when the survivor selection phase has finished.
     */
    fun onSurvivorSelectionFinished() = Unit

    /**
     * Called when the alteration phase begins.
     *
     * Alteration typically involves operations like mutation and crossover. This hook can be
     * used to track or modify any activities at the onset of the alteration process.
     */
    fun onAlterationStarted() = Unit

    /**
     * Called when the alteration phase has concluded.
     *
     * This hook can be used to capture any metrics or insights about the alterations performed
     * during the generation, such as mutation rates or successful crossovers.
     */
    fun onAlterationFinished() = Unit

    /**
     * Called at the commencement of the entire evolution process.
     *
     * Useful for any setup or initial logging operations that need to be performed right
     * before the evolution begins.
     */
    fun onEvolutionStart() = Unit

    /**
     * Called at the end of the entire evolution process.
     *
     * Suitable for final logging, cleanup, or any post-processing activities required after
     * the evolution concludes.
     */
    fun onEvolutionFinished() = Unit

    companion object {

        /**
         * Computes the number of steady generations based on the last and current generation.
         *
         * @param lastGeneration The previous generation record.
         * @param currentGeneration The current generation record.
         * @return Returns incremented steady count if the fitness of the fittest individual remains the same,
         * otherwise, returns 0.
         */
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

        /**
         * Computes the population's phenotype records based on the optimizer and the given population.
         *
         * This method sorts the population based on the optimizer and returns a list of phenotype records.
         *
         * @param optimizer The phenotype optimizer used for sorting.
         * @param population The current population of candidate solutions.
         * @return A list of phenotype records after sorting the population.
         */
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

    var evolution: EvolutionRecord<DNA, G>
}

