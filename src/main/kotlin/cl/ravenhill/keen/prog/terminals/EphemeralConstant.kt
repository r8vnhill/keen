package cl.ravenhill.keen.prog.terminals

import java.util.*


class EphemeralConstant<T>(val generator: () -> T) : Terminal<T> {
    override val arity: Int = 0
    override fun copy() = EphemeralConstant(generator)

    override fun invoke(args: Array<out T>) = generator()

    override fun toString() = "${generator()}"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is EphemeralConstant<*> -> false
        else -> generator() == other.generator()
    }

    override fun hashCode() = Objects.hash(EphemeralConstant::class, generator())
}