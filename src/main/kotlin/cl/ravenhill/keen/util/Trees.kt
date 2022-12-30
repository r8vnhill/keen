package cl.ravenhill.keen.util


/**
 * This file contains a set of classes and functions that are used to represent trees.
 */

/**
 * Generic tree data structure.
 *
 * @param T The type of the value stored in the tree.
 * @property depth The depth of this node relative to the root.
 * @property size The number of nodes in the tree.
 */
interface Tree<T> {
    val depth: Int
    val size: Int
    val children: List<T>
}

/**
 * A tree data structure that uses a list to store its nodes.
 */
interface ListTree<T: Any> : Tree<T> {
    override val size: Int
        get() = children.size + 1
}