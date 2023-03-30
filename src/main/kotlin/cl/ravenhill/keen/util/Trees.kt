package cl.ravenhill.keen.util

import kotlin.random.Random

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
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
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

    /**
     * Returns an [IntRange] that corresponds to the indices of the subtree rooted at the specified
     * [node1].
     *
     * @throws NoSuchElementException if the specified [node1] is not found in the tree.
     */
    fun searchSubtree(node1: Tree<out T?>): IntRange {
        // Find the index of the specified node
        val index = nodes.indexOfFirst { it === node1 }
        // If the specified node is not found, throw a NoSuchElementException
        if (index == -1) {
            throw NoSuchElementException("Node not found in tree.")
        }
        // Return an IntRange representing the indices of the subtree rooted at the specified node
        return index..index + nodes[index].size
    }

    fun replaceSubtree(range: IntRange, node: Tree<T>): Tree<T> {
        val newNodes = mutableListOf<Tree<T>>().apply {
            addAll(nodes.subList(0, range.first))
            addAll(node.nodes)
            addAll(nodes.subList(range.last, nodes.size))
        }
        return fromBreadthFirst(newNodes)
    }

    fun fromBreadthFirst(nodes: List<Tree<T>>): Tree<T>
}
