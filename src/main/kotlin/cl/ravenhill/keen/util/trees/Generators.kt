/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.Core

/**
 * Generates a recursive tree with [depth] levels and [height] maximum height, by randomly selecting
 * intermediate nodes from a list of [intermediates], and leaf nodes from a list of [leafs].
 * The condition for selecting an intermediate node or a leaf node is based on the [condition]
 * function that takes the current [height] and [depth] as arguments.
 *
 * The [leafFactory] and [intermediateFactory] parameters are functions that are used to create new
 * leaf and intermediate nodes, respectively. The [leafFactory] function takes a [L] object from the
 * list of [leafs], and returns a new instance of type [T], which represents a leaf node in the
 * tree.
 * The [intermediateFactory] function takes an [I] object from the list of [intermediates], and a
 * list of child nodes of type [T], and returns a new instance of type [T], which represents an
 * intermediate node in the tree.
 *
 * The [generateRecursive] function recursively generates child nodes until the [depth] limit is
 * reached, at which point a leaf node is created if the [condition] function evaluates to `true`.
 * If the [condition] function evaluates to `false`, an intermediate node is created by randomly
 * selecting a function from the list of [intermediates], and creating child nodes recursively using
 * this same function. The [generateRecursive] function returns the root node of the generated tree,
 * which is of type [T].
 *
 * @param V the type of value stored in the tree.
 * @param T the actual type of the tree.
 * @param I the type of intermediate nodes.
 * @param L the type of leaf nodes.
 *
 * @param intermediates a list of intermediate nodes, each of type [I], that can be randomly
 * selected to create intermediate nodes in the tree.
 * @param leafs a list of leaf nodes, each of type [L], that can be randomly selected to create leaf
 * nodes in the tree.
 * @param depth the maximum depth of the tree. The [generateRecursive] function will stop
 * recursively generating child nodes when the [depth] limit is reached.
 * @param height the maximum height of the tree. The [condition] function takes the current [height]
 * and [depth] as arguments, and returns `true` if a leaf node should be created, or `false` if an
 * intermediate node should be created.
 * @param condition a function that takes the current [height] and [depth] as arguments, and returns
 * `true` if a leaf node should be created, or `false` if an intermediate node should be created.
 * @param leafFactory a function that takes an [L] object from the list of [leafs], and returns a
 * new instance of type [T], which represents a leaf node in the tree.
 * @param intermediateFactory a function that takes an [I] object from the list of [intermediates],
 * and a list of child nodes of type [T], and returns a new instance of type [T], which represents
 * an intermediate node in the tree.
 *
 * @return the root node of the generated tree, which is of type [T].
 */

fun <V, T : Tree<V, T>, I : Intermediate<V>, L : Leaf<V>> Tree.Companion.generateRecursive(
    intermediates: List<I>,
    leafs: List<L>,
    depth: Int,
    height: Int,
    condition: (Int, Int) -> Boolean,
    leafFactory: (L) -> T,
    intermediateFactory: (I, List<T>) -> T,
): T {
    // Create an empty list to store children of the current node
    val children = mutableListOf<T>()
    // Decide whether to create a leaf or an intermediate node based on the condition
    val node = if (condition(height, depth)) {
        leafFactory(leafs.random(Core.random))
    } else {
        // Choose a random function from the list of functions
        val intermediate = intermediates.random(Core.random)
        // Create children for the current node
        repeat(intermediate.arity) {
            // Recursively create a child node and add it to the list of children
            children.add(
                generateRecursive(
                    intermediates,
                    leafs,
                    depth + 1,
                    height,
                    condition,
                    leafFactory,
                    intermediateFactory
                )
            )
        }
        // Create a function node with the list of children
        intermediateFactory(intermediate, children)
    }
    return node
}