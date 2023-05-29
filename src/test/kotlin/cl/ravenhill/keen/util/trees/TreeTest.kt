/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.random
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

class TreeTest : FreeSpec({
    /**
     * Represents a leaf node containing an integer value of 1.
     */
    lateinit var leafNode: TypedLeaf<Int>

    /**
     * Represents a tree consisting of a single leaf node containing an integer value of 1.
     */
    lateinit var leafTree: TypedTree<Int>

    /**
     * Represents an intermediate node with an arity of 2.
     */
    lateinit var intermediateNode: TypedIntermediate<Int>

    /**
     * Represents a tree consisting of an intermediate node with an arity of 2, and two child leaf
     * trees, both containing an integer value of 1.
     */
    lateinit var intermediateTree: TypedTree<Int>

    /**
     * Represents a tree consisting of an intermediate node with an arity of 2, and two child trees
     * - one leaf tree containing an integer value of 1, and one intermediate tree with two child
     * leaf trees, both containing an integer value of 1.
     *
     * ```
     *          intermediateNode
     *                 |
     *           +-----+-----+
     *           |           |
     *         leafNode  intermediateNode
     *                       |
     *                 +-----+-----+
     *                 |           |
     *              leafNode   leafNode
     * ```
     */
    lateinit var basicTree: TypedTree<Int>

    beforeEach {
        leafNode = TypedLeaf(1)
        leafTree = TypedTree(leafNode)
        intermediateNode = TypedIntermediate(2)
        intermediateTree = TypedTree(intermediateNode, listOf(leafTree, leafTree))
        basicTree = TypedTree(intermediateNode, listOf(leafTree, intermediateTree))
    }

    "A given leaf should" - {
        "have a `value` equal to the leaf object" {
            leafTree.value shouldBe leafNode
        }

        "have an empty list of `children`" {
            leafTree.children shouldBe emptyList()
        }

        "have a list with itself as the only element in `nodes`" {
            leafTree.nodes shouldBe listOf(leafTree)
        }

        "have an `arity` equal to 0" {
            leafTree.arity shouldBe 0
        }

        "have a `height` equal to 0" {
            leafTree.height shouldBe 0
        }

        "have a `size` equal to 1" {
            leafTree.size shouldBe 1
        }

        "have an empty list of `descendants`" {
            leafTree.descendants shouldBe emptyList()
        }

        "return itself when `random` is called" {
            checkAll(Arb.random()) { rng ->
                Core.random = rng
                leafTree.random() shouldBe leafTree
            }
        }
    }

    "A given intermediate node should" - {
        "have a `value` equal to the intermediate object" {
            intermediateTree.value shouldBe intermediateNode
        }

        "have a list of `children` equal to its child nodes" {
            intermediateTree.children shouldBe listOf(leafTree, leafTree)
        }

        "have a list of `nodes` equal to itself and its child nodes" {
            intermediateTree.nodes shouldBe listOf(intermediateTree, leafTree, leafTree)
        }

        "have an `arity` equal to the number of child nodes" {
            intermediateTree.arity shouldBe 2
        }

        "have a `height` equal to 1" {
            intermediateTree.height shouldBe 1
        }

        "have a `size` equal to 3" {
            intermediateTree.size shouldBe 3
        }

        "have a list of `descendants` equal to its child nodes" {
            intermediateTree.descendants shouldBe listOf(leafTree, leafTree)
        }

        "return a random node when `random` is called" {
            `check that a random node is returned when random is called`(intermediateTree)
        }
    }

    "A given tree should" - {
        "have a `value` equal to the root node" {
            basicTree.value shouldBe intermediateNode
        }

        "have a list of `children` equal to the list of child nodes" {
            basicTree.children shouldBe listOf(leafTree, intermediateTree)
        }

        "have a list of `nodes` equal to itself and all the nodes in its subtrees" {
            basicTree.nodes shouldBe listOf(
                basicTree,
                leafTree,
                intermediateTree,
                leafTree,
                leafTree
            )
        }

        "have an `arity` equal to the number of child nodes" {
            basicTree.arity shouldBe 2
        }

        "have a `height` equal to 2" {
            basicTree.height shouldBe 2
        }

        "have a `size` equal to 5" {
            basicTree.size shouldBe 5
        }

        "have a list of `descendants` equal to all the nodes in its subtrees" {
            basicTree.descendants shouldBe listOf(
                leafTree,
                intermediateTree,
                leafTree,
                leafTree
            )
        }

        "return a random node when `random` is called" {
            `check that a random node is returned when random is called`(basicTree)
        }
    }
})

/**
 * Verifies that a random node is returned when the `random` function is called on the specified
 * `tree`.
 *
 * @throws AssertionError if the randomly selected node is not equal to the expected randomly
 * selected node.
 */
private suspend fun `check that a random node is returned when random is called`(
    tree: TypedTree<Int>
) = checkAll(Arb.long()) { seed ->
    Core.random = Random(seed)
    tree.random() shouldBe tree.nodes.random(Random(seed))
}
