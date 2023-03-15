package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.prog.Reduceable
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
interface Fun<T> : Reduceable<T> {

    override val children: List<Reduceable<T>>

    /**
     * Sets the child at the given index.
     */
    operator fun set(index: Int, value: Reduceable<T>)
    operator fun get(i: Int): Reduceable<T>

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

    override val children: List<Reduceable<T>>
        get() = _children.toList()


    override var parent: Fun<T>? = null


    override fun set(index: Int, value: Reduceable<T>) {
        enforce {
            index should BeInRange(0..arity)
        }
        _children[index] = value
        _children[index].parent = this
    }

    override fun replaceChild(original: Reduceable<T>, new: Reduceable<T>) {
        enforce {
            requirement(
                "The original child should be a child of this node"
            ) { original.parent === this@AbstractFun }

            _children.size should BeEqualTo(arity) {
                "The parent of the original child should have the same arity as the number of " +
                        "children before the replacement"
            }
        }
        // Here we create a copy because modifying its parent would generate an error while
        // performing the replacement in the other direction (second swap).
        val newCopy = new.deepCopy()
        children.forEachIndexed { index, reduceable ->
            if (reduceable === original) {
                set(index, newCopy)
            }
        }
        enforce {
            requirement(
                "The new child should be a child of this node"
            ) { newCopy.parent === this@AbstractFun }

            _children.size should BeEqualTo(arity) {
                "The parent of the new child should have the same arity as the number of " +
                        "children after the replacement"
            }
        }
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

