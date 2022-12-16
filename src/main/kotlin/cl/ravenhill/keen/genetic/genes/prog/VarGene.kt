package cl.ravenhill.keen.genetic.genes.prog

import cl.ravenhill.keen.genetic.genes.Gene


class VarGene<T>(override val dna: Program<T>) : ProgramGene<T> {
    override fun duplicate(dna: Program<T>): Gene<Program<T>> {
        TODO("Not yet implemented")
    }

    override fun generator(): Program<T> {
        TODO("Not yet implemented")
    }
}

class Variable<T>(val name: String, val value: T)
