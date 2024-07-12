package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.PhaseListener

/**
 * A listener interface for receiving notifications about the start and end of the survivor selection phase in the
 * evolutionary computation process.
 *
 * ## Usage:
 * Implement this interface to create a custom listener for handling events that occur at the beginning and end of the
 * survivor selection phase.
 *
 * ### Example 1: Custom Survivor Selection Listener
 * ```
 * class MySurvivorSelectionListener : SurvivorSelectionListener<Int, MyGene> {
 *     override fun onSurvivorSelectionStarted(state: EvolutionState<Int, MyGene>) {
 *         println("Survivor selection started.")
 *     }
 *
 *     override fun onSurvivorSelectionEnded(state: EvolutionState<Int, MyGene>) {
 *         println("Survivor selection ended.")
 *     }
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface SurvivorSelectionListener<T, G> : PhaseListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the survivor selection phase starts.
     *
     * @param state the current state of the evolution process
     */
    fun onSurvivorSelectionStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the survivor selection phase ends.
     *
     * @param state the current state of the evolution process
     */
    fun onSurvivorSelectionEnded(state: EvolutionState<T, G>) = Unit
}
