package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Reduceable


/**
 * A terminal operation.
 *
 * @param T the type this operation returns.
 * @property arity the number of child operations this operation takes.
 *                 This is always 0 for terminals.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Terminal<T> : Reduceable<T> {
    override val arity: Int
        get() = 0
}