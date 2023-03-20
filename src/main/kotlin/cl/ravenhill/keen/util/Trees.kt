package cl.ravenhill.keen.util

import cl.ravenhill.keen.prog.Reduceable


/**
 * This file contains a set of classes and functions that are used to represent trees.
 */

/**
 * Generic tree data structure.
 *
 * @param T The type of the value stored in the tree.
 * @property size The number of nodes in the tree.
 * @property children The children of the tree.
 */
interface Tree<T> {
    val size: Int
    val children: List<T>
}

/**
 * A tree data structure that uses a list to store its nodes.
 */
interface ListTree<T> : Tree<T>