package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.prog.ProgramNode
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.generateProgramFull
import cl.ravenhill.keen.prog.generateProgramGrowing
import cl.ravenhill.keen.prog.generateProgramWith
import cl.ravenhill.keen.prog.terminals.Terminal
import java.util.Objects

/**
 * A [Gene] that represents a program tree.
 *
 * @param DNA The type of the value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class ProgramGene<DNA> internal constructor(
    program: List<ProgramNode<DNA>>,
    val functions: List<Fun<DNA>>,
    val terminals: List<Terminal<DNA>>,
    private val generationMethods: List<((
        List<Terminal<DNA>>, List<Fun<DNA>>, Int, Int
    ) -> List<ProgramNode<DNA>>)> = listOf(
        ::generateProgramGrowing, ::generateProgramFull
    )
) : Gene<List<ProgramNode<DNA>>> {

    override val dna = program

    override fun generator() = generateProgramWith(generationMethods, terminals, functions, 1, 1)

    override fun duplicate(dna: List<ProgramNode<DNA>>) =
        ProgramGene(dna, functions, terminals, generationMethods)


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
