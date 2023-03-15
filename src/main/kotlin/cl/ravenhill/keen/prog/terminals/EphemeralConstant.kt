package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.util.Tree
import java.util.*


class EphemeralConstant<T>(val generator: () -> T) : Terminal<T> {
    override val arity: Int = 0

    override var parent: Fun<T>? = null

    val value = generator()

    override fun copy() = EphemeralConstant(generator)
    override fun staticCopy() =
        EphemeralConstant { value }.also { it.parent = parent }

    override fun deepCopy() =
        EphemeralConstant { value }.also { it.parent = parent }

    override var children: List<Reduceable<T>> = emptyList()
    override fun equalTo(other: Tree<Reduceable<T>>): Boolean {
        if (other !is EphemeralConstant<*>) {
            return false
        }
        return value == other.value
    }

    override fun invoke(args: Array<out T>) = value

    override fun toString() = "$value"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is EphemeralConstant<*> -> false
        else -> value == other.value
    }

    override fun hashCode() = Objects.hash(EphemeralConstant::class, value)
}