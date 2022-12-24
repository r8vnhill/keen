package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.prog.Fun
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.Terminal

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
    program: Reduceable<DNA>,
    childOffset: Int,
    private val operations: List<Fun<DNA>>,
    private val terminals: List<Terminal<DNA>>
) : AbstractTreeGene<Reduceable<DNA>>(program, childOffset, program.arity), Gene<Reduceable<DNA>> {

    operator fun invoke(vararg args: DNA): DNA = dna(*args)

    override fun generator(): Reduceable<DNA> {
        TODO("Not yet implemented")
    }

    override fun duplicate(dna: Reduceable<DNA>) =
        ProgramGene(dna, childOffset, operations, terminals)
}
