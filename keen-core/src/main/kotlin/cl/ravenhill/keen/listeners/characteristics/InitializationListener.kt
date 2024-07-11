package cl.ravenhill.keen.listeners.characteristics

import cl.ravenhill.keen.genetic.genes.Gene

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
 *     override fun onInitializationStarted() {
 *         println("Initialization started.")
 *     }
 *
 *     override fun onInitializationEnded() {
 *         println("Initialization ended.")
 *     }
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface InitializationListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the initialization phase starts.
     */
    fun onInitializationStarted() = Unit

    /**
     * Called when the initialization phase ends.
     */
    fun onInitializationEnded() = Unit
}
