package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.*
import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.util.Tree
import java.util.*

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
    val functions: List<Fun<DNA>>,
    val terminals: List<Terminal<DNA>>,
) : AbstractTreeGene<Reduceable<DNA>>(program, program.arity), Gene<Reduceable<DNA>> {

    private val nodes = functions + terminals

    override val children: List<Reduceable<DNA>>
        get() = dna.children
    override fun equalTo(other: Tree<Reduceable<DNA>>): Boolean {
        TODO("Not yet implemented")
    }

    override val depth: Int
        get() = dna.depth

    init {
        enforce {
            program.height should BeAtMost(Core.maxProgramDepth) {
                "The program's depth (${program.depth}) is greater than the maximum " +
                        "allowed depth (${Core.maxProgramDepth})."
            }
        }
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
        val op = nodes.random(Core.random).copy()
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
        enforce {
            op.children.size should BeEqualTo(op.arity) {
                "The number of children (${op.children.size}) is not equal to the arity " +
                        "(${op.arity})"
            }
        }
        return op
    }

    override fun duplicate(dna: Reduceable<DNA>) =
        ProgramGene(dna, functions, terminals)

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
