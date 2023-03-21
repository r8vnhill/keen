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
 * @property arity The number of children of the root node.
 * @property descendants The descendants of the tree in a breadth-first order.
 * @property height The height of the program tree, which is the depth of the deepest node.
 */
interface Tree<T> {
    val size: Int
    val children: List<Tree<T>>
    val arity: Int
    val height: Int
    val descendants: List<Tree<T>>
        get() = children.fold(children) { acc, tree -> acc + tree.descendants }
}

/**
 * A tree data structure that uses a list to store its nodes.
 */
interface ListTree<T> : Tree<T> {
    val nodes: List<Tree<T>>
}