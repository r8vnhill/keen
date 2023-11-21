/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Reducible
import cl.ravenhill.keen.util.trees.Leaf

/**
 * Defines a terminal operation for tree-based genetic programming.
 *
 * In the context of genetic programming, terminals serve as the leaves of the program tree. Unlike other nodes,
 * terminals do not perform operations on child nodes but instead provide constant or variable values to the program.
 * Consequently, they don't have child nodes of their own.
 *
 * @param T The data type of the value provided by this terminal operation.
 * @property arity The number of child nodes associated with this terminal, which is always 0 for terminals.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Terminal<T> : Reducible<T>, Leaf<Reducible<T>> {

    /**
     * Produces a fresh instance of this terminal operation.
     *
     * This can be particularly useful when creating a new generation in genetic programming, ensuring that operations
     * are treated as separate entities rather than references to the same object.
     *
     * @return A new instance of the terminal operation.
     */
    fun create(): Terminal<T>

    override val arity: Int
        get() = 0
}
