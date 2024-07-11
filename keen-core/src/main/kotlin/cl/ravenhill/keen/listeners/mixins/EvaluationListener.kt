package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A listener interface for receiving notifications about the start and end of the evaluation phase in the evolutionary
 * computation process.
 *
 * ## Usage:
 * Implement this interface to create a custom listener for handling events that occur at the beginning and end of the
 * evaluation phase.
 *
 * ### Example 1: Custom Evaluation Listener
 * ```
 * class MyEvaluationListener : EvaluationListener<Int, MyGene> {
 *     override fun onEvaluationStarted() {
 *         println("Evaluation started.")
 *     }
 *
 *     override fun onEvaluationEnded() {
 *         println("Evaluation ended.")
 *     }
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
interface EvaluationListener<T, G> where G : Gene<T, G> {

    /**
     * Called when the evaluation phase starts.
     */
    fun onEvaluationStarted() = Unit

    /**
     * Called when the evaluation phase ends.
     */
    fun onEvaluationEnded() = Unit
}
