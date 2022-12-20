package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.prog.Reduceable

/**
 * A [Gene] that represents a program tree.
 *
 * @param T The type of the value.
 */
class ProgramGene<T>(override val dna: Reduceable<T>, private val primitives: List<Reduceable<T>>) :
        Gene<Reduceable<T>> {

    /**
     * Reduces the program tree to a single value.
     */
    fun reduce(): T = dna.reduce()
    override fun generator(): Reduceable<T> {
        TODO("Not yet implemented")
    }

    override fun duplicate(dna: Reduceable<T>): Gene<Reduceable<T>> {
        TODO("Not yet implemented")
    }
}
