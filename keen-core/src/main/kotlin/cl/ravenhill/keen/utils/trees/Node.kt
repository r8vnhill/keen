package cl.ravenhill.keen.utils.trees

import cl.ravenhill.keen.annotations.ExperimentalKeen
import cl.ravenhill.keen.mixins.MultiStringFormat

/**
 * Represents a generic node in a data structure, providing a flexible framework for nodes that can contain various
 * types of content and support multiple string formatting options.
 *
 * ## Generic Type:
 * - `T`: The type of content that the node holds. It can be any type, allowing for nodes containing diverse data
 *   types or objects.
 *
 * ## Usage:
 * This interface can be used to create various types of data structures, such as trees, linked lists, or graphs.
 * The flexibility in content type (`T`) and the ability to represent the node as a string make it suitable for both
 * simple and complex data structures.
 *
 * ### Example:
 * ```
 * class TreeNode<T>(override val contents: T, override val arity: Int) : Node<T> {
 *     // Additional properties and methods for TreeNode
 * }
 *
 * val treeNode = TreeNode("Example", 2)
 * println(treeNode.toSimpleString()) // Outputs: "Example"
 * ```
 *
 * @param T the type of the contents held within the node.
 * @property contents An optional property representing the content stored in the node. It is of type `T` and can be
 *   null.
 * @property arity Represents the number of children or connections the node has. This property is essential in data
 *   structures like trees or graphs, where the structure depends on the connections between nodes.
 */
@ExperimentalKeen
interface Node<out T> : MultiStringFormat {
    val contents: T? get() = null
    val arity: Int
    override fun toSimpleString() = "$contents"
}
