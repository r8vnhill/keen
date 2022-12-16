package cl.ravenhill.keen.genetic.genes.prog

import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A [Gene] that represents a program tree.
 *
 * @param T The type of the value.
 */
interface ProgramGene<T> : Gene<Program<T>> {

    /**
     * Reduces the program tree to a single value.
     */
    fun reduce(): T = dna.reduce()
}

class Program<T>(private val root: ProgramGene<T>) {
    fun reduce() = root.reduce()
}
