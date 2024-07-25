/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.FitnessRanker
import cl.ravenhill.keen.utils.isNotNaN


/**
 * A comprehensive listener interface for receiving notifications about various phases in the evolutionary computation
 * process, including initialization, evaluation, parent selection, survivor selection, alteration, and evolution
 * itself.
 *
 * ## Usage:
 * Implement this interface to create a custom listener that handles events occurring at different phases of the
 * evolutionary computation process.
 *
 * ### Example 1: Custom Evolution Listener
 * ```
 * class MyEvolutionListener : EvolutionListener<Int, MyGene> {
 *     override var ranker: IndividualRanker<Int, MyGene> = MyRanker()
 *     override var evolution: EvolutionRecord<Int, MyGene> = MyEvolutionRecord()
 *     override var timeSource: TimeSource = MyTimeSource()
 *
 *     override fun onEvolutionStarted(state: EvolutionState<Int, MyGene>) =
 *         println("Evolution started.")
 *
 *     override fun onEvolutionEnded(state: EvolutionState<Int, MyGene>) =
 *         println("Evolution ended.")
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface EvolutionListener<T, G> :
        GenerationListener<T, G>,
        InitializationListener<T, G>,
        EvaluationListener<T, G>,
        ParentSelectionListener<T, G>,
        SurvivorSelectionListener<T, G>,
        AlterationListener<T, G>
        where G : Gene<T, G> {

    /**
     * Called when the evolution phase starts.
     *
     * @param state the current state of the evolution process
     */
    fun onEvolutionStarted(state: GeneticEvolutionState<T, G>) = Unit

    /**
     * Called when the evolution phase ends.
     *
     * @param state the current state of the evolution process
     */
    fun onEvolutionEnded(state: GeneticEvolutionState<T, G>) = Unit

    /**
     * Displays the listener's information.
     */
    fun display() = println(toString())

    /**
     * Combines this listener with another listener.
     *
     * @param other the other listener to combine with
     * @return a list containing both listeners
     */
    operator fun plus(other: EvolutionListener<T, G>): List<EvolutionListener<T, G>> = listOf(this, other)

    companion object {
        /**
         * Computes the number of steady generations, where the fitness of the fittest individual remains the same
         * across consecutive generations.
         *
         * @param ranker the ranker used for ranking individuals
         * @param evolution the record of the evolution process
         * @return the number of steady generations
         */
        fun <T, G> computeSteadyGenerations(
            ranker: FitnessRanker<T, G>,
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
