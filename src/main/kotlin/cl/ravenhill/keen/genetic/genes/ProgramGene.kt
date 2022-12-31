package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal

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
    private val functions: List<Fun<DNA>>,
    private val terminals: List<Terminal<DNA>>,
) : AbstractTreeGene<Reduceable<DNA>>(program, program.arity), Gene<Reduceable<DNA>> {

    private val nodes = functions + terminals

    override lateinit var children: List<Reduceable<DNA>>
    override val depth: Int
        get() = dna.depth

    init {
        if (program.height > Core.maxProgramDepth) throw InvalidStateException("program") {
            "The program's depth (${program.depth}) is greater than the maximum " +
                    "allowed depth (${Core.maxProgramDepth})."
        }
        // Stores the program in a list (breadth-first).
        children = program.flatten()
    }

    /**
     * Reduces the program tree to a single value.
     *
     * @param args The arguments to the program.
     * @return The result of the program.
     */
    operator fun invoke(vararg args: DNA): DNA = dna(args)

    override fun generator(): Reduceable<DNA> {
        // Create a deep copy of a random node.
        val op = nodes.random(Core.random).deepCopy()
        generateChildren(op, op.depth + 1, nodes) // Generate the children.
        return op // Return the new node.
    }

    /**
     * Generates the children of the given node.
     *
     * @param op The node to generate the children for.
     * @param depth The depth of the node.
     * @param ops The valid nodes to generate the children from.
     * @return The node with the children.
     */
    private fun generateChildren(
        op: Reduceable<DNA>,
        depth: Int,
        ops: List<Reduceable<DNA>>
    ): Reduceable<DNA> {
        when (op) {
            is Fun<DNA> -> {
                // For each child of the node
                for (i in 0 until op.arity) {
                    // Creates a copy of a random node from the valid nodes.
                    val child = if (depth < Core.maxProgramDepth) {
                        ops.random(Core.random).copy()
                    } else {
                        terminals.random(Core.random).copy()
                    }
                    generateChildren(child, depth + 1, ops)
                    op[i] = child
                }
            }

            is Terminal<*> -> {
                // Do nothing.
            }

            else -> throw InvalidStateException("type") {
                "The node is not a valid type (${op::class})"
            }
        }
        return op
    }

    override fun duplicate(dna: Reduceable<DNA>) =
        ProgramGene(dna, functions, terminals)

    override fun toString() = children[0].toString()
}
