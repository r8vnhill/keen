package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal


class ProgramChromosome<I> private constructor(
    override val genes: List<ProgramGene<I>>,
    private val functions: List<Fun<I>>,
    private val terminals: List<Terminal<I>>,
    private val validator: (ProgramChromosome<I>) -> Boolean
) : Chromosome<Reduceable<I>> {

    class Factory<T> : Chromosome.Factory<T> {

        lateinit var operations: List<Reduceable<T>>

        override fun make(): Chromosome<T> {
            TODO("Not yet implemented")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun duplicate(genes: List<Gene<Reduceable<I>>>) =
        ProgramChromosome(genes as List<ProgramGene<I>>, functions, terminals, validator)
}