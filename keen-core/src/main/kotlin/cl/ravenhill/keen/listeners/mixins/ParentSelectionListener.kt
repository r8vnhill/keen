package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.genetic.genes.Gene

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
 *     override fun onParentSelectionStarted() =
 *         println("Parent selection started.")
 *
 *     override fun onParentSelectionEnded() =
 *         println("Parent selection ended.")
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface ParentSelectionListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the parent selection phase starts.
     */
    fun onParentSelectionStarted() = Unit

    /**
     * Called when the parent selection phase ends.
     */
    fun onParentSelectionEnded() = Unit
}
