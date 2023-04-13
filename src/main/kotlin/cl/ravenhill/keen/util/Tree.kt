package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.CollectionRequirement.NotBeEmpty
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import kotlin.random.Random

/**
 * Generic tree data structure where each node is stored as a depth-first list.
 *
 * @param V The type of the value stored in the tree.
 * @param T The type of the tree.
 * @property value The value stored in the root node.
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
interface Tree<V, T : Tree<V, T>> : SelfReferential<T> {
    val value: V
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
    fun fromDepthFirst(nodes: List<T>): T {
        enforce { "Cannot create a tree from an empty list of nodes." { nodes should NotBeEmpty } }
        // Create an empty stack to hold the nodes.
        val stack = mutableListOf<T>()
        // Traverse the list of nodes in reverse order.
        nodes.reversed().forEach {
            // Take the required number of children from the top of the stack.
            val children = stack.take(it.arity)
            // Remove the children from the stack.
            stack.removeAll(children)
            // Create a new node with the given value and children.
            val node = createNode(it.value, children)
            enforce {
                "The arity of the tree [${it.arity}] does not match the arity of the node " +
                        "[${node.children.size}]." { node.children.size should BeEqualTo(it.arity) }
            }
            // Add the new node to the stack.
            stack.add(node)
        }
        // Return the top of the stack.
        return stack.first()
    }

    /**
     * Creates a new node with the given value and children.
     */
    fun createNode(value: V, children: List<T>): T
}
