/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils.trees

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.annotations.ExperimentalKeen
import cl.ravenhill.keen.utils.nextIntInRange


/**
 * Generates a random tree structure within specified height constraints as part of the experimental features
 * in the Keen library. It is designed to create tree structures with controlled variation in height.
 *
 * ## Functionality:
 * - Constructs a tree by randomly selecting between leaf ([L]) and intermediate ([I]) nodes from provided lists.
 * - The tree's height is determined randomly but constrained within the specified minimum and maximum heights
 *   ([heightRange]).
 * - Utilizes a [condition] function at each level to decide between creating a leaf or an intermediate node.
 * - Employs [leafFactory] and [intermediateFactory] functions to construct the actual leaf and intermediate nodes.
 *
 * ## Constraints:
 * - Requires at least one leaf node in the leaf list to ensure a valid tree structure.
 * - Enforces that both minimum and maximum heights are positive integers, with the maximum being greater than the
 *   minimum.
 *
 * ## Usage:
 * Ideal for generating diverse tree structures in testing, simulations, genetic programming, or algorithm development
 * where random yet controlled tree structures are essential.
 *
 * ### Example:
 * ```kotlin
 * val tree = Tree.generate(
 *     nodes = listOfLeaves to listOfIntermediates,
 *     depths = 2 to 5,
 *     condition = { maxHeight, depth -> depth >= maxHeight },
 *     leafFactory = { leaf -> /* create leaf node */ },
 *     intermediateFactory = { intermediate, children -> /* create intermediate node */ }
 * )
 * // Generates a Tree instance with height between 2 and 5
 * ```
 *
 * @param nodes A pair of lists containing leaf nodes ([List]<[L]>) and intermediate nodes ([List]<[I]>).
 * @param heightRange A pair specifying the minimum and maximum heights of the tree (inclusive).
 * @param condition A function that determines the creation of a leaf or an intermediate node based on the current
 *   depth and the maximum height.
 * @param leafFactory A function that constructs a leaf node of type [T] from an [L] object.
 * @param intermediateFactory A function that constructs an intermediate node of type [T] from an [I] object and a list
 *   of child nodes.
 * @param V The type parameter representing the value type held by the tree nodes.
 * @param L The leaf node type, derived from `Leaf<V>`.
 * @param I The intermediate node type, derived from `Intermediate<V>`.
 * @param T The tree type, derived from the `Tree<V, T>` interface.
 * @return A newly generated tree of type `T` conforming to the specified height constraints.
 */
@ExperimentalKeen
fun <V, L, I, T> Tree.Companion.generate(
    nodes: Pair<List<L>, List<I>>,
    heightRange: IntRange,
    condition: (maxHeight: Int, currentDepth: Int) -> Boolean,
    leafFactory: (L) -> T,
    intermediateFactory: (I, List<T>) -> T,
): T where L : Leaf<V>, I : Intermediate<V>, T : Tree<Node<V>, T> {
    val leafs = nodes.first
    constraints {
        "There should be at least one leaf node." { leafs mustNot BeEmpty }
        "The minimum height (${heightRange.first}) must be positive." { heightRange.first must BePositive }
        "The maximum height (${heightRange.last}) must be positive." { heightRange.last must BePositive }
    }
    val height = Domain.random.nextIntInRange(heightRange)
    return generateRecursive(
        nodes,
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
    factories: Pair<(L) -> T, (I, List<T>) -> T>,
): T where L : Leaf<V>, I : Intermediate<V>, T : Tree<Node<V>, T> {
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
