package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.util.trees.Leaf

/**
 * Represents a terminal operation in a tree-based genetic programming system.
 * Terminals are operations that return a value and do not have any child nodes.
 *
 * @param T The type of value returned by this terminal operation.
 * @property arity The number of child nodes expected by this terminal. Always 0.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Terminal<T> : Reduceable<T>, Leaf<Reduceable<T>> {
    /**
     * Creates a new instance of this terminal operation.
     *
     * @return A new instance of this terminal operation.
     */
    fun create(): Terminal<T>

    override val arity: Int
        get() = 0
}
