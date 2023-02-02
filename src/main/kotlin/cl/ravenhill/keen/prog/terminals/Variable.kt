package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import java.util.*


class Variable<T>(private val name: String, val index: Int) : Terminal<T> {

    override val arity: Int = 0

    override var parent: Fun<T>? = null

    override fun copy(): Reduceable<T> =
        Variable<T>(name, index).also { it.parent = parent }

    override fun staticCopy(): Reduceable<T> {
        return copy()
    }

    override var children: List<Reduceable<T>> = emptyList()


    override fun invoke(args: Array<out T>) = args[index]

    override fun toString() = name

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Variable<*> -> false
        else -> name == other.name && index == other.index
    }

    override fun hashCode() = Objects.hash(Variable::class, name, index)
}