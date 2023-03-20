package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BeAtMost
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo

/***************************************************************************************************
 * Structures and functions to represent and manipulate program trees.
 **************************************************************************************************/


/**
 * A program is a list of program nodes.
 *
 * @param T The type of the value this program reduces to.
 */
typealias Program<T> = List<ProgramNode<T>>

/**
 * A node in a program tree.
 *
 * @param V The type of the value this node reduces to.
 * @property reduceable the reduceable expression used to reduce this node.
 * @property depth the depth of this node.
 * @property arity the arity of the reduceable expression.
 * @property children the children of this node.
 * @property parent the parent of this node.
 *
 * @constructor Creates a new program node with the given reduceable expression.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 */
class ProgramNode<V>(val reduceable: Reduceable<V>, val depth: Int) {

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
    operator fun invoke(vararg args: V): V = reduceable(args)

    /**
     * Adds a child to this node.
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

    override fun toString() = reduceable.toString()
}


fun <T> Program<T>.reduce(vararg args: T): T {
    this.linkChildren()
    return this.first().invoke(*args)
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
fun <T> Program<T>.linkChildren() {
    val parents = mutableListOf(this.first())
    var i = 1
    while (i < this.size) {
        val node = this.first()
        while (parents.last().depth + 1 != node.depth) {
            parents.removeLast()
        }
        parents.last().addChild(node)
        parents.add(node)
        i++
    }
}
