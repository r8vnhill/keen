package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.PhaseListener

/**
 * A listener interface for receiving notifications about the start and end of the initialization phase in the
 * evolutionary computation process.
 *
 * ## Usage:
 * Implement this interface to create a custom listener for handling events that occur at the beginning and end of the
 * initialization phase.
 *
 * ### Example 1: Custom Initialization Listener
 * ```
 * class MyInitializationListener : InitializationListener<Int, MyGene> {
 *     override fun onInitializationStarted(state: EvolutionState<Int, MyGene>) {
 *         println("Initialization started.")
 *     }
 *
 *     override fun onInitializationEnded(state: EvolutionState<Int, MyGene>) {
 *         println("Initialization ended.")
 *     }
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface InitializationListener<T, G> : PhaseListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the initialization phase starts.
     *
     * @param state the current state of the evolution process
     */
    fun onInitializationStarted(state: EvolutionState<T, G>) = Unit

    /**
     * Called when the initialization phase ends.
     *
     * @param state the current state of the evolution process
     */
    fun onInitializationEnded(state: EvolutionState<T, G>) = Unit
}
