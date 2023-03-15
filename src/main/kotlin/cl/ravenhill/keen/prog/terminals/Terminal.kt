package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable


interface Terminal<T> : Reduceable<T> {
    override val arity: Int
        get() = 0

    override val height: Int
        get() = 0

    override fun flatten() = listOf(this)

    override fun deepCopy() = copy()

    override fun replaceChild(original: Reduceable<T>, new: Reduceable<T>) {
        throw InvalidStateException("child") {
            "Terminals do not have children."
        }
    }
}