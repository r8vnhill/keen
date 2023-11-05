/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.trees

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.collections.BeEmpty
import cl.ravenhill.enforcer.requirements.collections.HaveSize
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.MultiStringFormat
import cl.ravenhill.keen.util.SelfReferential

/**
 * Represents a generic tree data structure in which each node is organized in a depth-first list.
 *
 * This interface is designed for representing hierarchical structures and offers methods for
 * tree manipulations like subtree replacement and conversion to string in multiple formats.
 *
 * ### Example
 * ```kotlin
 * data class MyTree<V>(
 *     val node: Node<V>,
 *     override val children: List<MyTree<V>> = emptyList()
 * ) : Tree<Node<V>, MyTree<V>> {
 *
 *     override val arity: Int = node.arity
 *
 *     override val value = node
 *
 *     override fun createNode(value: Node<V>, children: List<MyTree<V>>) =
 *         MyTree(value, children)
 *
 *     override val nodes: List<MyTree<V>>
 *         get() = listOf(this) + children.flatMap { it.nodes }
 * }
 *
 * val leafNodeD = MyTree(MyLeaf('d'))
 * val intermediateNodeB = MyTree(MyIntermediate(1, 'b'), listOf(leafNodeD))
 * val leafNodeC = MyTree(MyLeaf('c'))
 * val singleElementTree = MyTree(MyLeaf('a'))
 * val multiElementTree = MyTree(MyIntermediate(2, 'a'), listOf(intermediateNodeB, leafNodeC))
 * ```
 *
 * @param V The type of the value stored in the tree node.
 * @param T The type of the tree itself.
 *
 * @property value The value held in the root node.
 * @property size Total count of nodes within the tree.
 * @property nodes List of all nodes in depth-first order.
 * @property children Immediate child nodes of the root node.
 * @property arity Count of immediate children the root node has.
 * @property descendants All nodes excluding the root, in depth-first order.
 * @property height The height of this node in the tree. Defined as the greatest distance
 *                  from this node to any of its descendant leaf nodes. A node without children has a height of zero.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Tree<V, T> : SelfReferential<T>, MultiStringFormat where T : Tree<V, T> {
    val value: V
    val children: List<T>
    val nodes: List<T>
    val arity: Int
    val height: Int
        get() = if (children.isEmpty()) 0 else children.maxOf { it.height } + 1
    val size: Int
        get() = nodes.size
    val descendants: List<T>
        get() = nodes.drop(1)

    /**
     * Returns a random node from the tree using the given [random] number generator.
     */
    fun random() = nodes.random(Core.random)

    /**
     * Returns an [IntRange] that corresponds to the indices of the subtree rooted at the specified
     * [node].
     *
     * @throws NoSuchElementException if the specified [node] is not found in the tree.
     */
    fun searchSubtree(node: T): IntRange {
        // Find the index of the specified node
        val index = nodes.indexOfFirst { it === node }
        // If the specified node is not found, throw a NoSuchElementException
        if (index == -1) {
            throw NoSuchElementException("Node not found in tree.")
        }
        // Return an IntRange representing the indices of the subtree rooted at the specified node
        return index..index + nodes[index].size
    }

    /**
     * Returns a new tree with the specified ``node`` replacing the subtree rooted at the given
     * ``range``.
     */
    fun replaceSubtree(range: IntRange, node: T): T {
        val newNodes = mutableListOf<T>().apply {
            addAll(nodes.subList(0, range.first))
            addAll(node.nodes)
            addAll(nodes.subList(range.last, nodes.size))
        }
        return fromDepthFirst(newNodes)
    }

    fun replaceSubtree(original: T, replacement: T): T {
        val range = searchSubtree(original)
        val newNodes = mutableListOf<T>().apply {
            addAll(nodes.subList(0, range.first))
            addAll(replacement.nodes)
            addAll(nodes.subList(range.last, nodes.size))
        }
        return fromDepthFirst(newNodes)
    }

    /**
     * Creates a new tree from the given `nodes` in depth-first order.
     */
    fun fromDepthFirst(nodes: List<T>): T {
        enforce { "Cannot create a tree from an empty list of nodes." { nodes mustNot BeEmpty } }
        // Create an empty stack to hold the nodes.
        val stack = mutableListOf<T>()
        // Traverse the list of nodes in reverse order.
        nodes.reversed().forEach {
            // Take the required number of children from the top of the stack.
            val children = stack.take(it.arity)
            // Remove the children from the stack.
            stack.removeAll(children.toSet())
            // Create a new node with the given value and children.
            val node = createNode(it.value, children)
            enforce {
                "The arity of the tree [${it.arity}] does not match the arity of the node [${node.children.size}]." {
                    node.children must HaveSize(it.arity)
                }
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

    /**
     * Transforms the tree into a concise string representation.
     *
     * The output is a neatly formatted representation of the tree structure, where each depth
     * level is indented by two spaces. Nodes are denoted by their values, and child nodes are
     * enclosed within curly braces.
     *
     * __Important:__ This function does not handle infinite recursion.
     *
     * ### Examples
     *
     * #### Single element tree
     * ```kotlin
     * singleElementTree.toSimpleString()
     * ```
     * Output:
     * ```
     * MyLeaf(value=a)
     * ```
     *
     * #### Multi-element tree
     * ```kotlin
     * multiElementTree.toSimpleString()
     * ```
     * Output:
     * ```
     * MyIntermediate(arity=2, value=a) {
     *   MyIntermediate(arity=1, value=b) {
     *     MyLeaf(value=d)
     *   }
     *   MyLeaf(value=c)
     * }
     * ```
     *
     * @return A neatly formatted string portraying the tree.
     */
    override fun toSimpleString(): String {
        fun stringify(child: Tree<V, T>, indent: Int = 0, visited: MutableSet<Tree<V, T>> = mutableSetOf()): String {
            // Create a StringBuilder for constructing the string representation
            val builder = StringBuilder()

            // Append the value of the current node
            builder.append(" ".repeat(indent)) // This adds spaces for indentation
            builder.append(child.value.toString())
            visited.add(child)

            if (child.children.isNotEmpty()) {
                // Append child nodes
                builder.append(" {\n") // Opening brace for child nodes
                for (c in child.children) {
                    // Recurse into child with an increased indentation
                    builder.append(stringify(c, indent + 2, visited = visited))
                    builder.append("\n") // Newline after each child
                }
                builder.append(" ".repeat(indent)) // Spaces for closing brace indentation
                builder.append("}") // Closing brace for child nodes
            }

            return builder.toString()
        }
        return stringify(this)
    }

    /**
     * Converts the tree to a detailed string representation.
     *
     * This function returns a representation of the tree that provides more detailed information
     * about each node. It includes attributes such as value, arity, height, and size of the tree.
     * Additionally, it recursively lists all nodes in the tree.
     *
     * For example, for a simple tree, the representation might look something like:
     * "Tree(value=a, arity=2, height=2, size=3, nodes=[...])"
     *
     * @return A detailed string representation of the tree.
     */
    override fun toDetailedString(): String {
        return "${this::class.simpleName}(" +
              "value=$value, size=$size, arity=$arity, height=$height, children=$children, descendants=$descendants)"
    }

    /**
     * Companion object intended for the [Tree] interface.
     *
     * Presently unoccupied, this placeholder allows for future static-like access or
     * extension functions for the [Tree] interface.
     */
    companion object
}
