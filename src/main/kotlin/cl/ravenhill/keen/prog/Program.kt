/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeEqualTo
import cl.ravenhill.keen.util.Copyable
import cl.ravenhill.keen.util.trees.Tree

/**
 * Defines a program structure in a tree-like form for genetic programming or symbolic regression.
 *
 * A program consists of a [Reduceable] expression as the node value and may have child programs
 * that represent the arguments to the expression. This recursive structure allows for the representation
 * of complex expressions and computational procedures.
 *
 * @param V The type of the value that the program operates on.
 * @param reduceable The [Reduceable] expression that the program node represents.
 * @param children Optional list of child [Program] instances that are the arguments to the [reduceable] expression.
 *
 * @constructor Creates a new [Program] instance with the provided [reduceable] expression and optional [children].
 *
 * @property root The root [Program] of the tree structure. It provides access to the top-level program node.
 * @property arity The arity (number of arguments) of the [reduceable] expression, derived from its structure.
 * @property node An alias for the [reduceable] value, representing the current program node.
 * @property nodes A depth-first list of all program nodes in the tree, starting from the current node.
 * @property root An alias for the first node in the [nodes] list.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Program<V>(
    val reduceable: Reduceable<V>,
    override val children: List<Program<V>> = emptyList()
) : Tree<Reduceable<V>, Program<V>>, Copyable<Program<V>> {

    init {
        constraints {
            "The arity of the reduceable expression must be equal to the number of children" {
                reduceable.arity must BeEqualTo(children.size)
            }
        }
    }

    override val arity: Int = reduceable.arity
    override val node: Reduceable<V> = reduceable
    override val nodes: List<Program<V>>
        get() = listOf(this) + children.flatMap { it.nodes }
    val root = nodes.first()

    /**
     * Creates a new instance of the program node with the given value and children.
     * This method is fundamental for the construction of the tree structure.
     *
     * @param value The [Reduceable] value to be associated with the new program node.
     * @param children The list of child [Program] instances to be associated with the new program node.
     *
     * @return A new [Program] instance representing the node with its children.
     */
    override fun createNode(value: Reduceable<V>, children: List<Program<V>>) = Program(value, children)

    /**
     * Invokes the reduceable expression with the given arguments within a specified environment.
     *
     * @param environment The [Environment] in which the program is to be evaluated.
     * @param args Variable number of arguments of type [V] to be passed to the [reduceable] expression.
     *
     * @return The result of evaluating the reduceable expression with the provided arguments.
     */
    operator fun invoke(environment: Environment<V>, vararg args: V): V =
        reduceable.invoke(environment, children.map { it(environment, *args) })

    /**
     * Creates a copy of this [Program] instance, duplicating its structure and values.
     *
     * @return A new [Program] instance that is a copy of this instance.
     */
    override fun copy(): Program<V> = Program(reduceable, children.map { it.copy() })

    // Overrides the default toString method to provide a simple string representation of the program.
    // If the program has no children, it simply returns the string representation of the reduceable.
    // Otherwise, it constructs a string with the reduceable and its children.
//    override fun toString() =
//        if (children.isEmpty()) reduceable.toString() else "($reduceable(${children.joinToString(", ") { it.toString() }}))"
}

