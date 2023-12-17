/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils.trees

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeEqualTo
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.mixins.MultiStringFormat
import cl.ravenhill.keen.mixins.SelfReferential


/**
 * Represents a generic tree structure in computational models. This interface extends `SelfReferential<T>` and
 * `Collection<T>`, and implements `MultiStringFormat`. It provides a comprehensive framework for trees where
 * each node can be a tree itself, allowing for nested tree structures. Marked as experimental in the Keen library,
 * this interface is intended for use in advanced computational models and may be subject to changes in future versions.
 *
 * ## Usage:
 * This interface is useful in scenarios involving hierarchical data structures, like expression trees or decision
 * trees. It allows for the creation and manipulation of complex tree structures in a flexible and generic way.
 *
 * ### Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * class MyTree<V>(override val value: V) : Tree<V, MyTree<V>> {
 *     // Implementation details
 * }
 *
 * val tree = MyTree("root")
 * ```
 *
 * @param V The type of value held by each node in the tree.
 * @param T The type of the tree itself, extending the `Tree` interface.
 * @property value The value held by the tree node.
 * @property children A list of child nodes.
 * @property nodes A list of all nodes in the tree, including the tree itself and its descendants.
 * @property arity The number of child nodes.
 * @property height The height of the tree, calculated as the maximum height of its children plus one.
 * @property size The total number of nodes in the tree.
 * @property descendants A list of all descendant nodes, excluding the tree itself.
 * @see SelfReferential for self-referential capabilities.
 * @see ExperimentalKeen for the experimental status of this interface.
 */
@ExperimentalKeen
interface Tree<V, T> : SelfReferential<T>, Iterable<T>, MultiStringFormat where T : Tree<V, T> {

    val value: V

    val children: List<T>

    val nodes: List<T>

    val arity: Int

    val height: Int get() = if (children.isEmpty()) 0 else children.maxOfOrNull { it.height }!! + 1

    val size: Int get() = nodes.size

    val descendants: List<T> get() = drop(1)

    /**
     * Selects and returns a random node from the tree. This function provides a way to randomly access nodes within
     * the tree, which can be useful in various computational scenarios, such as randomized algorithms or simulations.
     *
     * ## Functionality:
     * - Utilizes a random number generator ([Domain.random]) to select a node at random from the list of all nodes
     *   in the tree ([nodes]).
     * - The [nodes] list includes the tree itself and all its descendant nodes.
     * - Returns a randomly selected node of type [T].
     *
     * ## Usage:
     * This function can be used in scenarios where a random element needs to be selected from a tree, such as in
     * genetic algorithms, stochastic processes, or when randomly sampling nodes for analysis or testing.
     *
     * ### Example:
     * ```
     * // Assuming an instance of a Tree with several nodes
     * val tree: Tree<MyValueType, MyTreeType> = ...
     * val randomNode = tree.random()
     * // `randomNode` is a randomly selected node from the tree
     * ```
     *
     * @return A randomly selected node from the tree, of type [T].
     */
    fun random(): T = nodes.random(Domain.random)

    /**
     * Finds the index range of the first node in the tree that satisfies a given predicate, including the indices of
     * the node and all its descendants. This function is useful for identifying and locating a specific subtree within
     * the larger tree structure based on defined criteria.
     *
     * ## Functionality:
     * - Takes a predicate function that defines the condition to be met by the node.
     * - Searches for the first node in the tree's [nodes] list that satisfies the predicate.
     * - If such a node is found, it returns an index range within the [nodes] list, encompassing the found node and all
     *   its descendants. This range indicates the position of the entire subtree within the tree.
     * - Ensures that at least one node in the tree satisfies the predicate.
     *
     * ## Constraints:
     * - The function enforces that the predicate must be true for at least one node in the tree. If no nodes satisfy
     *   the predicate, an exception is thrown.
     *
     * ## Usage:
     * This function is valuable in scenarios where a specific portion of the tree (a subtree) needs to be identified
     * or processed.
     * It can be used for operations such as subtree extraction, analysis, or manipulation based on conditional logic.
     *
     * ### Example:
     * ```
     * val tree: Tree<MyValueType, MyTreeType> = ...
     * val subtreeRange = tree.indexOfFirst { it.value == desiredValue }
     * // subtreeRange now holds the index range of the first node (and its descendants) that matches the condition
     * ```
     *
     * @param predicate A function that takes a node of type [T] and returns a Boolean indicating whether the node
     *   satisfies the condition.
     * @return An [IntRange] indicating the index range of the first node that satisfies the predicate, including the
     *   node itself and all its descendants, within the tree's nodes list.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws IntConstraintException if no node in the tree satisfies the predicate.
     */
    fun indexOfFirst(predicate: (T) -> Boolean): IntRange {
        val index = nodes.indexOfFirst(predicate)
        constraints { "Predicate does not hold for any node in the tree." { index mustNot BeEqualTo(-1) } }
        return index..<index + nodes[index].size
    }

    /**
     * Abstract function to create and return a new node of the tree. This function is a fundamental component of the
     * `Tree` interface, allowing for the construction of tree nodes with specified values and children. Implementers
     * of the `Tree` interface must provide a concrete implementation of this function.
     *
     * ## Functionality:
     * - Constructs a new node of the tree, of type [T].
     * - Takes a value of type [V] and a list of children of type [T] as parameters.
     * - The newly created node should encapsulate the provided value and be linked to the specified children.
     *
     * ## Usage:
     * This function is essential for dynamically building and modifying tree structures. It can be used in various
     * scenarios, such as building a tree from a data source, adding new nodes to an existing tree, or reconstructing
     * a tree after performing operations like node replacement or removal.
     *
     * ### Example Implementation:
     * ```
     * class MyTree<V>(override val value: V, override val children: List<MyTree<V>>) : Tree<V, MyTree<V>> {
     *     override fun createNode(value: V, children: List<MyTree<V>>): MyTree<V> = MyTree(value, children)
     * }
     *
     * // Example of using createNode to add a new child to a tree
     * val parentTree = MyTree("parent", listOf())
     * val childTree = parentTree.createNode("child", listOf())
     * // childTree is now a new MyTree instance with the value "child"
     * ```
     *
     * @param value The value to be stored in the new node.
     * @param children The list of child nodes to be attached to the new node.
     * @return A new instance of type [T], representing the newly created node.
     */
    fun createNode(value: V, children: List<T>): T

    /**
     * Provides an iterator over the nodes of the tree. This method overrides the `iterator` method from the
     * `Collection` interface. It allows for iterating through all nodes in the tree, which includes the tree itself
     * and its descendants, in a linear and sequential manner.
     *
     * ## Usage:
     * This method is essential for scenarios that require sequential access to all nodes within the tree, such as when
     * performing operations like searching, traversing, or applying functions to each node in the tree. It conforms to
     * the standard iterator pattern, allowing the tree to be used in loops and stream-based operations.
     *
     * ### Example:
     * ```
     * val tree: Tree<MyValueType, MyTreeType> = ...
     * for (node in tree) {
     *     // Perform operations on each node
     * }
     * ```
     *
     * @return An iterator that provides sequential access to all nodes in the tree.
     */
    override fun iterator() = nodes.iterator()

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
     * MyTree(value=MyIntermediate(arity=2, value=a), size=4, arity=2, height=2, children=[MyTree(
     * node=MyIntermediate(arity=1, value=b), children=[MyTree(node=MyLeaf(value=d), children=[])]), MyTree(
     * node=MyLeaf(value=c), children=[])], descendants=[MyTree(node=MyIntermediate(arity=1, value=b), children=[
     * MyTree(node=MyLeaf(value=d), children=[])]), MyTree(node=MyLeaf(value=d), children=[]), MyTree(
     * node=MyLeaf(value=c), children=[])])
     * ```
     *
     * @return A detailed string representation of the tree.
     */
    override fun toDetailedString() = "${this::class.simpleName}(" +
          "value=$value, size=$size, arity=$arity, height=$height, children=$children, descendants=$descendants)"

    companion object
}

/**
 * Replaces the first node in the tree that satisfies a given predicate with a specified replacement node. This
 * function is a part of the experimental features in the Keen library and is crucial for modifying tree structures
 * based on conditional logic.
 *
 * ## Functionality:
 * - Searches for the first node in the tree that matches the given predicate.
 * - Replaces this node, along with its entire subtree, with the provided replacement node.
 * - Constructs a new tree with this modified structure and returns it.
 *
 * ## Usage:
 * This function is useful in scenarios where a part of the tree needs to be updated or replaced based on specific
 * conditions. It can be employed in various tree manipulation tasks, such as in genetic programming, tree-based
 * data models, or domain-specific language processing.
 *
 * ### Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * val tree: Tree<MyValueType, MyTreeType> = ...
 * val replacementNode: MyTreeType = ...
 * val updatedTree = tree.replaceFirst(replacementNode) { node ->
 *     // Predicate to determine which node to replace
 *     node.value == someCondition
 * }
 * // updatedTree now contains the tree with the specified node replaced
 * ```
 *
 * @param replacement The node to be used as a replacement.
 * @param predicate A lambda expression that defines the condition to identify the node to be replaced.
 * @return A new instance of the tree with the first node satisfying the predicate replaced by the `replacement` node.
 */
@ExperimentalKeen
fun <V, T> Tree<V, T>.replaceFirst(replacement: T, predicate: (T) -> Boolean): T where T : Tree<V, T> {
    val range = indexOfFirst(predicate)
    val newNodes = mutableListOf<T>().apply {
        addAll(nodes.subList(0, range.first))
        add(replacement)
        addAll(nodes.subList(range.last + 1, nodes.size))
    }
    return fromTopDown(newNodes)
}

/**
 * Constructs a tree from a given list of nodes, using a top-down approach. This function is part of the experimental
 * features in the Keen library and is essential for building tree structures based on a predefined sequence of nodes.
 *
 * ## Functionality:
 * - Accepts a list of nodes as input to construct the tree.
 * - Uses a stack-based approach to assemble the tree, starting from the last node in the provided list and working
 *   upwards to the root.
 * - Each node is created with its value and the required number of children, as determined by its arity, taken from
 *   the top of the stack.
 * - The final tree is returned, with the root node being the last element added to the stack.
 *
 * ## Constraints:
 * - The function enforces that the input list of nodes must not be empty.
 * - It also checks that the arity of each node matches the number of children it is assigned.
 *
 * ## Usage:
 * This method is useful for reconstructing or building trees when a specific node order is given or derived, such as
 * in serialization/deserialization processes, genetic programming, or when applying transformations to trees.
 *
 * ### Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * val nodeList: List<MyTreeType> = ...
 * val reconstructedTree = Tree<MyValueType, MyTreeType>.fromTopDown(nodeList)
 * // reconstructedTree is now a new tree constructed from the nodeList
 * ```
 *
 * @param nodes A list of nodes to be used for constructing the tree.
 * @return A new instance of the tree constructed from the provided list of nodes.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws CollectionConstraintException if the input list of nodes is empty or if the arity of a node does not match
 *   the number of children it is assigned.
 */
@ExperimentalKeen
fun <V, T> Tree<V, T>.fromTopDown(nodes: List<T>): T where T : Tree<V, T> {
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
        val node = createNode(it.value, children)
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
