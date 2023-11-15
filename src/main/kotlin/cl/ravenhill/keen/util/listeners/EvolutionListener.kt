/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.isNotNan
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
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
 * @property population Current set of candidate solutions.
 * @property optimizer Optimizer utilized during evolution.
 * @property fittest Most adapted individual in the current generation.
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
    var optimizer: IndividualOptimizer<DNA, G>
    var generation: Int
    val currentGeneration: GenerationRecord<DNA, G>
    var evolution: EvolutionRecord<DNA, G>

    @ExperimentalTime
    var timeSource: TimeSource

    /**
     * Called when the evolution process has ended (e.g., when the termination criteria are met).
     */
    fun onEvolutionEnded() = Unit

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
        @Deprecated(
            "Use computeSteadyGenerations(optimizer, evolution) instead.",
            ReplaceWith("computeSteadyGenerations(optimizer, evolution)")
        )
        fun <DNA, G> computeSteadyGenerations(
            lastGeneration: GenerationRecord<DNA, G>,
            currentGeneration: GenerationRecord<DNA, G>,
        ) where G : Gene<DNA, G> = lastGeneration.population.resulting.let { previous ->
            if (previous.first().fitness == currentGeneration.population.resulting.first().fitness) {
                lastGeneration.steady + 1
            } else {
                0
            }
        }

        fun <T, G> computeSteadyGenerations(
            optimizer: IndividualOptimizer<T, G>,
            evolution: EvolutionRecord<T, G>
        ): Int where G : Gene<T, G> {
            var steady = 0
            for (i in evolution.generations.size - 1 downTo 1) {
                val last = evolution.generations[i - 1]
                val current = evolution.generations[i]
                val lastFittest = last.population.resulting
                    .filter { it.fitness.isNotNan() }
                    .maxOfWith(
                        comparator = optimizer.comparator,
                        selector = { it.toIndividual() }
                    )
                val currentFittest = current.population.resulting
                    .filter { it.fitness.isNotNan() }
                    .maxOfWith(
                        comparator = optimizer.comparator,
                        selector = { it.toIndividual() }
                    )
                if (lastFittest.fitness == currentFittest.fitness) {
                    steady++
                } else {
                    break
                }
            }
            return steady
        }

        /**
         * Computes the population's individual records based on the optimizer and the given population.
         *
         * This method sorts the population based on the optimizer and returns a list of individual records.
         *
         * @param optimizer The individual optimizer used for sorting.
         * @param population The current population of candidate solutions.
         * @return A list of individual records after sorting the population.
         */
        fun <DNA, G : Gene<DNA, G>> computePopulation(
            optimizer: IndividualOptimizer<DNA, G>,
            population: Population<DNA, G>,
        ): List<IndividualRecord<DNA, G>> {
            val sorted = optimizer.sort(population)
            return List(sorted.size) {
                IndividualRecord(sorted[it].genotype, sorted[it].fitness)
            }
        }
    }
}
