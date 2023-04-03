package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.CollectionRequirement.NotBeEmpty
import cl.ravenhill.keen.requirements.IntRequirement
import cl.ravenhill.keen.requirements.IntRequirement.*
import cl.ravenhill.keen.util.Copyable
import cl.ravenhill.keen.util.Tree

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
 * @property depth The depth of this node in the program tree.
 *
 * @constructor Creates a new program node with the given [reduceable] expression and [depth].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Program<V>(
    val reduceable: Reduceable<V>,
    private val depth: Int,
    override val children: List<Program<V>> = emptyList()
) : Tree<V, Program<V>>, Copyable<Program<V>> {
    // Inherit documentation from Tree.
    override val arity: Int = reduceable.arity

    val root: Program<V>
        get() = nodes.first()

    // Inherit documentation from Tree.
    override fun fromDepthFirst(nodes: List<Program<V>>): Program<V> {
        require(nodes.isNotEmpty()) { "Cannot create a program from an empty list of nodes." }

        val nodeIterator = nodes.iterator()

        // Recursively create the child nodes in depth-first order.
        fun createChildren(): List<Program<V>> {
            val children = mutableListOf<Program<V>>()
            var childDepth = depth + 1

            // Iterate through the child nodes in the depth-first order.
            while (nodeIterator.hasNext()) {
                val child = nodeIterator.next()

                // If the child node's depth is greater than or equal to the next expected depth, add it to the children list.
                if (child.depth >= childDepth) {
                    children.add(child)

                    // If the child node's depth is equal to the next expected depth, recursively create its children.
                    if (child.depth == childDepth) {
                        children.addAll(child.createChildren())
                    }
                } else {
                    // If the child node's depth is less than the next expected depth, it's not a child of this node.
                    nodeIterator.previous()
                    break
                }
            }

            return children
        }

        // Create the child nodes recursively in depth-first order.
        val children = createChildren()

        // Create and return a new program node with the collected children.
        return Program(reduceable, depth, children)
    }


    /**
     * Reduces this node to a value.
     *
     * @param args the arguments to reduce this node with.
     * @return the value this node reduces to.
     */
    fun reduce(vararg args: V) = root(*args)

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
    override fun copy(): Program<V> = Program(reduceable, depth, children.map { it.copy() })

    // Documentation inherited from Any
    override fun toString() =
        if (children.isEmpty()) reduceable.toString() else "($reduceable(${children.joinToString(", ")}))"
}
