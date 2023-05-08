package cl.ravenhill.keen.prog

import cl.ravenhill.keen.util.Copyable
import cl.ravenhill.keen.util.trees.Tree

/**
 * Represents a node in a program tree.
 *
 * A program tree is a tree data structure that represents a program.
 * Each node in the tree represents an operation that can be performed on some input values to
 * produce a result.
 * The tree is constructed by recursively applying operations to the input values until a result is
 * produced.
 *
 * @param V The type of the value this node reduces to.
 *
 * @property reduceable The reduceable expression used to reduce this node.
 * @property children The children of this node.
 *
 * @constructor Creates a new program node with the given [reduceable] expression and [children].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Program<V>(
    val reduceable: Reduceable<V>,
    override val children: List<Program<V>> = emptyList()
) : Tree<Reduceable<V>, Program<V>>, Copyable<Program<V>> {
    // Inherit documentation from Tree.
    override val arity: Int = reduceable.arity
    // Inherit documentation from Tree.
    override val value = reduceable

    val root: Program<V>
        get() = nodes.first()

    // Inherit documentation from Tree.
    override fun createNode(value: Reduceable<V>, children: List<Program<V>>) =
        Program(value, children)


    // Inherit documentation from Tree.
    override val nodes: List<Program<V>>
        get() = listOf(this) + children.flatMap { it.nodes }

    /**
     * Reduces this node to a value.
     *
     * @param args the arguments to reduce this node with.
     * @return the value this node reduces to.
     */
    operator fun invoke(vararg args: V): V = reduceable(children.map { it(*args) })

    // Documentation inherited from Copyable
    override fun copy(): Program<V> = Program(reduceable, children.map { it.copy() })

    // Documentation inherited from Any
    override fun toString() =
        if (children.isEmpty()) reduceable.toString() else "($reduceable(${children.joinToString(", ")}))"
}
