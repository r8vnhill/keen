/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.genes.Gene
import java.io.Serializable


/**
 * The `Evolver` interface defines the contract for an object that can evolve a population of
 * genetic material of type `DNA` from an initial state to a final state.
 *
 * @param DNA the type of genetic material that will be evolved
 * @param G the type of [Gene] that will be used to evolve the genetic material
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Evolver<DNA, G: Gene<DNA, G>> {
    /**
     * The entry point of the evolution process.
     *
     * @return an [EvolutionResult] containing the last generation of the evolution process.
     */
    fun evolve(): EvolutionResult<DNA, G>
}
