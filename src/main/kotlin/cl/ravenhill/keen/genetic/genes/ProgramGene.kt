package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.generateProgramFull
import cl.ravenhill.keen.prog.generateProgramGrowing
import cl.ravenhill.keen.prog.generateProgramWith
import cl.ravenhill.keen.prog.terminals.Terminal
import java.util.Objects

/**
 * A [Gene] that represents a program tree.
 *
 * This gene contains a program tree represented by a [Program] instance.
 * It also contains a list of functions and a list of terminals that can be used to generate new
 * programs during mutation or crossover.
 * Additionally, it contains a list of generation methods that determine how new programs are
 * created.
 *
 * Genetic programming is a type of evolutionary algorithm that evolves computer programs to solve a
 * specific problem.
 * In this case, the [ProgramGene] represents a single program in the population being evolved.
 * The functions and terminals are the building blocks of the programs, and the generation methods
 * are used to create new programs during mutation or crossover.
 *
 * @param DNA The type of the value.
 * @param program The program tree represented by this gene.
 * @property functions The list of functions that can be used to generate new programs.
 * @property terminals The list of terminals that can be used to generate new programs.
 * @property generationMethods The list of generation methods used to create new programs during
 * mutation or crossover.
 *
 * @since 2.0.0
 * @version 2.0.0
 */
class ProgramGene<DNA>(
    program: Program<DNA>,
    val functions: List<Fun<DNA>>,
    val terminals: List<Terminal<DNA>>,
    private val generationMethods: List<((
        List<Terminal<DNA>>, List<Fun<DNA>>, Int, Int
    ) -> Program<DNA>)> = listOf(
        ::generateProgramGrowing, ::generateProgramFull
    )
) : Gene<Program<DNA>, ProgramGene<DNA>> {

    /**
     * The program tree represented by this gene.
     */
    override val dna = program

    // region : -== FACTORY METHODS ==-
    /**
     * Generates a new program tree by applying one of the [generationMethods] with the list of
     * [terminals] and [functions] and the maximum depth specified.
     */
    override fun generator() =
        generateProgramWith(generationMethods, terminals, functions, 1, Core.maxProgramDepth)

    /**
     * Creates a new instance of [ProgramGene] with the specified [dna], [functions], [terminals],
     * and [generationMethods].
     *
     * @param dna The program tree for the new instance.
     */
    override fun withDna(dna: Program<DNA>) =
        ProgramGene(dna.copy(), functions, terminals, generationMethods)
    // endregion FACTORY METHODS

    // region : -== IMPLEMENTATION OF [Any] ==-
    /// Documentation inherited from [Any]
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is ProgramGene<*> -> false
        else -> dna == other.dna
    }

    /// Documentation inherited from [Any]
    override fun hashCode() = Objects.hash(ProgramGene::class, dna)

    /// Documentation inherited from [Any]
    override fun toString() = dna.toString()
    // endregion IMPLEMENTATION OF [Any]
}
