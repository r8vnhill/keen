package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.genetic.genes.Gene
import java.util.*


class CharChromosome(
    override val genes: List<Gene<Char>>,
    private val filter: (Char) -> Boolean = { true }
) : Chromosome<Char> {

    override fun duplicate(genes: List<Gene<Char>>) = CharChromosome(genes)

    constructor(
        size: Int,
        filter: (Char) -> Boolean = { true },
        range: CharRange
    ) : this(List(size) { CharGene.create(range, filter) })

    override fun verify() = genes.all { filter(it.dna) }

    override fun toString(): String {
        return genes.joinToString("")
    }

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is CharChromosome -> false
        other::class != this::class -> false
        else -> genes == other.genes
    }

    override fun hashCode() = Objects.hash(CharChromosome::class, genes)

    class Factory : Chromosome.Factory<Char> {
        var size = 0
        var range = ' '..'z'
        var filter: (Char) -> Boolean = { true }
        override fun make() = CharChromosome(size, filter, range)

        override fun toString() = "CharChromosome.Builder { size: $size }"
    }
}
