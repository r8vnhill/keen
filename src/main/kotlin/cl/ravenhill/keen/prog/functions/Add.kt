package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable

/**
 * Creates a new [Add] operation.
 */
fun add() = Add(0)

/**
 * Addition operation.
 *
 * @property depth The depth of the operation in the tree.
 * @property left The left child.
 * @property right The right child.
 * @property arity The number of arguments the operation takes.
 * @property children The children of the operation.
 * @constructor Creates a new [Add] operation.
 */
class Add(override val depth: Int) : Fun<Double> {


    var left: Reduceable<Double>
        get() = children[0]
        set(value) {
            children[0] = value
        }
    var right: Reduceable<Double>
        get() = children[1]
        set(value) {
            children[1] = value
        }
    override val arity: Int = 2
    override val children = mutableListOf<Reduceable<Double>>()

    override fun addChild(child: Reduceable<Double>) {
        if (children.size >= arity) throw InvalidStateException("arity") {
            "The number of children is greater than the arity."
        }
        children.add(child)
    }

    override fun copy(depth: Int) = Add(depth)
    override fun flatten() = listOf(this) + left.flatten() + right.flatten()

    override fun invoke(args: Array<out Double>) = left(args) + right(args)

    override fun toString(): String {
        return "($left + $right)"
    }
}
