/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.util.MultiStringFormat

/***************************************************************************************************
 * This file defines three interfaces: Node<T>, Intermediate<T>, and Leaf<T>, representing
 * different types of nodes in a tree structure.
 * The Node<T> interface represents a general node in the tree and specifies the arity property,
 * which indicates the number of child nodes the node can have.
 * The Intermediate<T> interface extends Node<T> and represents an intermediate node with a
 * specific arity.
 * It also inherits the arity property from Node<T>.
 * The Leaf<T> interface extends Node<T> and represents a leaf node, which has no child nodes and
 * always has an arity of zero.
 * Each interface includes the type parameter T to indicate the type of data contained in the nodes.
 **************************************************************************************************/

/**
 * Represents a generic node in a tree-like data structure, which can be part of a genetic algorithm or other
 * hierarchical systems.
 *
 * A node can be seen as a container that may hold various types of content and can be connected to other nodes,
 * forming a tree structure. The arity represents the number of direct children or connections this node has.
 *
 * @param T The type of content that this node can hold. It's nullable to allow nodes without explicit content.
 *
 * @property arity The fixed number of direct children that this node can have. It defines the structural place of
 *                 the node within the tree. For example, a binary node would have an arity of 2.
 * @property contents The actual content held by the node, which could be any type or `null` if the node does not
 *                    hold content.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Node<T> : MultiStringFormat {

    val arity: Int

    val contents: T?
        get() = null

    /**
     * Provides a simple string representation of the node, typically displaying the contents or a placeholder
     * if the contents are `null`.
     *
     * @return A string representation of the node's content or a default string if the content is `null`.
     */
    override fun toSimpleString() = "$contents"

    /**
     * Provides a detailed string representation of the node, which defaults to the standard `toString` method.
     * This can be overridden for more complex or informative outputs, especially when `contents` alone is not
     * descriptive enough for debugging or logging purposes.
     *
     * @return A string that gives detailed information about the node, including its type, content, and other
     *         properties relevant for understanding the node's role and state within the tree.
     */
    override fun toDetailedString() = toString()
}

/**
 * Represents an intermediate node in a tree structure. Intermediate nodes typically have children and serve
 * as connectors or parents in the hierarchy.
 *
 * The `Intermediate` interface extends the `Node` interface, inheriting its properties and functions. This
 * interface is mainly used to differentiate between node types within a tree, especially when implementing
 * tree traversals or manipulations.
 *
 * @param T The type of content that this node can hold.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Intermediate<T> : Node<T>

/**
 * Represents a leaf node in a tree structure. Leaf nodes are the end points of a tree and do not have any
 * children, which is reflected by their arity being zero.
 *
 * The `Leaf` interface specializes the `Node` interface for the case of terminal nodes in a tree. It is useful
 * for type discrimination, allowing algorithms to recognize and handle leaf nodes specifically when necessary.
 *
 * @param T The type of content that this leaf node can hold.
 *
 * @property arity Always returns 0, signifying that this node type cannot have children.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Leaf<T> : Node<T> {
    override val arity: Int get() = 0
}
