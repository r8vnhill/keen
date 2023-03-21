package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BeAtMost
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.Copyable
import cl.ravenhill.keen.util.ListTree
import cl.ravenhill.keen.util.Tree

/***************************************************************************************************
 * Structures and functions to represent and manipulate program trees.
 **************************************************************************************************/


/**
 * A program is a list of program nodes.
 *
 * @param T The type of the value this program reduces to.
 * @param nodes The list of nodes that make up this program.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Program<T>(override val nodes: List<ProgramNode<T>>) : ListTree<T>, Copyable<Program<T>> {

    /**
     * Indicates whether the children of each node in the program tree have been assigned.
     */
    private var linked = false

    // Inherit documentation from Tree.
    override val height = nodes.maxBy { it.depth }.depth

    // Inherit documentation from Tree.
    override val size: Int
        get() = nodes.size

    /**
     * The list of children nodes of the program nodes at depth 1 (i.e., the direct children of the
     * root node).
     */
    override val children = nodes.filter { it.depth == 1 }

    /**
     * The arity of the program, which is the number of direct children of the root node.
     */
    override val arity = children.size

    /**
     * Reduces the program tree to a value by invoking the root node with the given arguments.
     *
     * @param args the arguments to reduce the program tree with.
     * @return the value the program tree reduces to.
     */
    fun reduce(vararg args: T): T {
        this.linkChildren()
        return nodes.first().invoke(*args)
    }

    /**
     * Assigns children to each node in the program tree.
     * This function assumes that the nodes in the program tree are stored in a breadth-first order
     * in the program array.
     * For each node in the array, this function determines its parent by looking at the last node
     * in the parents list whose depth is one less than the current node's depth.
     * It then adds the current node as a child of its parent node, and appends the current node to
     * the parents list.
     * This process continues until all nodes in the program array have been assigned their
     * children.
     */
    private fun linkChildren() {
        if (linked) return
        val parents = mutableListOf<ProgramNode<T>>()
        for (node in nodes) {
            // If the node is the root node, add it to the parents list and continue.
            if (node.depth == 0) {
                parents.add(node)
                continue
            }
            // Find the parent node by looking at the last node in the parents list whose depth is
            // one less than the current node's depth.
            val parent = parents.last { it.depth == node.depth - 1 }
            // Add the current node as a child of its parent node.
            parent.addChild(node)
            // Append the current node to the parents list.
            parents.add(node)
        }
        // Set linked to true to avoid unnecessary repeated assignments.
        linked = true
    }

    // Inherit documentation from Any.
    override fun toString() = nodes.toString()

    // Inherit documentation from Copyable.
    override fun copy(): Program<T> {
        val nodes = this.nodes.map { it.copy() }
        return Program(nodes)
    }
}

/**
 * A node in a program tree.
 *
 * @param V The type of the value this node reduces to.
 *
 * @property reduceable the reduceable expression used to reduce this node.
 * @property depth the depth of this node.
 *
 * @constructor Creates a new program node with the given reduceable expression.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 */
class ProgramNode<V>(val reduceable: Reduceable<V>, val depth: Int) : Tree<V>,
        Copyable<ProgramNode<V>> {
    // Inherit documentation from Tree.
    override val arity: Int = reduceable.arity
    // Inherit documentation from Tree.
    override val height: Int
        get() = children.maxBy { it.height }.height + 1

    /**
     * The list of children of this node.
     */
    private val _children = mutableListOf<ProgramNode<V>>()
    // Inherit documentation from Tree.
    override val children: List<ProgramNode<V>>
        get() = _children

    // Inherit documentation from Tree.
    override val size: Int
        get() = _children.fold(1) { acc, node -> acc + node.size }

    /**
     * Reduces this node to a value.
     *
     * @param args the arguments to reduce this node with.
     * @return the value this node reduces to.
     */
    operator fun invoke(vararg args: V): V = reduceable(children.map { it(*args) })

    /**
     * Adds a child to this node.
     *
     * This function enforces the following requirements:
     * - The child's depth should be one more than this node's depth.
     * - The child's arity should be less than or equal to this node's arity.
     *
     * @param node the child to add.
     */
    fun addChild(node: ProgramNode<V>) {
        enforce {
            node.depth should BeEqualTo(depth + 1)
            node.children.size should BeAtMost(arity - 1)
        }
        _children.add(node)
    }

    // Documentation inherited from Copyable
    override fun copy() = ProgramNode(reduceable, depth)

    // Documentation inherited from Any
    override fun toString() = reduceable.toString()
}
