/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.util.trees.Intermediate

/**
 * Represents a named function that is capable of reducing its arguments to produce an outcome.
 * The function's body, which is a lambda, can be evaluated by providing a list of arguments.
 * The function can also reduce or simplify sub-expressions present within its body, recursively
 * handling other `Reduceable` instances such as functions or terminals.
 *
 * The class also provides a clear `toString` representation by simply returning the name of the function.
 *
 * @param T The generic type representing the return type of the function.
 *
 * @property name The unique identifier or name of the function.
 * @property arity Specifies the number of arguments the function can take.
 * @property body A lambda that provides the computational logic of the function. It takes a list
 *                of arguments and returns a result of type T.
 *
 * @see Terminal This could be another related class or type that the user should be aware of,
 *               providing more context.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
open class Fun<T>(
    private val name: String,
    override val arity: Int,
    private val body: (List<T>) -> T
) : Reduceable<T>, Intermediate<Reduceable<T>> {

    /**
     * Reduces the arguments using the body of the function to produce a result.
     * This method is an override from `Reduceable` interface.
     *
     * @param args A list of arguments to be processed by the function.
     * @return The result after processing the arguments.
     */
    override fun invoke(args: List<T>) = body(args)

    /**
     * An overloaded version of the invoke method that accepts variable arguments.
     * The arguments are converted to a list and then processed.
     *
     * @param args Variable arguments to be processed by the function.
     * @return The result after processing the arguments.
     */
    override fun invoke(vararg args: T): T = body(args.toList())

    /**
     * Provides a string representation of the function, which is simply its name.
     *
     * @return The name of the function.
     */
    override fun toString() = name
}

