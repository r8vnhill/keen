package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.util.validateRange
import java.util.*

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
 * @property arity The number of arguments the operation takes.
 * @property children The children of the operation.
 * @constructor Creates a new [Add] operation.
 */
class Add : Fun<Double> {
    override val arity: Int = 2

    // The children are initialized with ephemeral constants to avoid nullability
    // issues. The initial values are set to 0.0 since that is the identity of the
    // addition operation.
    private val _children =
        mutableListOf<Reduceable<Double>>(EphemeralConstant { 0.0 },
            EphemeralConstant { 0.0 })
    override val children: List<Reduceable<Double>>
        get() = _children.toList()

    override fun set(index: Int, value: Reduceable<Double>) {
        index.validateRange(0 to arity)
        _children[index] = value
    }

    override fun copy() = Add()
    override fun flatten() = listOf(this) + _children.flatMap { it.flatten() }
    override fun invoke(args: Array<out Double>) = _children[0](args) + _children[1](args)

    override fun toString() = "(${_children[0]} + ${_children[1]})"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Add -> false
        else -> _children == other._children
    }

    override fun hashCode() = Objects.hash(Add::class, _children)
}
