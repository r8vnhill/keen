package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Reduceable


interface Terminal<T> : Reduceable<T> {
    override fun flatten() = listOf(this)
}