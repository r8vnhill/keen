package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.PhaseListener

/**
 * A listener interface for receiving notifications about the start and end of the parent selection phase in the
 * evolutionary computation process.
 *
 * ## Usage:
 * Implement this interface to create a custom listener for handling events that occur at the beginning and end of the
 * parent selection phase.
 *
 * ### Example 1: Custom Parent Selection Listener
 * ```
 * class MyParentSelectionListener : ParentSelectionListener<Int, MyGene> {
 *     override fun onParentSelectionStarted(state: EvolutionState<Int, MyGene>) {
 *         println("Parent selection started.")
 *     }
 *
 *     override fun onParentSelectionEnded(state: EvolutionState<Int, MyGene>) {
 *         println("Parent selection ended.")
 *     }
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface ParentSelectionListener<T, G, U> : PhaseListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the parent selection phase starts.
     *
     * @param state the current state of the evolution process
     */
    fun onParentSelectionStarted(state: GeneticEvolutionState<T, G>) = Unit

    /**
     * Called when the parent selection phase ends.
     *
     * @param state the current state of the evolution process
     */
    fun onParentSelectionEnded(state: GeneticEvolutionState<T, G>) = Unit
}
