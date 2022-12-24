package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable


interface Fun<T> : Reduceable<T> {
    val children: MutableList<Reduceable<T>>
}