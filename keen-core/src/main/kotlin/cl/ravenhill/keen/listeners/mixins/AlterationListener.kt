package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.genetic.genes.Gene

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
 *     override fun onAlterationStarted() =
 *         println("Alteration started.")
 *
 *     override fun onAlterationEnded() =
 *         println("Alteration ended.")
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface AlterationListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the alteration phase starts.
     */
    fun onAlterationStarted() = Unit

    /**
     * Called when the alteration phase ends.
     */
    fun onAlterationEnded() = Unit
}
