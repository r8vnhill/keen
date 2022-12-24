package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable

fun add() = Add(0)

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
