package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.util.validateRange

/**
 * Creates a new [Add] operation.
 */
fun add(left: Reduceable<Double>, right: Reduceable<Double>) = Add().apply {
    set(0, left)
    set(1, right)
}

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
class Add : Fun<Double> {
    var left: Reduceable<Double>
        get() = children[0]
        set(value) {
            _children[0] = value
        }
    var right: Reduceable<Double>
        get() = children[1]
        set(value) {
            _children[1] = value
        }
    override val arity: Int = 2

    private val _children =
        // The children are initialized with ephemeral constants to avoid nullability
        // issues. The initial values are set to 0.0 since that is the identity of the
        // addition operation.
        mutableListOf<Reduceable<Double>>(EphemeralConstant { 0.0 },
            EphemeralConstant { 0.0 })
    override val children: List<Reduceable<Double>>
        get() = _children.toList()

    override fun set(index: Int, value: Reduceable<Double>) {
        index.validateRange(0 to arity)
        _children[index] = value
    }

    override fun addChild(child: Reduceable<Double>) {
        if (_children.size >= arity) throw InvalidStateException("arity") {
            "The number of children is greater than the arity."
        }
        _children.add(child)
    }

    override fun copy() = Add()
    override fun flatten() = listOf(this) + left.flatten() + right.flatten()

    override fun invoke(args: Array<out Double>) = left(args) + right(args)

    override fun toString(): String {
        return "($left + $right)"
    }
}
