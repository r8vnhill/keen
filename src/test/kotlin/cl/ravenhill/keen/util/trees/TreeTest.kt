/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.trees.tree
import cl.ravenhill.keen.random
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
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

    "A given [Tree] should" - {
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

        "when searching for an element in its subtree" - {
            "should return an IntRange with the indices of the subtree" {
                val range = basicTree.searchSubtree(intermediateTree)
                range.first shouldBe 2
                range.last shouldBe 5
            }

            "should throw a NoSuchElementException if the element is not found" {
                checkAll(Arb.tree(Arb.int())) { tree ->
                    shouldThrowWithMessage<NoSuchElementException>("Node not found in tree.") {
                        basicTree.searchSubtree(tree)
                    }
                }
            }
        }

        "can be created from a depth-first list of nodes" {
            checkAll(Arb.tree(Arb.int())) { tree ->
                val newTree = basicTree.fromDepthFirst(tree.nodes)
                newTree shouldBe tree
            }
        }

        "can replace a subtree with a new node" {
            /**
             * Tree representation:
             *
             *    a
             *    |
             *    b
             */
            val originalNode = TypedTree(TypedIntermediate(1, 'a'), listOf(TypedTree(TypedLeaf('b'))))

            /**
             * Tree representation:
             *
             *   c
             *  / \
             * a   d
             * |
             * b
             */
            val tree = TypedTree(TypedIntermediate(2, 'c'), listOf(originalNode, TypedTree(TypedLeaf('d'))))

            /**
             * Tree representation:
             *
             *   e
             *  / \
             * f   g
             */
            val replacementNode =
                TypedTree(TypedIntermediate(2, 'e'), listOf(TypedTree(TypedLeaf('f')), TypedTree(TypedLeaf('g'))))

            // Replace the originalNode in the tree with the replacementNode
            val newTree = tree.replaceSubtree(originalNode, replacementNode)

            // Validate if the newTree structure matches our expectation
            newTree.value shouldBe TypedIntermediate(2, 'c')
            newTree.children.size shouldBe 2

            // Checking the replaced subtree
            val replacedSubtree = newTree.children.first()
            replacedSubtree.value shouldBe TypedIntermediate(3, 'e')
            replacedSubtree.children.size shouldBe 2
            replacedSubtree.children[0].value shouldBe TypedLeaf('f')
            replacedSubtree.children[1].value shouldBe TypedLeaf('g')

            // Checking the subtree that wasn't replaced
            val secondSubtree = newTree.children[1]
            secondSubtree.value shouldBe TypedLeaf('d')
        }


        "toSimpleString should return simplified tree structure" {
            // Create a sample tree for testing
            val child1 = TypedTree(TypedLeaf('b'))
            val child2 = TypedTree(TypedIntermediate(3, 'c'), listOf(TypedTree(TypedLeaf('d'))))
            val root = TypedTree(TypedIntermediate(2, 'a'), listOf(child1, child2))

            // Expected output:
            val expectedOutput = """
            a {
              b
              c {
                d
              }
            }
        """.trimIndent()

            root.toSimpleString() shouldBe expectedOutput
        }

        "toFullString should return detailed tree structure" {
            // Create a sample tree for testing
            val child1 = TypedTree(TypedLeaf('b'))
            val child2 = TypedTree(TypedIntermediate(3, 'c'), listOf(TypedTree(TypedLeaf('d'))))
            val root = TypedTree(TypedIntermediate(2, 'a'), listOf(child1, child2))

            // Expected output (pseudo-code, the actual format may differ based on the actual implementation):
            val expectedOutput =
                "Tree(value=a, arity=2, height=2, size=3, nodes=[" +
                      "Tree(value=b, arity=0, height=0, size=1, nodes=[]), " +
                      "Tree(value=c, arity=1, height=1, size=2, nodes=[" +
                      "Tree(value=d, arity=0, height=0, size=1, nodes=[])])])"

            root.toFullString() shouldBe expectedOutput
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
