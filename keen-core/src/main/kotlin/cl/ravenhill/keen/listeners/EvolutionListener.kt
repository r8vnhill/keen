/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.utils.isNotNaN
import kotlin.time.TimeSource


/**
 * An interface for monitoring and responding to key events in the evolution process of a genetic algorithm.
 *
 * Implementations of this interface can track and react to various stages of the evolutionary process, including
 * the start and end of generations, evaluation phases, and selection processes. This monitoring capability is
 * crucial for logging, debugging, adapting strategies, or implementing custom behaviors at specific points
 * in the evolution cycle.
 *
 *
 * ## Callback Methods:
 * Each method corresponds to a specific event in the evolutionary process:
 * - `onGenerationStarted` and `onGenerationEnded`: Triggered at the start and end of each generation.
 * - `onInitializationStarted` and `onInitializationEnded`: Triggered at the start and end of the initialization phase.
 * - `onEvaluationStarted` and `onEvaluationEnded`: Triggered at the start and end of the evaluation phase.
 * - `onParentSelectionStarted` and `onParentSelectionEnded`: Triggered at the start and end of the offspring
 *   selection phase.
 * - `onSurvivorSelectionStarted` and `onSurvivorSelectionEnded`: Triggered at the start and end of the survivor
 *   selection phase.
 * - `onAlterationStarted` and `onAlterationEnded`: Triggered at the start and end of the alteration phase.
 * - `onEvolutionStarted` and `onEvolutionEnded`: Triggered at the start and end of the entire evolution process.
 *
 * Implementors can override these methods to execute custom logic or operations at each stage, enhancing the
 * flexibility and control over the evolutionary process.
 *
 * @param T The type of data used in the evolution process, typically representing the genetic information.
 * @param G The type of gene used in the evolution process, must be a subtype of [Gene].
 *
 * @property ranker An [IndividualRanker] used to rank individuals based on their fitness or other criteria.
 * @property evolution An [EvolutionRecord] instance used to record and store detailed information about the
 *                     evolution process, such as generation timelines and individual records.
 * @property timeSource A [TimeSource] used to measure the duration of different stages of the evolution process,
 *                      providing insights into performance and efficiency.
 *
 * @see EvolutionState for details about the state of the evolution process.
 * @see IndividualRanker for details on how individuals are ranked.
 * @see EvolutionRecord for details on recording evolution data.
 * @see TimeSource for details on timing various phases of the evolution process.
 */
interface EvolutionListener<T, G> where G : Gene<T, G> {
    var ranker: IndividualRanker<T, G>
    var evolution: EvolutionRecord<T, G>
    var timeSource: TimeSource

    /**
     * Called when the generation of the evolutionary algorithm has started.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    fun onGenerationStarted(state: EvolutionState<T, G>) = Unit


    /**
     * Handles the event when the generation has ended.
     *
     * @param state The current evolution state.
     */
    fun onGenerationEnded(state: EvolutionState<T, G>) = Unit


    /**
     * Called when the initialization process has started.
     *
     * @param state The current state of the evolution process.
     */
    fun onInitializationStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the initialization process has ended.
     *
     * @param state The current state of the evolution process.
     */
    fun onInitializationEnded(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the evaluation process has started.
     *
     * @param state The current state of the evolution process.
     */
    fun onEvaluationStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the evaluation process has ended.
     *
     * @param state The current state of the evolution process.
     */
    fun onEvaluationEnded(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the offspring selection process has started.
     *
     * @param state The current state of the evolution process.
     */
    fun onParentSelectionStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the offspring selection process has ended.
     *
     * @param state The current state of the evolution process.
     */
    fun onParentSelectionEnded(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the survivor selection process has started.
     *
     * @param state The current state of the evolution process.
     */
    fun onSurvivorSelectionStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the survivor selection process has ended.
     *
     * @param state The current state of the evolution process.
     */
    fun onSurvivorSelectionEnded(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the alteration process has started.
     *
     * @param state The current state of the evolution process.
     */
    fun onAlterationStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the alteration process has ended.
     *
     * @param state The current state of the evolution process.
     */
    fun onAlterationEnded(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the evolution process has started.
     *
     * @param state The current state of the evolution process.
     */
    fun onEvolutionStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the evolution process has ended.
     *
     * @param state The current state of the evolution process.
     */
    fun onEvolutionEnded(state: EvolutionState<T, G>) = Unit

    /**
     * Prints the string representation of the current object to the standard output.
     */
    fun display() = println(toString())

    /**
     * Concatenates this [EvolutionListener] with another [EvolutionListener].
     * Returns a new [List] containing both listeners.
     *
     * @param other The other [EvolutionListener] to concatenate with.
     * @return A new [List] containing both listeners.
     */
    operator fun plus(other: EvolutionListener<T, G>): List<EvolutionListener<T, G>> = listOf(this, other)

    companion object {

        /**
         * Calculates the number of consecutive generations where the fittest individual has not changed.
         *
         * This function assesses the stability or stagnation in the evolutionary process by counting the number of
         * consecutive generations where the fittest individual remains unchanged. A higher number indicates a longer
         * period of steady state, which might suggest convergence or a lack of diversity in the population.
         *
         * ## Functionality:
         * - **Fittest Individual Identification**: For each generation, identifies the fittest individual based on the
         *   specified [ranker]. The fittest individual is determined from the offspring population of each generation.
         * - **Steady State Calculation**: Compares the fittest individuals between consecutive generations to determine
         *   if there has been a change. Increments a count as long as the fittest individual remains the same.
         * - **Break on Change**: Terminates the count if a generation is found where the fittest individual differs
         *   from the previous generation, indicating a change in the evolutionary landscape.
         *
         * ## Usage:
         * This function can be used as a criterion for termination or for monitoring the progress of the evolutionary
         * algorithm. It helps in understanding if the algorithm is making progress or if it has potentially converged.
         *
         * ### Example:
         * ```kotlin
         * val ranker: IndividualRanker<MyDataType, MyGene> = /* Initialization */
         * val evolutionRecord: EvolutionRecord<MyDataType, MyGene> = /* Initialization */
         *
         * val steadyGenerations = computeSteadyGenerations(ranker, evolutionRecord)
         * ```
         * In this example, `steadyGenerations` represents the number of generations for which the fittest individual
         * has remained constant, potentially indicating a steady state in the evolutionary process.
         *
         * @param ranker The [IndividualRanker] used to determine the fittest individual in each generation.
         * @param evolution The [EvolutionRecord] containing the history of generations for analysis.
         * @return The number of consecutive generations where the fittest individual has remained unchanged.
         */
        fun <T, G> computeSteadyGenerations(
            ranker: IndividualRanker<T, G>,
            evolution: EvolutionRecord<T, G>,
        ): Int where G : Gene<T, G> {
            var steady = 0
            for (i in evolution.generations.size - 1 downTo 1) {
                val last = evolution.generations[i - 1]
                val current = evolution.generations[i]
                val lastFittest = last.population.offspring
                    .filter { it.fitness.isNotNaN() }
                    .maxOfWith(ranker.comparator) { it.toIndividual() }
                val currentFittest = current.population.offspring
                    .filter { it.fitness.isNotNaN() }
                    .maxOfWith(ranker.comparator) { it.toIndividual() }
                if (lastFittest.fitness == currentFittest.fitness) {
                    steady++
                } else {
                    break
                }
            }
            return steady
        }
    }
}
