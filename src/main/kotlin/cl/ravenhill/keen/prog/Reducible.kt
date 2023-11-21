/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog

import cl.ravenhill.keen.util.trees.Node

/**
 * Represents an operation that can be condensed or simplified to produce a single outcome. This
 * operation works on instances of type `T` and may involve multiple input values or sub-operations.
 *
 * A `Reducible` can be visualized as a node in a tree, with the potential of having child nodes.
 * Each child node (or operation) can be recursively reduced until a final value for the root node
 * is determined. The concept is somewhat similar to how functions in mathematics can be
 * "reduced" to simpler forms or values.
 *
 * @param T The type of the value that the operation works with and produces. This type parameter
 *          represents both the input values and the result of the operation.
 *
 * @property arity Specifies the number of sub-operations or input values that this operation
 *                 can handle. This property defines how the operation behaves with respect to
 *                 its inputs, determining its complexity and nature.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Reducible<T> : Node<Reducible<T>> {
    override val contents: Reducible<T>
        get() = this

    /**
     * An overloaded version of the `invoke` method that allows the operation to be
     * evaluated within a specific environment. This enables the operation to access
     * or modify variables defined within the given environment.
     *
     * @param environment The context or environment in which the operation is evaluated.
     * @return The result of the operation after evaluating within the environment.
     */
    operator fun invoke(environment: Environment<T>, args: List<T>): T

    operator fun invoke(environment: Environment<T>, vararg args: T): T = invoke(environment, args.toList())
}

