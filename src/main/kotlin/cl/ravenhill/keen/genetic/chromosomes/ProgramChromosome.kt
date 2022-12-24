package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.util.trees.Tree


class ProgramChromosome<I> private constructor(
    override val genes: List<Gene<I>>,
    private val functions: List<Fun<I>>,
    private val terminals: List<Terminal<I>>,
    private val validator: (ProgramChromosome<I>) -> Boolean
) : Chromosome<I> {
    class Factory<T> : Chromosome.Factory<T> {

        lateinit var operations: List<Reduceable<T>>

        override fun make(): Chromosome<T> {
            TODO("Not yet implemented")
        }
    }

    override fun duplicate(genes: List<Gene<I>>): Chromosome<I> {
        TODO("Not yet implemented")
    }
}