/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.Representation

data class Genotype<T, G>(val chromosomes: List<Chromosome<T, G>>) : Representation<T, G>,
    Collection<Chromosome<T, G>> where G : Gene<T, G> {

    constructor(vararg chromosomes: Chromosome<T, G>) : this(chromosomes.toList())

    override fun flatten() = chromosomes.flatMap { it.flatten() }

    override val size = chromosomes.size
    override fun isEmpty() = chromosomes.isEmpty()

    override fun iterator() = chromosomes.iterator()

    override fun containsAll(elements: Collection<Chromosome<T, G>>) = chromosomes.containsAll(elements)

    override fun contains(element: Chromosome<T, G>) = chromosomes.contains(element)

    override fun verify() = chromosomes.all { it.verify() }

    operator fun get(index: Int): Chromosome<T, G> {
        constraints {
            "The index ($index) must be in the range [0, $size)"(::InvalidIndexException) {
                index in this@Genotype.indices
            }
        }
        return chromosomes[index]
    }

    override fun toString() = when (Domain.toStringMode) {
        ToStringMode.SIMPLE -> chromosomes.joinToString(separator = ", ", prefix = "[", postfix = "]")
        else -> "Genotype(chromosomes=$chromosomes)"
    }
}
