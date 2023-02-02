package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.Core.Contract
import cl.ravenhill.keen.IntRequirement.*
import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.Reduceable

/**
 * This file provides the interface and abstract class for all functions.
 */

/**
 * A Function is a [Reduceable] that takes a number of arguments and produces a result.
 *
 * @param T The type of the value.
 * @property children The children of the operation.
 * @property depth The depth of the reduceable tree.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Fun<T> : Reduceable<T> { // Fun

    override var children: List<Reduceable<T>>

    /**
     * Sets the child at the given index.
     */
    operator fun set(index: Int, value: Reduceable<T>)
}

/**
 * Abstract class for all functions.
 * @param T The type of the value.
 * @property children The children of the operation.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
abstract class AbstractFun<T> : Fun<T> {

    /**
     * Backing field for the children.
     */
    @Suppress("PropertyName")
    protected abstract val _children: MutableList<Reduceable<T>>

    override var children: List<Reduceable<T>>
        get() = _children.toList()
        set(value) {
            _children.clear()
            _children.addAll(value)
        }

    override var parent: Fun<T>? = null


    override fun set(index: Int, value: Reduceable<T>) {
        Contract {
            index should BeInRange(0..arity)
        }
        _children[index] = value.deepCopy()
        _children[index].parent = this
    }

    override fun replaceChild(original: Reduceable<T>, new: Reduceable<T>) {
        val index = _children.indexOf(original)
        if (original.parent != this) {
            throw InvalidStateException("child") {
                "The child $original is not a child of this node."
            }
        }
        set(index, new)
    }

    override fun flatten() = listOf(this) + _children.flatMap { it.flatten() }

    override fun deepCopy() = copy().let {
        it as Fun
        for (i in 0 until arity) {
            it[i] = _children[i].deepCopy().also { child -> child.parent = it }
        }
        it
    }

    override fun staticCopy() = copy().let {
        it as Fun
        for (i in 0 until arity) {
            it[i] = _children[i].staticCopy()
        }
        it
    }
}