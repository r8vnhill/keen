package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.terminals.Terminal

/**
 * This is an open class that represents a function with a name, an arity, and a body.
 * The function is "reduceable", which means that it can be simplified by evaluating its body
 * with its arguments, recursively reducing any sub-expressions that may be functions or
 * terminals.
 *
 * @param name The name of the function.
 * @param arity The arity of the function, which is the number of arguments it takes.
 * @param body The body of the function, which is a lambda that takes a list of arguments
 *             and returns a value of type T.
 * @param T The generic type of the function's return value.
 * @see Terminal
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
open class Fun<T>(
    private val name: String,
    override val arity: Int,
    private val body: (List<T>) -> T
) : Reduceable<T> {

    // Inherited documentation from Reduceable<T>
    override fun invoke(args: List<T>) = body(args)

    // Inherited documentation from Any
    override fun toString() = name
}
