/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.Verifiable

/**
 * Any unit of genetic material used by the [cl.ravenhill.keen.evolution.Evolver].
 *
 * @param DNA  The type of the genetic material's value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface GeneticMaterial<DNA, G : Gene<DNA, G>> : Verifiable {

    /**
     * Flattens the genetic material into a list of values.
     */
    fun flatMap(transform: (DNA) -> DNA = { it }): List<DNA>

    /**
     * Returns a string representation of the genetic material.
     */
    fun toSimpleString() = toString()
}
