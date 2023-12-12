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
 * A program consists of a [Reducible] expression as the node value and may have child programs
 * that represent the arguments to the expression. This recursive structure allows for the representation
 * of complex expressions and computational procedures.
 *
 * @param V The type of the value that the program operates on.
 * @param reducible The [Reducible] expression that the program node represents.
 * @param children Optional list of child [Program] instances that are the arguments to the [reducible] expression.
 *
 * @constructor Creates a new [Program] instance with the provided [reducible] expression and optional [children].
 *
 * @property root The root [Program] of the tree structure. It provides access to the top-level program node.
 * @property arity The arity (number of arguments) of the [reducible] expression, derived from its structure.
 * @property node An alias for the [reducible] value, representing the current program node.
 * @property nodes A depth-first list of all program nodes in the tree, starting from the current node.
 * @property root An alias for the first node in the [nodes] list.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Program<V>(
    val reducible: Reducible<V>,
    override val children: List<Program<V>> = emptyList()
) : Tree<Reducible<V>, Program<V>>, Copyable<Program<V>> {

    init {
        constraints {
            "The arity of the reduceable expression must be equal to the number of children" {
                reducible.arity must BeEqualTo(children.size)
            }
        }
    }

    override val arity: Int = reducible.arity
    override val node: Reducible<V> = reducible
    override val nodes: List<Program<V>>
        get() = listOf(this) + children.flatMap { it.nodes }
    val root = nodes.first()

    /**
     * Creates a new instance of the program node with the given value and children.
     * This method is fundamental for the construction of the tree structure.
     *
     * @param value The [Reducible] value to be associated with the new program node.
     * @param children The list of child [Program] instances to be associated with the new program node.
     *
     * @return A new [Program] instance representing the node with its children.
     */
    override fun createNode(value: Reducible<V>, children: List<Program<V>>) = Program(value, children)

    /**
     * Invokes the reduceable expression with the given arguments within a specified environment.
     *
     * @param environment The [Environment] in which the program is to be evaluated.
     * @param args Variable number of arguments of type [V] to be passed to the [reducible] expression.
     *
     * @return The result of evaluating the reduceable expression with the provided arguments.
     */
    operator fun invoke(environment: Environment<V>, vararg args: V): V =
        reducible.invoke(environment, children.map { it(environment, *args) })

    /**
     * Creates a copy of this [Program] instance, duplicating its structure and values.
     *
     * @return A new [Program] instance that is a copy of this instance.
     */
    override fun copy(): Program<V> = Program(reducible, children.map { it.copy() })

    override fun toSimpleString() = if (children.isEmpty()) {
        reducible.toString()
    } else {
        "(${reducible.toSimpleString()} ${children.joinToString(" ") { it.toString() }})"
    }

    override fun toString() = "Program(reduceable=$reducible, children=$children)"

    override fun toDetailedString(): String {
        val builder = StringBuilder()
        builder.append("Program(reduceable=$reducible, children=[\n")
        children.forEach { builder.append("\t${it.toDetailedString()}\n") }
        builder.append("])")
        return builder.toString()
    }

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Program<*> -> false
        else -> reducible == other.reducible && children == other.children
    }
}

