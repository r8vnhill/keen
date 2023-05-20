/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work.
 * If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.Core
import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.requirements.CollectionRequirement.BeEmpty
import cl.ravenhill.enforcer.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.enforcer.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive


//fun <V, T : Tree<V, T>, I : Intermediate<V>, L : Leaf<V>> Tree.Companion.generateFull(
//    leafs: List<L>, functions: List<Fun<T>>, min: Int, max: Int
//): Program<T> {
//    val condition = { height: Int, depth: Int ->
//        depth == height
//    }
//    return generate(functions, leafs, min, max, condition)
//}

/**
 * Generates a tree by randomly selecting intermediate and leaf nodes from the provided lists of
 * [intermediates] and [leafs], based on the given [condition] function.
 * The generated tree has a height between [min] and [max].
 *
 * @param V The type of value stored in the tree.
 * @param T The actual type of the tree.
 * @param I The type of intermediate nodes.
 * @param L The type of leaf nodes.
 *
 * @param intermediates A list of intermediate nodes, each of type [I], that can be randomly
 * selected to create intermediate nodes in the tree.
 * @param leafs A list of leaf nodes, each of type [L], that can be randomly selected to create leaf
 * nodes in the tree.
 * @param min The minimum height of the generated tree.
 * @param max The maximum height of the generated tree.
 * @param condition A function that takes the current height and maximum height as arguments and
 * returns `true` if a leaf node should be created, or `false` if an intermediate node should be
 * created.
 * @param leafFactory A function that takes an [L] object from the list of [leafs] and returns a new
 * instance of type [T], representing a leaf node in the tree.
 * @param intermediateFactory A function that takes an [I] object from the list of [intermediates],
 * and a list of child nodes of type [T], and returns a new instance of type [T], representing an
 * intermediate node in the tree.
 *
 * @return The root node of the generated tree, of type [T].
 *
 * @throws EnforcementException if the list of intermediates and leafs is empty, or if the minimum
 * or maximum height is not positive, or if the maximum height is less than the minimum height.
 *
 * @see Tree.Companion.generateRecursive
 */
fun <V, L, I, T> Tree.Companion.generate(
    leafs: List<L>,
    intermediates: List<I>,
    min: Int,
    max: Int,
    condition: (Int, Int) -> Boolean,
    leafFactory: (L) -> T,
    intermediateFactory: (I, List<T>) -> T,
): T where L : Leaf<V>, I : Intermediate<V>, T : Tree<V, T> {
    enforce {
        "There should be at least one intermediate node." { intermediates mustNot BeEmpty }
        "The minimum height must be positive." { min must BePositive }
        "The maximum height must be positive." { max must BePositive }
        "The maximum height [$max] must be greater than the minimum height [$min]." {
            min mustNot BeEqualTo(Int.MAX_VALUE)
            max must BeAtLeast(min + 1)
        }
    }
    val height = Core.random.nextInt(min, max)
    return generateRecursive(
        intermediates,
        leafs,
        0,
        height,
        condition,
        leafFactory,
        intermediateFactory
    )
}

/**
 * Generates a recursive tree with [depth] levels and [maxHeight] maximum height, by randomly
 * selecting intermediate nodes from a list of [intermediates], and leaf nodes from a list of
 * [leafs].
 * The condition for selecting an intermediate node or a leaf node is based on the [condition]
 * function that takes the current [maxHeight] and [depth] as arguments.
 *
 * The [leafFactory] and [intermediateFactory] parameters are functions that are used to create new
 * leaf and intermediate nodes, respectively.
 * The [leafFactory] function takes a [L] object from the
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
 * this same function.
 * The [generateRecursive] function returns the root node of the generated tree,
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
 * @param depth the maximum depth of the tree.
 * The [generateRecursive] function will stop
 * recursively generating child nodes when the [depth] limit is reached.
 * @param maxHeight the maximum height of the tree.
 * The [condition] function takes the current [maxHeight]
 * and [depth] as arguments, and returns `true` if a leaf node should be created, or `false` if an
 * intermediate node should be created.
 * @param condition a function that takes the current [maxHeight] and [depth] as arguments, and returns
 * `true` if a leaf node should be created, or `false` if an intermediate node should be created.
 * @param leafFactory a function that takes an [L] object from the list of [leafs], and returns a
 * new instance of type [T], which represents a leaf node in the tree.
 * @param intermediateFactory a function that takes an [I] object from the list of [intermediates],
 * and a list of child nodes of type [T], and returns a new instance of type [T], which represents
 * an intermediate node in the tree.
 *
 * @return the root node of the generated tree, which is of type [T].
 */

private fun <V, T : Tree<V, T>, I : Intermediate<V>, L : Leaf<V>> Tree.Companion.generateRecursive(
    intermediates: List<I>,
    leafs: List<L>,
    depth: Int,
    maxHeight: Int,
    condition: (Int, Int) -> Boolean,
    leafFactory: (L) -> T,
    intermediateFactory: (I, List<T>) -> T,
): T {
    // Create an empty list to store children of the current node
    val children = mutableListOf<T>()
    // Decide whether to create a leaf or an intermediate node based on the condition
    val node = if (condition(maxHeight, depth)) {
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
                    maxHeight,
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
