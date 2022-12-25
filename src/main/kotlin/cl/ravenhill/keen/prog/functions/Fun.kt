package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable

/**
 * A Function is a [Reduceable] that takes a number of arguments and produces a result.
 *
 * @param T The type of the value.
 * @property children The children of the operation.
 * @property depth The depth of the reduceable tree.
 */
interface Fun<T> : Reduceable<T> {

    val children: List<Reduceable<T>>
    override val depth: Int
        get() = children.maxOf { it.depth } + 1

    /**
     * Sets the child at the given index.
     */
    operator fun set(index: Int, value: Reduceable<T>)
}