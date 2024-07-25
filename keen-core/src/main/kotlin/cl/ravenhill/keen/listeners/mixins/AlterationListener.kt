package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.PhaseListener

/**
 * A listener interface for receiving notifications about the start and end of the alteration phase in the
 * evolutionary computation process.
 *
 * ## Usage:
 * Implement this interface to create a custom listener for handling events that occur at the beginning and end of the
 * alteration phase.
 *
 * ### Example 1: Custom Alteration Listener
 * ```
 * class MyAlterationListener : AlterationListener<Int, MyGene> {
 *     override fun onAlterationStarted(state: EvolutionState<Int, MyGene>) {
 *         println("Alteration started.")
 *     }
 *
 *     override fun onAlterationEnded(state: EvolutionState<Int, MyGene>) {
 *         println("Alteration ended.")
 *     }
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface AlterationListener<T, G> : PhaseListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the alteration phase starts.
     *
     * @param state the current state of the evolution process
     */
    fun onAlterationStarted(state: GeneticEvolutionState<T, G>) = Unit

    /**
     * Called when the alteration phase ends.
     *
     * @param state the current state of the evolution process
     */
    fun onAlterationEnded(state: GeneticEvolutionState<T, G>) = Unit
}
