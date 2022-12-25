package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable


class Variable<T>(private val name: String, val index: Int) : Terminal<T> {

    override val arity: Int = 0

    override fun copy(): Reduceable<T> = Variable(name, index)

    override fun addChild(child: Reduceable<T>) {
        throw InvalidStateException("arity") { "Variables do not have children." }
    }

    override fun invoke(args: Array<out T>) = args[index]

    override fun toString() = name
}