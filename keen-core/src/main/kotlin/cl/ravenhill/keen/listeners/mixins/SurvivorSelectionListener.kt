package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.genetic.genes.Gene

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
 *     override fun onSurvivorSelectionStarted() =
 *         println("Survivor selection started.")
 *
 *     override fun onSurvivorSelectionEnded() =
 *         println("Survivor selection ended.")
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface SurvivorSelectionListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the survivor selection phase starts.
     */
    fun onSurvivorSelectionStarted() = Unit

    /**
     * Called when the survivor selection phase ends.
     */
    fun onSurvivorSelectionEnded() = Unit
}
