package cl.ravenhill.keen.prog

import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.util.Copyable
import cl.ravenhill.keen.util.ListTree

/**
 * A reduce-able operation.
 *
 * @param T The type of the value.
 * @property arity The number of child operations this operation takes.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Reduceable<T>: Copyable<Reduceable<T>> {

    val arity: Int

    /**
     * Reduces the operation to a single value.
     *
     * @param args The arguments to the operation.
     * @return The result of the operation.
     */
    operator fun invoke(args: List<T>): T
}
