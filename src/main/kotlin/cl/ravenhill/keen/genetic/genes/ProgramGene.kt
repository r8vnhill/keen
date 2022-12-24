package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.functions.add
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.prog.terminals.Variable

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
    private val filter: (Reduceable<DNA>) -> Boolean = { true }
) : AbstractTreeGene<Reduceable<DNA>>(program, program.arity), Gene<Reduceable<DNA>> {

    private val nodes = functions + terminals

    override lateinit var children: List<Reduceable<DNA>>

    init {
        if (program.depth > Core.maxProgramDepth) throw InvalidStateException("program") {
            "The program's depth is greater than the maximum allowed depth (${Core.maxProgramDepth})."
        }
        // Stores the program in a list (breadth-first).
        children = program.flatten()
    }

    /**
     * Reduces the program tree to a single value.
     *
     * @param args Array<out DNA>
     * @return DNA
     */
    operator fun invoke(vararg args: DNA): DNA = dna(args)

    override fun generator(): Reduceable<DNA> {
        // Filter the valid nodes by the filter function.
        val validOps = children.filter(filter)
        // Get a random node from the valid nodes.
        val op = validOps.random(Core.random)
        generateChildren(op, op.depth + 1, nodes)
        return op
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
            is Fun<*> -> {
                op.children.clear()
                // For each child of the node
                for (i in 0 until op.arity) {
                    // Creates a copy of a random node from the valid nodes.
                    val child = ops.random(Core.random).copy(depth)
                    generateChildren(child, depth + 1, ops)
                    op.addChild(child)
                }
            }

            is Terminal<*> -> {
                // Do nothing.
            }

            else -> throw InvalidStateException("type") { "The node is not a valid type." }
        }
        return op
    }

    override fun duplicate(dna: Reduceable<DNA>) =
        ProgramGene(dna, functions, terminals)

    override fun toString() = dna.toString()
}

fun main() {
    val add = add()
    add.addChild(EphemeralConstant(1) { 1.0 })
    add.addChild(EphemeralConstant(2) { 2.0 })
    val gene = ProgramGene(
        add,
        listOf(add),
        listOf(EphemeralConstant(0) { 1.0 }, Variable("x", 0, 0))
    )
    println(gene)
    val gene2 = gene.mutate()
    println(gene2)
}