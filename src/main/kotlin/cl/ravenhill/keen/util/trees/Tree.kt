/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.trees

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.collections.BeEmpty
import cl.ravenhill.enforcer.requirements.collections.HaveElement
import cl.ravenhill.enforcer.requirements.collections.HaveSize
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.MultiStringFormat
import cl.ravenhill.keen.util.SelfReferential

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
        get() = nodes.subList(1, nodes.size)

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
     * Converts the tree to a simplified string representation.
     *
     * This function provides a pretty-printed representation of the tree. Each level of the tree
     * is indented by two spaces. The nodes are represented by their values, and child nodes are
     * encapsulated within curly braces.
     *
     * For example, a tree with value "a" having two children with values "b" and "c", where "c" has a
     * child with value "d", will be represented as:
     * ```
     * a {
     *   b
     *   c {
     *     d
     *   }
     * }
     * ```
     *
     * @return A string representation of the tree.
     */
    override fun toSimpleString(): String {
        fun stringify(child: Tree<V, T>, indent: Int = 0): String {
            // Create a StringBuilder for constructing the string representation
            val builder = StringBuilder()

            // Append the value of the current node
            builder.append(" ".repeat(indent)) // This adds spaces for indentation
            builder.append(value.toString())

            if (children.isNotEmpty()) {
                // Append child nodes
                builder.append(" {\n") // Opening brace for child nodes
                for (c in children) {
                    builder.append(stringify(c, indent + 2)) // Recurse into child with an increased indentation
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
    override fun toFullString(): String {
        fun subtreeToString(node: Tree<V, T>, visited: MutableSet<Tree<V, T>>): String {
            enforce {
                "The tree contains cycles." { visited mustNot HaveElement(node) }
            }
            // Check for cyclic references.
            if (visited.contains(node)) {
                return "CYCLIC_REFERENCE"
            }

            visited.add(node)

            val childrenStr = node.children.joinToString(", ") { child -> subtreeToString(child, visited) }
            return "Tree(value=${node.value}, children=[$childrenStr])"
        }

        return subtreeToString(this, mutableSetOf())
    }

    /**
     * Companion object for the [Tree] interface, empty on purpose.
     * This is used to provide static-like access to the [Tree] interface to be available to
     * create extension functions.
     */
    companion object
}
