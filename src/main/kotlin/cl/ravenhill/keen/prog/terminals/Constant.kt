/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Environment

/**
 * A terminal node representing a constant value in a program tree.
 *
 * This node holds a constant value of type T and returns that value when invoked.
 * It has zero arity, i.e., it does not have any child nodes.
 *
 * @param T the type of the constant value this node holds
 * @property value the constant value this node holds.
 * @constructor creates a new constant node with the given value
 */
data class Constant<T>(val value: T) : Terminal<T> {
    /**
     * Creates a new `Constant` node with the same value as this node.
     *
     * @return a new `Constant` node with the same value as this node
     */
    override fun create() = Constant(value)
    override fun invoke(environment: Environment<T>, args: List<T>): T = value
}
