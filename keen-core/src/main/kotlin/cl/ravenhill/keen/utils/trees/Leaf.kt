package cl.ravenhill.keen.utils.trees

import cl.ravenhill.keen.annotations.ExperimentalKeen

/**
 * Represents a leaf node in a data structure, typically used in contexts like trees or hierarchical structures.
 * Being a leaf node, it does not have any child nodes, indicated by its arity being zero. This interface is part
 * of the Keen library and is currently marked as experimental.
 *
 * ## Characteristics:
 * - As a `Leaf`, it extends the `Node<T>` interface, making it a specific type of node with specialized behavior.
 * - The `arity` property, which represents the number of child nodes, is always zero for a `Leaf`, distinguishing it
 *   from other node types that can have children.
 *
 * ## Usage:
 * `Leaf` nodes are typically used at the terminal points of a tree or a similar hierarchical data structure where
 * nodes do not need to reference other nodes. They are useful in scenarios where you need to represent the end
 * of a path or a data point that does not contain further subdivisions.
 *
 * ### Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * class MyLeaf<T>(override val contents: T) : Leaf<T> {
 *     // Additional properties or methods specific to MyLeaf
 * }
 *
 * val leafNode = MyLeaf("Leaf data")
 * println(leafNode.arity) // Outputs: 0
 * ```
 *
 * @param T the type of the contents held within the leaf node.
 * @property arity The number of child nodes associated with this leaf node, which is always 0 for leaf nodes.
 * @see ExperimentalKeen for information on the experimental status of this interface.
 */
@ExperimentalKeen
interface Leaf<T> : Node<T> {
    override val arity: Int get() = 0
}
