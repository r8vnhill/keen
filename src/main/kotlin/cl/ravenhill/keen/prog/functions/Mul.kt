package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import java.util.*

/**
 * Creates a new [Mul] operation.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
fun mul(left: Reduceable<Double>, right: Reduceable<Double>) = Mul().apply {
    set(0, left)
    set(1, right)
}

/**
 * A multiplication operation.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Mul : AbstractFun<Double>() {

    override val _children = mutableListOf<Reduceable<Double>>(EphemeralConstant { 1.0 },
        EphemeralConstant { 1.0 })

    override val arity = 2

    override fun copy() = Mul().also { it.parent = parent }

    override fun invoke(args: Array<out Double>) = _children[0](args) * _children[1](args)

    // region : equals, hashCode, toString
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Mul -> false
        else -> _children == other._children
    }

    override fun hashCode() = Objects.hash(Mul::class, _children)

    override fun toString() = "(${_children[0]} * ${_children[1]})"
    // endregion
}
