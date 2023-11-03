package cl.ravenhill.keen.prog

import cl.ravenhill.keen.util.trees.Node

/**
 * Represents an operation that can be reduced down to a single value.
 * The `Reduceable` interface extends the `Node` interface, where each node represents
 * an instance of a `Reduceable` operation. The concept of reduction here refers to
 * taking multiple values (or child operations) and condensing them into a single outcome.
 *
 * @param T The type of the value or outcome the operation works upon or produces.
 *
 * @property arity The number of child operations (or arguments) this operation can take.
 *           This is an essential property that helps in defining the nature of the operation
 *           and how it processes its child operations or arguments.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Reduceable<T> : Node<Reduceable<T>> {

    /**
     * Reduces the operation to produce a single value based on the provided arguments.
     * This method uses a list to accept multiple arguments for the operation.
     *
     * @param args A list of arguments that the operation will use to produce the outcome.
     * @return The single result or outcome of the operation.
     */
    operator fun invoke(args: List<T>): T

    /**
     * An overloaded version of the `invoke` method that can directly take variable arguments.
     * It simplifies the process when the number of arguments is known.
     *
     * @param args Variable number of arguments that the operation will use to produce the outcome.
     * @return The single result or outcome of the operation.
     */
    operator fun invoke(vararg args: T): T = invoke(args.toList())
}

