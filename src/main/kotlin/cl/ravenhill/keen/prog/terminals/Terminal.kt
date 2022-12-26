package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Reduceable


interface Terminal<T> : Reduceable<T> {
    override val arity: Int
        get() = 0
    override val depth: Int
        get() = 1
    override fun flatten() = listOf(this)

    override fun deepCopy() = copy()
}