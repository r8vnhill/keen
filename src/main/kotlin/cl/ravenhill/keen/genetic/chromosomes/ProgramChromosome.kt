package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.generateProgramFull
import cl.ravenhill.keen.prog.generateProgramGrowing
import cl.ravenhill.keen.prog.generateProgramWith
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.util.addIfAbsent
import java.util.Objects


class ProgramChromosome<T> private constructor(
    override val genes: List<ProgramGene<T>>,
    private val functions: List<Fun<T>>,
    private val terminals: List<Terminal<T>>,
    private val validator: (ProgramGene<T>) -> Boolean,
    private val generationMethods: List<((
        List<Terminal<T>>, List<Fun<T>>, Int, Int
    ) -> Program<T>)>
) : Chromosome<Program<T>, ProgramGene<T>> {

    /// Documentation inherited from [Chromosome]
    override fun withGenes(genes: List<ProgramGene<T>>) = ProgramChromosome(
        genes,
        functions,
        terminals,
        validator,
        generationMethods
    )

    /// Documentation inherited from [Verifiable]
    override fun verify() = genes.isNotEmpty() && genes.all { it.verify() && validator(it) }

    /// Documentation inherited from [Any]
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is ProgramChromosome<*> -> false
        genes != other.genes -> false
        else -> true
    }

    /// Documentation inherited from [Any]
    override fun hashCode() = Objects.hash(ProgramChromosome::class, genes)

    /// Documentation inherited from [Any]
    override fun toString() = genes.map { it.dna }.joinToString("\n")

    class Factory<T> : Chromosome.AbstractFactory<Program<T>, ProgramGene<T>>() {

        /** The functions that can be used in the chromosome */
        private val _functions = mutableListOf<Fun<T>>()
        val functions get() = _functions.toList()

        /** The terminals that can be used in the chromosome */
        private val _terminals = mutableListOf<Terminal<T>>()
        val terminals get() = _terminals.toList()

        var validator: (ProgramGene<T>) -> Boolean = { true }

        var generationMethods: List<((
            List<Terminal<T>>, List<Fun<T>>, Int, Int
        ) -> Program<T>)> = listOf(
            ::generateProgramGrowing, ::generateProgramFull
        )

        /**
         * Adds a new function to the chromosome.
         */
        fun function(name: String, arity: Int, fn: (List<T>) -> T) =
            _functions.addIfAbsent(Fun(name, arity, fn))

        /**
         * Adds a new terminal to the chromosome.
         */
        fun terminal(fn: () -> Terminal<T>) = _terminals.addIfAbsent(fn())

        override fun make(): ProgramChromosome<T> {
            return ProgramChromosome(
                (0 until size).map {
                    ProgramGene(
                        generateProgramWith(
                            generationMethods,
                            _terminals,
                            _functions,
                            1,
                            Core.maxProgramDepth
                        ),
                        _functions,
                        _terminals
                    )
                },
                _functions,
                _terminals,
                validator,
                generationMethods
            )
        }
    }
}
