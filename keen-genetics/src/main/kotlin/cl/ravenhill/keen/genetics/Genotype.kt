/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics

import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.Representation

data class Genotype<T, G>(val chromosomes: List<Chromosome<T, G>>) : Representation<T, G> where G : Gene<T, G> {
    override fun flatten(): List<T> {
        TODO("Not yet implemented")
    }

    override val size: Int
        get() = TODO("Not yet implemented")
}
