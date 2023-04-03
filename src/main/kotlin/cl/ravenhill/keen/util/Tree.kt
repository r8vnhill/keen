package cl.ravenhill.keen.util

import kotlin.random.Random

/**
 * Generic tree data structure where each node is stored as a depth-first list.
 *
 * @param V The type of the value stored in the tree.
 * @property size The number of nodes in the tree.
 * @property nodes The nodes of the tree in a depth-first order.
 * @property children The children of the tree.
 * @property arity The number of children of the root node.
 * @property descendants The descendants of the tree in a breadth-first order.
 * @property height The height of this node in the program tree.
 *
 *  The height of a node is defined as the maximum distance from the node to any leaf node in
 *  the subtree rooted at the node.
 *  If the node has no children, its height is zero.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Tree<V, T: Tree<V, T>> : SelfReferential<T> {
    val children: List<T>
    val nodes: List<T>
    val arity: Int
    val height: Int
        get() = if (children.isEmpty()) 0 else children.maxOf { it.height } + 1
    val size: Int
        get() = nodes.size
    val descendants: List<T>
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
    fun searchSubtree(node1: T): IntRange {
        // Find the index of the specified node
        val index = nodes.indexOfFirst { it === node1 }
        // If the specified node is not found, throw a NoSuchElementException
        if (index == -1) {
            throw NoSuchElementException("Node not found in tree.")
        }
        // Return an IntRange representing the indices of the subtree rooted at the specified node
        return index..index + nodes[index].size
    }

    /**
     * Returns a new tree with the specified ``node`` replacing the subtree rooted at the given
     * ``range``].
     */
    fun replaceSubtree(range: IntRange, node: T): T {
        val newNodes = mutableListOf<T>().apply {
            addAll(nodes.subList(0, range.first))
            addAll(node.nodes)
            addAll(nodes.subList(range.last, nodes.size))
        }
        return fromDepthFirst(newNodes)
    }

    /**
     * Creates a new tree from the given `nodes` in depth-first order.
     */
    fun fromDepthFirst(nodes: List<T>): T
}
