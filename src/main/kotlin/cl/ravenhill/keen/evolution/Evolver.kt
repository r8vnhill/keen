package cl.ravenhill.keen.evolution


/**
 * The `Evolver` interface defines the contract for an object that can evolve a population of
 * genetic material of type `DNA` from an initial state to a final state.
 *
 * @param DNA the type of genetic material that will be evolved
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Evolver<DNA> {

    /**
     * Evolves the population of genetic material from an initial state to a final state, using the
     * provided `start` configuration.
     *
     * @param start the configuration for the evolution process
     * @return the result of the evolution process
     */
    fun evolve(start: EvolutionStart<DNA>): EvolutionResult<DNA>
}
