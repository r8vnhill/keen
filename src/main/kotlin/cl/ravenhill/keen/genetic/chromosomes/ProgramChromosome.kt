package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.CoroutineConstructor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.GenerationMethod
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.generateProgramFull
import cl.ravenhill.keen.prog.generateProgramGrowing
import cl.ravenhill.keen.prog.generateProgramWith
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.util.addIfAbsent
import java.util.Objects

/**
 * Genetic programming is a type of machine learning where a population of candidate computer
 * programs, represented as strings of instructions, is evolved over time through the application of
 * genetic operators such as mutation, crossover, and selection.
 * The goal is to find a program that solves a particular problem or optimizes a particular
 * objective function.
 *
 * A [Chromosome] in genetic programming is a data structure that represents a candidate [Program].
 * The chromosome consists of a sequence of [Gene]s that encode a list of programs, where each gene
 * represents a single program.
 *
 * The `ProgramChromosome` class represents a chromosome that holds a collection of programs.
 * The programs are evaluated by interpreting the [Fun]ctions and [Terminal]s in a specific way,
 * according to a set of rules that depend on the particular application domain.
 *
 * The ``ProgramChromosome`` class can be used to solve a wide range of problems that can be
 * represented as computer programs.
 * For example, it can be used to evolve programs that solve mathematical problems, such as symbolic
 * regression or function optimization, or to evolve programs that play games, such as tic-tac-toe
 * or chess.
 *
 * __References:__
 * 1. Koza, John R. Genetic Programming: On the Programming of Computers by Means of Natural
 *    Selection. MIT Press, 1992.
 * 2. Koza, John R. Genetic Programming II: Automatic Discovery of Reusable Programs. MIT
 *    Press, 1994.
 *
 * @param T The type of the values that the functions and terminals in the program operate on.
 *
 * @property genes A list of genes that encode the program.
 * @property functions The functions that can be used in the program.
 * @property terminals The terminals that can be used in the program.
 * @property validator A function that validates the program genes before they are used to create
 * the program.
 * By default, it always returns true.
 * @property generationMethods A list of functions that are used to generate programs during the
 * creation process.
 * By default, the factory uses the [generateProgramGrowing] and [generateProgramFull] methods.
 *
 * @constructor Creates a new program chromosome with the given [genes], [functions], [terminals],
 * [validator], and [generationMethods].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class ProgramChromosome<T>(
    override val genes: List<ProgramGene<T>>,
    private val functions: List<Fun<T>>,
    private val terminals: List<Terminal<T>>,
    private val validator: (ProgramGene<T>) -> Boolean,
    private val generationMethods: List<GenerationMethod<T>>
) : Chromosome<Program<T>, ProgramGene<T>> {

    /**
     * Creates a new program chromosome with the given size, functions, terminals, validator,
     * generation methods, and constructor executor.
     *
     * @param size The size of the chromosome, i.e., the number of genes it contains.
     * @param functions A mutable list of functions that can be used in the programs encoded by the
     * chromosome.
     * @param terminals A mutable list of terminals that can be used in the programs encoded by the
     * chromosome.
     * @param validator A function that validates the program genes before they are used to create
     * the program.
     * @param generationMethods A list of functions that are used to generate programs during the
     * creation process.
     * @param constructorExecutor An object that executes the given constructor to create the
     * chromosome.
     *
     * @see SequentialConstructor
     * @see CoroutineConstructor
     */
    constructor(
        size: Int,
        functions: MutableList<Fun<T>>,
        terminals: MutableList<Terminal<T>>,
        validator: (ProgramGene<T>) -> Boolean,
        generationMethods: List<GenerationMethod<T>>,
        constructorExecutor: ConstructorExecutor<ProgramGene<T>>
    ) : this(constructorExecutor(size) {
        ProgramGene(
            generateProgramWith(generationMethods, terminals, functions, 1, Core.maxProgramDepth),
            functions,
            terminals,
            generationMethods
        )
    }, functions, terminals, validator, generationMethods)

    // region : -== OVERRIDES ==- :
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
    // endregion OVERRIDES

    /**
     * This class represents a factory for creating programs of type [T].
     *
     * @param T The type of the programs to be created by the factory.
     *
     * @property validator A function that validates the program genes before they are used to
     * create the programs.
     * By default, it always returns true.
     * @property generationMethods A list of functions that are used to generate programs during the
     * creation process.
     * By default, the factory uses the [generateProgramGrowing] and [generateProgramFull] methods.
     * @property functions The functions that can be used in the programs.
     * Empty by default.
     * @property terminals The terminals that can be used in the programs.
     * Empty by default.
     */
    class Factory<T> : Chromosome.AbstractFactory<Program<T>, ProgramGene<T>>() {
        var validator: (ProgramGene<T>) -> Boolean = { true }

        var generationMethods: List<((
            List<Terminal<T>>, List<Fun<T>>, Int, Int
        ) -> Program<T>)> = listOf(
            ::generateProgramGrowing, ::generateProgramFull
        )

        // region : -== FUNCTIONS ==- :
        /** The functions that can be used in the chromosome */
        private val _functions = mutableListOf<Fun<T>>()
        val functions get() = _functions.toList()

        /**
         * Adds a new function to the chromosome.
         */
        fun function(name: String, arity: Int, fn: (List<T>) -> T) =
            _functions.addIfAbsent(Fun(name, arity, fn))
        // endregion FUNCTIONS

        // region : -== TERMINALS ==- :
        /** The terminals that can be used in the chromosome */
        private val _terminals = mutableListOf<Terminal<T>>()
        val terminals get() = _terminals.toList()

        /**
         * Adds a new terminal to the chromosome.
         */
        fun terminal(fn: () -> Terminal<T>) = _terminals.addIfAbsent(fn())
        // endregion TERMINALS

        /// Documentation inherited from [Chromosome.Factory]
        override fun make() =
            ProgramChromosome(size, _functions, _terminals, validator, generationMethods, executor)
    }
}
