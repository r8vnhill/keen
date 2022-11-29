package cl.ravenhill.keen.evolution


/**
 * The evolution interceptor allows to update the [EvolutionStart] object, __*before*__ the
 * evolution start, and update the [EvolutionResult] object, __*after*__ the evolution ends.
 *
 * @param DNA The type of the DNA
 *
 * @property before The function to be executed before the evolution starts
 * @property after The function to be executed after the evolution ends
 *
 * @constructor Creates a new [EvolutionInterceptor] with the given [before] and [after] functions
 */
class EvolutionInterceptor<DNA>(
    val before: (EvolutionStart<DNA>) -> EvolutionStart<DNA>,
    val after: (EvolutionResult<DNA>) -> EvolutionResult<DNA>
) {
    companion object {
        /**
         * [EvolutionInterceptor] that does nothing
         */
        fun <DNA> identity() = EvolutionInterceptor(
            { before: EvolutionStart<DNA> -> before }
        ) { end: EvolutionResult<DNA> -> end }
    }
}
