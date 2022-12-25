package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable


class EphemeralConstant<T>(val generator: () -> T) : Terminal<T> {
    override val arity: Int = 0
    override fun copy() = EphemeralConstant(generator)

    override fun addChild(child: Reduceable<T>) {
        throw InvalidStateException("arity") { "Ephemeral constants do not have children." }
    }

    override fun invoke(args: Array<out T>) = generator()

    override fun toString() = "${generator()}"
}