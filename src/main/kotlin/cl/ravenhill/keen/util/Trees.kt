package cl.ravenhill.keen.util

import cl.ravenhill.keen.prog.Reduceable
import kotlin.random.Random


/**
 * This file contains a set of classes and functions that are used to represent trees.
 */

/**
 * Generic tree data structure where each node is stored as a breadth-first list.
 *
 * @param T The type of the value stored in the tree.
 * @property size The number of nodes in the tree.
 * @property nodes The nodes of the tree in a breadth-first order.
 * @property children The children of the tree.
 * @property arity The number of children of the root node.
 * @property descendants The descendants of the tree in a breadth-first order.
 * @property height The height of the program tree, which is the depth of the deepest node.
 */
interface Tree<T> {
    val children: List<Tree<T>>
    val nodes: List<Tree<T>>
    val arity: Int
    val height: Int
    val size: Int
        get() = nodes.size
    val descendants: List<Tree<T>>
        get() = nodes.subList(1, nodes.size)

    /**
     * Returns a random node from the tree using the given [random] number generator.
     */
    fun random(random: Random = Random.Default) = nodes.random(random)
}

/**
 * A tree data structure that uses a list to store its nodes.
 */
interface ListTree<T> : Tree<T> {
}