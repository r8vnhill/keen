package cl.ravenhill.keen.utils.trees

import cl.ravenhill.keen.annotations.ExperimentalKeen

/**
 * Represents an intermediate node within a hierarchical or computational structure. As an implementation of the
 * `Node` interface, `Intermediate` nodes typically serve as connectors or processing points between the root and
 * leaf nodes in a structure. This interface is annotated as experimental in the Keen library, indicating that it
 * is intended for advanced computational models and may be subject to changes in future versions.
 *
 * ## Characteristics:
 * - Implements the `Node<T>` interface, indicating that it can hold or process a value of type `T`.
 * - `Intermediate` nodes are often used in structures like trees or graphs where they play a role in linking
 *   different parts of the structure or in processing data as it flows through the structure.
 *
 * ## Usage:
 * The `Intermediate` interface is useful in scenarios such as computational trees, graphs, or other data structures
 * where intermediate processing or decision points are needed. They are crucial in complex algorithms that require
 * multiple stages of computation or decision-making.
 *
 * ### Example Usage:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * class MyIntermediateNode<T> : Intermediate<T> {
 *     // Implementation details for the intermediate node
 * }
 *
 * // Using MyIntermediateNode in a computational structure
 * val intermediateNode = MyIntermediateNode<String>()
 * // Additional implementation or usage scenarios
 * ```
 *
 * @param T the type of the value that the intermediate node holds or processes.
 * @see Node for the base interface details.
 * @see ExperimentalKeen for information regarding the experimental status of this interface.
 */
@ExperimentalKeen
interface Intermediate<T> : Node<T>
