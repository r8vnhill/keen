package cl.ravenhill.keen.util


/**
 * This file contains a set of classes and functions that are used to represent trees.
 */

/**
 * Generic tree data structure.
 *
 * @param T The type of the value stored in the tree.
 */
interface Tree<T> {
    val depth: Int
}

/**
 * A tree data structure that uses a list to store its nodes.
 */
interface ListTree<T: Any> : Tree<T> {
    var children: List<T>
}