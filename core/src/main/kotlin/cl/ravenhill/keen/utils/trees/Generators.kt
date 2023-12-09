/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils.trees

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.jakt.constraints.ints.BeEqualTo
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen


/**
 * Generates a random tree structure within specified height constraints. This function is part of the experimental
 * features in the Keen library and is designed for creating tree structures with a controlled variation in height.
 *
 * ## Functionality:
 * - Creates a tree by randomly selecting leaf ([L]) and intermediate ([I]) nodes based on given lists.
 * - The height of the tree is determined randomly within the specified minimum ([min]) and maximum ([max]) bounds.
 * - A [condition] function is used to guide the decision of creating leaf or intermediate nodes at each level.
 * - Factories provided in [factories] are used to construct the actual leaf and intermediate nodes.
 *
 * ## Constraints:
 * - Ensures that there is at least one leaf node in the provided list.
 * - Validates that both the minimum and maximum heights are positive, and the maximum height is greater than the
 *   minimum height.
 *
 * ## Usage:
 * This method is useful for generating varied tree structures for testing purposes, simulations, or any scenario where
 * random yet controlled tree structures are needed. Particularly useful in genetic programming or tree-based algorithm
 * development.
 *
 * ### Example:
 * ```
 * val tree = Tree.generate(
 *     nodes = listOfLeaves to listOfIntermediates,
 *     min = 2,
 *     max = 5,
 *     condition = { maxHeight, depth -> depth >= maxHeight },
 *     factories = leafFactory to intermediateFactory
 * )
 * // tree is a randomly generated Tree instance with height between 2 and 5
 * ```
 *
 * @param nodes A pair of lists containing leaf nodes (`List<L>`) and intermediate nodes (`List<I>`).
 * @param min The minimum height of the tree to be generated.
 * @param max The maximum height of the tree to be generated.
 * @param condition A function that determines whether to create a leaf or an intermediate node.
 * @param leafFactory A function that takes an [L] object from the list of leafs and returns a new instance of type
 *   [T], representing a leaf node in the tree.
 * @param intermediateFactory A function that takes an [I] object from the list of intermediates, and a list of child
 *   nodes of type [T], and returns a new instance of type [T], representing an intermediate node in the tree.
 * @param V The type of value held by the tree nodes.
 * @param L The leaf node type, extending `Leaf<V>`.
 * @param I The intermediate node type, extending `Intermediate<V>`.
 * @param T The type of the tree itself, extending the `Tree` interface.
 * @return A newly generated tree of type `T` within the specified height constraints.
 */
@ExperimentalKeen
fun <V, L, I, T> Tree.Companion.generate(
    nodes: Pair<List<L>, List<I>>,
    min: Int,
    max: Int,
    condition: (Int, Int) -> Boolean,
    leafFactory: (L) -> T,
    intermediateFactory: (I, List<T>) -> T,
): T where L : Leaf<V>, I : Intermediate<V>, T : Tree<V, T> {
    val leafs = nodes.first
    val intermediates = nodes.second
    constraints {
        "There should be at least one leaf node." { leafs mustNot BeEmpty }
        "The minimum height must be positive." { min must BePositive }
        "The maximum height must be positive." { max must BePositive }
        "The maximum height [$max] must be greater than the minimum height [$min]." {
            min mustNot BeEqualTo(Int.MAX_VALUE)
            max must BeAtLeast(min + 1)
        }
    }
    val height = Domain.random.nextInt(min, max)
    return generateRecursive(
        leafs to intermediates,
        0,
        height,
        condition,
        leafFactory to intermediateFactory
    )
}

/**
 * Recursively generates a tree structure with a specified depth and maximum height. This private helper function
 * is part of the experimental features in the Keen library. It is designed to facilitate the creation of tree
 * structures composed of leaf and intermediate nodes.
 *
 * ## Functionality:
 * - Takes a pair of lists containing leaf ([L]) and intermediate ([I]) node types.
 * - Recursively constructs a tree up to a specified [depth] and [maxHeight].
 * - Uses a [condition] function to determine whether to create a leaf or an intermediate node at each step.
 * - Utilizes provided [factories] functions to create leaf ([T]) and intermediate ([T]) nodes.
 * - Builds the tree by adding children to intermediate nodes, with the depth of recursion guided by the arity of
 *   the intermediate nodes and the [condition] function.
 *
 * ## Usage:
 * This method is instrumental in constructing complex tree structures for scenarios like testing tree-based algorithms,
 * generating random trees for simulation purposes, or building structured data models.
 *
 * ### Example:
 * ```
 * // Example usage within the Tree companion object
 * val tree = Tree.generateRecursive(
 *     nodes = listOfLeaves to listOfIntermediates,
 *     depth = 0,
 *     maxHeight = 5,
 *     condition = { maxHeight, depth -> depth >= maxHeight },
 *     factories = leafFactory to intermediateFactory
 * )
 * // tree is a newly generated Tree instance with the specified structure
 * ```
 *
 * @param V The type of value held by the tree nodes.
 * @param T The type of the tree itself, extending the `Tree` interface.
 * @param L The leaf node type, extending `Leaf<V>`.
 * @param I The intermediate node type, extending `Intermediate<V>`.
 * @param nodes A pair of lists containing leaf nodes (`List<L>`) and intermediate nodes (`List<I>`).
 * @param depth The current depth in the tree during the recursive generation.
 * @param maxHeight The maximum height of the tree to be generated.
 * @param condition A function that determines whether to create a leaf or an intermediate node based on `maxHeight` and
 *   `depth`.
 * @param factories A pair of factory functions to create leaf and intermediate nodes, respectively.
 * @return A newly generated tree of type `T`.
 */
@ExperimentalKeen
private fun <V, T, I, L> Tree.Companion.generateRecursive(
    nodes: Pair<List<L>, List<I>>,
    depth: Int,
    maxHeight: Int,
    condition: (maxHeight: Int, depth: Int) -> Boolean,
    factories: Pair<(L) -> T, (I, List<T>) -> T>
): T where L : Leaf<V>, I : Intermediate<V>, T : Tree<V, T> {
    val leafs = nodes.first
    val intermediates = nodes.second
    // Create an empty list to store children of the current node
    val children = mutableListOf<T>()
    // Decide whether to create a leaf or an intermediate node based on the condition
    val node = if (condition(maxHeight, depth)) {
        factories.first(leafs.random(Domain.random))
    } else {
        // Choose a random function from the list of functions
        val intermediate = intermediates.random(Domain.random)
        // Create children for the current node
        repeat(intermediate.arity) {
            // Recursively create a child node and add it to the list of children
            children.add(
                generateRecursive(
                    nodes,
                    depth + 1,
                    maxHeight,
                    condition,
                    factories
                )
            )
        }
        // Create a function node with the list of children
        factories.second(intermediate, children)
    }
    return node
}
