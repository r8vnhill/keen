/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.trees

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
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
 * @property node The value held in the root node.
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
    val node: V
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
     * Searches for the first node in the tree that satisfies the given [predicate] and
     * returns an [IntRange] representing the indices of the subtree rooted at that node.
     *
     * The function will traverse the tree in a depth-first order and stop at the first
     * node for which the [predicate] returns `true`. The returned range starts from
     * the index of the node in the depth-first list of nodes of the tree and goes up
     * to the last node in the subtree rooted at that node.
     *
     * ### Example:
     * Given a tree as a (top-down) depth-first list of nodes: [`A`, `B`, `D`, `C`]
     * ```
     * (0) A
     * (1) ├── B
     * (2) │   └── D
     * (3) └── C
     * ```
     * If the predicate is looking for node `B`, the returned range will cover the nodes `B` and `D` (indices 1 and 2).
     *
     * @param predicate A lambda function that takes in a tree node and returns a boolean value.
     *                  It determines the condition based on which a node should be found.
     *
     * @return An [IntRange] representing the indices of the subtree rooted at the node that
     *         satisfies the predicate. The range starts from the index of the node in the
     *         depth-first list of nodes of the tree and goes up to the last node in the subtree
     *         rooted at that node.
     *
     * @throws NoSuchElementException If no node in the tree satisfies the given predicate.
     */
    fun indexOfFirst(predicate: (T) -> Boolean): IntRange {
        val index = nodes.indexOfFirst(predicate)
        if (index == -1) {
            throw NoSuchElementException("Node not found in tree")
        }
        return index..<index + nodes[index].size
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
        return fromTopDown(newNodes)
    }

    /**
     * Replaces the first subtree in the current tree that satisfies the given [predicate] with a new subtree
     * [replacement].
     *
     * This function searches for the first node that satisfies the provided [predicate]. Upon finding this node, it
     * replaces the subtree rooted at this node with the [replacement] subtree.
     *
     * The process involves the following steps:
     * 1. Search for the first node that matches the [predicate].
     * 2. Identify the range of nodes that make up the subtree rooted at the found node.
     * 3. Construct a new list of nodes by combining:
     *     - Nodes before the identified subtree.
     *     - Nodes from the replacement subtree.
     *     - Nodes after the identified subtree.
     * 4. Reconstruct the tree using the newly formed list of nodes.
     *
     * ### Example:
     * Suppose we have a tree represented as a (top-down) depth-first list of nodes: [`A`, `B`, `D`, `C`]
     * and a replacement subtree represented by: [`X`, `Y`].
     *
     * Invoking this function with a predicate that matches node `B` will replace the subtree rooted at `B`
     * with the `X` subtree, resulting in a new tree: [`A`, `X`, `Y`, `C`].
     *
     * @param replacement The subtree to replace the identified subtree with. This subtree is described by its root node
     *        [T].
     * @param predicate A lambda function that determines the condition based on which a node in the current tree should
     *        be matched.
     *
     * @return The root node of the modified tree after the replacement.
     *
     * @throws NoSuchElementException If no node in the tree satisfies the given predicate.
     */
    @Throws(NoSuchElementException::class)
    fun replaceFirst(replacement: T, predicate: (T) -> Boolean): T {
        val range = indexOfFirst(predicate)
        val newNodes = mutableListOf<T>().apply {
            addAll(nodes.subList(0, range.first))
            addAll(replacement.nodes)
            addAll(nodes.subList(range.last + 1, nodes.size))
        }
        return fromTopDown(newNodes)
    }

    /**
     * Constructs a tree from a given list of nodes using a depth-first order.
     *
     * This function constructs a tree by treating the provided list of nodes as a depth-first traversal of the desired
     * tree. Starting from the end of the list, each node is popped and becomes a parent to the previous nodes based on
     * its arity (number of children).
     *
     * ### Process:
     *
     * 1. Traverse the list of nodes in reverse order.
     * 2. For each node, retrieve its arity and get the corresponding number of child nodes from the stack.
     * 3. Create a new node with the extracted children.
     * 4. Push the new node onto the stack.
     *
     * After processing all nodes, the top of the stack will be the root node of the resulting tree.
     *
     * ### Constraints:
     * - The list of nodes should not be empty.
     * - The arity of each node in the list must be consistent with the number of available child nodes in the stack at
     *   each step.
     *
     * ### Example:
     * Given a list of nodes in depth-first order: [`A`, `B`, `D`, `C`]
     * ```
     * val nodes = listOf(
     *    MyNode(2, 'A'),
     *    MyNode(1, 'B'),
     *    MyNode(0, 'D'),
     *    MyNode(0, 'C')
     * )
     * singleElementTree.fromDepthFirst(nodes)
     * ```
     * The resulting tree will be:
     * ```
     * (0) A
     * (1) ├── B
     * (2) │   └── D
     * (3) └── C
     * ```
     *
     * @param nodes A list of nodes in depth-first order from which the tree should be constructed.
     *
     * @return The root node of the constructed tree.
     *
     * @throws CompositeException If the list of nodes is empty or if there is a mismatch between the node's arity and
     *                               the actual number of children available.
     */
    fun fromTopDown(nodes: List<T>): T {
        constraints { "Cannot create a tree from an empty list of nodes." { nodes mustNot BeEmpty } }
        // Create an empty stack to hold the nodes.
        val stack = mutableListOf<T>()
        // Traverse the list of nodes in reverse order.
        nodes.reversed().forEach {
            // Take the required number of children from the top of the stack.
            val children = stack.take(it.arity)
            // Remove the children from the stack.
            stack.removeAll(children.toSet())
            // Create a new node with the given value and children.
            val node = createNode(it.node, children)
            constraints {
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
            builder.append(child.node.toString())
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
     * ### Examples
     *
     * #### Single element tree
     *
     * ```kotlin
     * singleElementTree.toDetailedString()
     * ```
     *
     * Output:
     * ```
     * MyTree(value=MyLeaf(value=a), size=1, arity=0, height=0, children=[], descendants=[])
     * ```
     *
     * #### Multi-element tree
     *
     * ```kotlin
     * multiElementTree.toDetailedString()
     * ```
     *
     * Output:
     * ```
     * MyTree(value=MyIntermediate(arity=2, value=a), size=4, arity=2, height=2, children=[MyTree(node=MyIntermediate(arity=1, value=b), children=[MyTree(node=MyLeaf(value=d), children=[])]), MyTree(node=MyLeaf(value=c), children=[])], descendants=[MyTree(node=MyIntermediate(arity=1, value=b), children=[MyTree(node=MyLeaf(value=d), children=[])]), MyTree(node=MyLeaf(value=d), children=[]), MyTree(node=MyLeaf(value=c), children=[])])
     *
     * @return A detailed string representation of the tree.
     */
    override fun toDetailedString() = "${this::class.simpleName}(" +
          "value=$node, size=$size, arity=$arity, height=$height, children=$children, descendants=$descendants)"

    /**
     * Companion object intended for the [Tree] interface.
     *
     * Presently unoccupied, this placeholder allows for future static-like access or
     * extension functions for the [Tree] interface.
     */
    companion object
}
