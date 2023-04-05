package cl.ravenhill.keen.evolution


/**
 * The EvolutionInterceptor class allows for customization of the evolutionary process by enabling
 * updates to the [EvolutionStart] object before the evolution starts, and updates to the
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
class EvolutionInterceptor<DNA>(
    val before: (EvolutionStart<DNA>) -> EvolutionStart<DNA>,
    val after: (EvolutionResult<DNA>) -> EvolutionResult<DNA>
) {
    companion object {
        /**
         * Returns an [EvolutionInterceptor] that does nothing.
         */
        fun <DNA> identity() = EvolutionInterceptor(
            { before: EvolutionStart<DNA> -> before }
        ) { end: EvolutionResult<DNA> -> end }
    }
}
