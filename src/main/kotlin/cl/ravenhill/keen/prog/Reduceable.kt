package cl.ravenhill.keen.prog

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.util.Copyable
import cl.ravenhill.keen.util.ListTree

/**
 * A reduce-able operation.
 *
 * @param T The type of the value.
 * @property arity The number of arguments the operation takes.
 * @property depth The depth of the operation in the tree.
 * @property parent The parent of the operation (``null`` if it is the root).
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Reduceable<T>: ListTree<Reduceable<T>>, Copyable<Reduceable<T>> {
    var parent: Reduceable<T>?

    val arity: Int

    /**
     * Reduces the operation to a single value.
     *
     * @param args The arguments to the operation.
     * @return The result of the operation.
     */
    operator fun invoke(args: Array<out T>): T

    /**
     * Flattens the operation into a list.
     */
    fun flatten(): List<Reduceable<T>>

    val descendants: List<Reduceable<T>>
        get() = flatten().drop(1)

    override val size: Int
        get() = descendants.size + 1
}
