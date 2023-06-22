package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * The EvolutionInterceptor class allows for customization of the evolutionary process by enabling
 * updates to the [EvolutionState] object before the evolution starts, and updates to the
 * [EvolutionResult] object after the evolution ends.
 *
 * @param DNA The type of the DNA.
 *
 * @property before The function to be executed before the evolution starts.
 * @property after The function to be executed after the evolution ends.
 *
 * @constructor Creates a new [EvolutionInterceptor] with the given [before] and [after] functions.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionInterceptor<DNA, G : Gene<DNA, G>>(
    val before: (EvolutionState<DNA, G>) -> EvolutionState<DNA, G>,
    val after: (EvolutionResult<DNA, G>) -> EvolutionResult<DNA, G>
) {
    companion object {
        /**
         * Returns an [EvolutionInterceptor] that does nothing.
         */
        fun <DNA, G : Gene<DNA, G>> identity() = EvolutionInterceptor(
            { before: EvolutionState<DNA, G> -> before }
        ) { end: EvolutionResult<DNA, G> -> end }

        fun <DNA, G : Gene<DNA, G>> after(
            function: (EvolutionResult<DNA, G>) -> EvolutionResult<DNA, G>
        ): EvolutionInterceptor<DNA, G> {
            return EvolutionInterceptor(
                { before: EvolutionState<DNA, G> -> before }
            ) { end: EvolutionResult<DNA, G> -> function(end) }
        }
    }
}
