package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.util.validateRange

/**
 * This file provides the interface and abstract class for all functions.
 */

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

/**
 * Abstract class for all functions.
 * @param T The type of the value.
 * @property children The children of the operation.
 */
abstract class AbstractFun<T>: Fun<T> {

    /**
     * Backing field for the children.
     */
    @Suppress("PropertyName")
    protected abstract val _children: MutableList<Reduceable<T>>

    override val children: List<Reduceable<T>>
        get() = _children.toList()

    override fun set(index: Int, value: Reduceable<T>) {
        index.validateRange(0 to arity)
        _children[index] = value
    }

    override fun flatten() = listOf(this) + _children.flatMap { it.flatten() }

    override fun deepCopy() = copy().let {
        it as Fun
        for (i in 0 until arity) {
            it[i] = _children[i].deepCopy()
        }
        it
    }
}