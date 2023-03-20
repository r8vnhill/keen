package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BeAtMost
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.Copyable
import cl.ravenhill.keen.util.ListTree

/***************************************************************************************************
 * Structures and functions to represent and manipulate program trees.
 **************************************************************************************************/


/**
 * A program is a list of program nodes.
 *
 * @param T The type of the value this program reduces to.
 */
class Program<T>(private val nodes: List<ProgramNode<T>>) : ListTree<ProgramNode<T>>,
        Copyable<Program<T>> {
    /**
     * Indicates whether the children of each node in the program tree have been assigned.
     */
    private var linked = false

    val height = nodes.maxBy { it.depth }.depth

    override val size: Int
        get() = nodes.size

    override val children: List<ProgramNode<T>>
        get() = nodes.filter { it.depth == 1 }

    /**
     * Reduces the program tree to a value.
     *
     * @receiver the program tree represented as a breadth-first array of program nodes.
     * @param args the arguments to reduce the program tree with.
     * @return the value the program tree reduces to.
     */
    fun reduce(vararg args: T): T {
        this.linkChildren()
        return nodes.first().invoke(*args)
    }

    /**
     * Assigns children to each node in the program tree.
     * This function assumes that the nodes in the program tree are stored in a breadth-first order in
     * the program array.
     * For each node in the array, this function determines its parent by looking at the last node in
     * the parents list whose depth is one less than the current node's depth.
     * It then adds the current node as a child of its parent node, and appends the current node to the
     * parents list.
     * This process continues until all nodes in the program array have been assigned their children.
     *
     * @receiver the program tree represented as a breadth-first array of program nodes.
     */
    private fun linkChildren() {
        if (linked) return
        val parents = mutableListOf<ProgramNode<T>>()
        for (node in nodes) {
            if (node.depth == 0) {
                parents.add(node)
                continue
            }
            val parent = parents.last { it.depth == node.depth - 1 }
            parent.addChild(node)
            parents.add(node)
        }
        linked = true
    }

    override fun toString() = nodes.toString()

    override fun copy(): Program<T> {
        val nodes = this.nodes.map { it.copy() }
        return Program(nodes)
    }
}

/**
 * A node in a program tree.
 *
 * @param V The type of the value this node reduces to.
 * @property reduceable the reduceable expression used to reduce this node.
 * @property depth the depth of this node.
 * @property arity the arity of the reduceable expression.
 * @property children the children of this node.
 *
 * @constructor Creates a new program node with the given reduceable expression.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 */
class ProgramNode<V>(val reduceable: Reduceable<V>, val depth: Int) : Copyable<ProgramNode<V>> {

    val arity: Int = reduceable.arity

    private val _children = mutableListOf<ProgramNode<V>>()

    val children: List<ProgramNode<V>>
        get() = _children

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
