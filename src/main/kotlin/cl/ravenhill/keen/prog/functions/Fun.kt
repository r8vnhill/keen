package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.requirements.IntRequirement
import cl.ravenhill.keen.requirements.IntRequirement.*
import cl.ravenhill.keen.util.Tree

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
    operator fun get(i: Int): Reduceable<T>
    fun removeChild(original: Reduceable<T>) {
        children = children.filterFirst { it == original }
    }

    fun addChild(new: Reduceable<T>) {
        children = children + new
    }
}

private fun <E> List<E>.filterFirst(function: (E) -> Boolean): List<E> {
    val filtered = mutableListOf<E>()
    var found = false
    for (element in this) {
        if (!found && function(element)) {
            found = true
            continue
        }
        filtered.add(element)
    }
    return filtered
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
        enforce {
            index should BeInRange(0..arity)
        }
        _children[index] = value.deepCopy()
        _children[index].parent = this
    }

    override fun replaceChild(original: Reduceable<T>, new: Reduceable<T>) {
        val originalParent = original.parent
        originalParent?.addChild(new.staticCopy())
        originalParent?.removeChild(original)
    }

    override fun flatten(): List<Reduceable<T>> {
        val flat = mutableListOf<Reduceable<T>>()
        for (child in _children) {
            flat.add(child)
            flat.addAll(child.flatten())
        }
        return listOf(this) + flat
    }

    override fun deepCopy() = copy().let {
        it as Fun
        enforce {
            it.children.size should BeEqualTo(arity)
        }
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

    override operator fun get(i: Int): Reduceable<T> {
        enforce {
            i should BeInRange(0..arity)
        }
        return _children[i]
    }

    override fun equalTo(other: Tree<Reduceable<T>>): Boolean {
        if (other !is Fun) {
            return false
        }
        if (arity != other.arity) {
            return false
        }
        for (i in 0 until arity) {
            if (this[i] != other[i]) {
                return false
            }
        }
        return true
    }
}

