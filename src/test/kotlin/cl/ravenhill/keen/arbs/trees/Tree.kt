/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.trees

import cl.ravenhill.keen.util.trees.Tree
import cl.ravenhill.keen.util.trees.generate
import cl.ravenhill.keen.util.trees.intermediate
import cl.ravenhill.keen.util.trees.intermediateFactory
import cl.ravenhill.keen.util.trees.leaf
import cl.ravenhill.keen.util.trees.leafFactory
import cl.ravenhill.keen.arbs.datatypes.orderedPair
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list

/**
 * Generates an arbitrary [Tree] structure using the provided generator for its elements.
 * This function provides a means to generate a random tree structure within a specified
 * depth range, allowing the creation of trees with varying levels of complexity.
 *
 * @param T The type of elements contained within the tree.
 * @param gen An [Arb] instance that dictates the generation of tree elements of type [T].
 * @param maxDepth An [IntRange] that specifies the minimum and maximum depth of the generated tree.
 *                 By default, this range is set between 1 and 5.
 *
 * @return An [Arb] instance capable of generating random trees based on the provided parameters.
 */
fun <T> Arb.Companion.tree(gen: Arb<T>, maxDepth: IntRange = 1..5) = arbitrary {
    val (lo, hi) = orderedPair(int(maxDepth), int(maxDepth), strict = true).bind()
    Tree.generate(
        leafs = list(leaf(gen), 1..10).bind(),
        intermediates = list(intermediate<T>(), 1..10).bind(),
        min = lo,
        max = hi,
        condition = { _, _ -> true },
        leafFactory = { value -> leafFactory(value) }
    ) { value, children -> intermediateFactory(value, children) }
}
