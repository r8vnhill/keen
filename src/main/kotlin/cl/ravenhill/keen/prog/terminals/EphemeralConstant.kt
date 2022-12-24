package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable


class EphemeralConstant<T>(override val depth: Int, val generator: () -> T) : Terminal<T> {
    override val arity: Int = 0
    override fun copy(depth: Int) = EphemeralConstant(depth, generator)

    override fun addChild(child: Reduceable<T>) {
        throw InvalidStateException("arity") { "Ephemeral constants do not have children." }
    }

    override fun invoke(args: Array<out T>) = generator()

    override fun toString() = "${generator()}"
}