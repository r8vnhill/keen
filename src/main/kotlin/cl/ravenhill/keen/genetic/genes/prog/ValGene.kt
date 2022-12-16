package cl.ravenhill.keen.genetic.genes.prog

import cl.ravenhill.keen.genetic.genes.Gene
import java.util.*

/**
 * A gene that represents a value in a program.
 *
 * @param T
 * @property dna Gene<T>
 * @constructor
 */
class ValGene<T>(override val dna: Gene<T>) : Gene<Gene<T>> {
    override fun generator() = dna.mutate()

    override fun duplicate(dna: Gene<T>) = ValGene(dna)

    override fun toString() = dna.toString()

    override fun verify() = dna.verify()

    fun reduce() = dna.dna

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is ValGene<*> -> false
        other::class != this::class -> false
        else -> dna == other.dna
    }

    override fun hashCode() = Objects.hash(this::class, dna)
}