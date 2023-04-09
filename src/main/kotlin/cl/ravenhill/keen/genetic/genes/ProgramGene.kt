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
 * @param DNA The type of the value.
 * @param program The program tree represented by this gene.
 *
 * @property functions The list of functions that can be used to generate new programs.
 * @property terminals The list of terminals that can be used to generate new programs.
 * @property generationMethods The list of generation methods used to create new programs during
 *      mutation or crossover.
 *
 * @constructor Creates a new instance of [ProgramGene] with the specified ``program``, [functions],
 * [terminals], and [generationMethods].
 *
 * @since 2.0.0
 * @version 2.0.0
 */
class ProgramGene<DNA> internal constructor(
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
     * Returns the program tree represented by this gene.
     */
    override val dna = program

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

    // region : Equals, HashCode, ToString
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is ProgramGene<*> -> false
        else -> dna == other.dna
    }

    override fun hashCode() = Objects.hash(ProgramGene::class, dna)

    override fun toString() = dna.toString()
    // endregion
}
