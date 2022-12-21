package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.AbstractBinaryOperation
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.op.Fun


class ProgramChromosome<I> private constructor(genes: List<ProgramGene<I>>) {
    class Factory<T> :Chromosome.Factory<T> {

        lateinit var operations: List<Fun<T>>

        override fun make(): Chromosome<T> {
            TODO("Not yet implemented")
        }
    }
}