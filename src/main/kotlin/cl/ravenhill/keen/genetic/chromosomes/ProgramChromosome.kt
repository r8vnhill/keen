package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Add
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.functions.GreaterThan
import cl.ravenhill.keen.prog.functions.If
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.prog.terminals.Variable


class ProgramChromosome<I> private constructor(
    override val genes: List<ProgramGene<I>>,
    private val functions: List<Fun<I>>,
    private val terminals: List<Terminal<I>>,
    private val validator: (ProgramChromosome<I>) -> Boolean
) : Chromosome<Reduceable<I>> {

    class Factory<T> : Chromosome.Factory<Reduceable<T>> {
        var size = 1
        private val functions = mutableListOf<Fun<T>>()
        private val terminals = mutableListOf<Terminal<T>>()
        private var validator: (ProgramChromosome<T>) -> Boolean = { true }

        fun function(fn: () -> Fun<T>) = apply { functions.add(fn()) }
        fun terminal(fn: () -> Terminal<T>) = apply { terminals.add(fn()) }
        override fun make() = ProgramChromosome(
                (0 until size).map {
                    ProgramGene(
                        (functions + terminals).random(Core.random),
                        functions,
                        terminals
                    )
                },
                functions,
                terminals,
                validator
            )
    }

    @Suppress("UNCHECKED_CAST")
    override fun duplicate(genes: List<Gene<Reduceable<I>>>) =
        ProgramChromosome(genes as List<ProgramGene<I>>, functions, terminals, validator)

    override fun toString() = genes.map { it.dna }.joinToString("\n")
}
