package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A listener interface for receiving notifications about the start and end of generations in the evolutionary
 * computation process.
 *
 * ## Usage:
 * Implement this interface to create a custom listener for handling events that occur at the beginning and end of each
 * generation.
 *
 * ### Example 1: Custom Generation Listener
 * ```
 * class MyGenerationListener : GenerationListener<Int, MyGene> {
 *     override fun onGenerationStarted(state: EvolutionState<Int, MyGene>) =
 *         println("Generation started: ${state.generation}")
 *
 *     override fun onGenerationEnded(state: EvolutionState<Int, MyGene>) =
 *         println("Generation ended: ${state.generation}")
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface GenerationListener<T, G> where G : Gene<T, G> {

    /**
     * Called when a generation starts.
     *
     * @param state the current state of the evolution process
     */
    fun onGenerationStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when a generation ends.
     *
     * @param state the current state of the evolution process
     */
    fun onGenerationEnded(state: EvolutionState<T, G>) = Unit
}
