/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.genes.Gene


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
interface Evolver<DNA, G: Gene<DNA, G>> {

    /**
     * Evolves the population of genetic material from an initial state to a final state, using the
     * provided `start` configuration.
     *
     * @param start the configuration for the evolution process
     * @return the result of the evolution process
     */
    fun evolve(start: EvolutionStart<DNA, G>): EvolutionResult<DNA, G>
}
