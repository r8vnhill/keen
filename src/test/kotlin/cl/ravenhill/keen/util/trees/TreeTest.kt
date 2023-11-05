/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.trees

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TreeTest : FreeSpec({
    lateinit var singleElementTree: TypedTree<Char>
    lateinit var multiElementTree: TypedTree<Char>

    lateinit var leafNodeD: TypedTree<Char>
    lateinit var intermediateNodeB: TypedTree<Char>
    lateinit var leafNodeC: TypedTree<Char>

    beforeTest {
        leafNodeD = TypedTree(TypedLeaf('d'))
        intermediateNodeB = TypedTree(TypedIntermediate(1, 'b'), listOf(leafNodeD))
        leafNodeC = TypedTree(TypedLeaf('c'))
        singleElementTree = TypedTree(TypedLeaf('a'))
        multiElementTree = TypedTree(TypedIntermediate(2, 'a'), listOf(intermediateNodeB, leafNodeC))
    }

    /**
     * # Tree | Test Specification
     * This test specification is used to test the `Tree` interface based on two cases:
     *
     * ## Case 1: `Tree` has a single node
     *      a
     *
     * ## Case 2: `Tree` has more than one node
     *     a
     *    / \
     *   b   c
     *   |
     *   d
     */
    "A single element [Tree]" - {
        "when accessing its [height]" - {
            "should return 0" {
                singleElementTree.height shouldBe 0
            }
        }

        "when accessing its [size]" - {
            "should return 1" {
                singleElementTree.size shouldBe 1
            }
        }

        "when accessing its descendants" - {
            "should return an empty list" {
                singleElementTree.descendants shouldBe emptyList()
            }
        }

        "can be converted to a simplified string representation" {
            singleElementTree.toSimpleString() shouldBe "TypedLeaf(value=a)"
        }

        "can be converted to a detailed string representation" {
            singleElementTree.toDetailedString() shouldBe "TypedTree(" +
                  "value=TypedLeaf(value=a), " +
                  "size=1, " +
                  "arity=0, " +
                  "height=0, " +
                  "children=[], " +
                  "descendants=[]" +
                  ")"
        }
    }

    "A [Tree] with more than one element" - {
        "when accessing its height" - {
            "should return the height of its tallest subtree" {
                multiElementTree.height shouldBe 2
            }
        }

        "when accessing its size" - {
            "should return the number of nodes in the tree" {
                multiElementTree.size shouldBe 4
            }
        }

        "when accessing its descendants" - {
            "should return a list with all the nodes in the tree except the root node" {
                multiElementTree.descendants shouldBe listOf(intermediateNodeB, leafNodeD, leafNodeC)
            }
        }

        "can be converted to a simplified string representation" {
            multiElementTree.toSimpleString() shouldBe """
                TypedIntermediate(arity=2, value=a) {
                  TypedIntermediate(arity=1, value=b) {
                    TypedLeaf(value=d)
                  }
                  TypedLeaf(value=c)
                }
            """.trimIndent()
        }

        "can be converted to a detailed string representation" {
            multiElementTree.toDetailedString() shouldBe "TypedTree(" +
                  "value=TypedIntermediate(arity=2, value=a), " +
                  "size=4, " +
                  "arity=2, " +
                  "height=2, " +
                  "children=[" +
                  "TypedTree(" +
                  "node=TypedIntermediate(arity=1, value=b), " +
                  "children=[TypedTree(node=TypedLeaf(value=d), children=[])]" +
                  "), " +
                  "TypedTree(node=TypedLeaf(value=c), children=[])" +
                  "], " +
                  "descendants=[" +
                  "TypedTree(" +
                  "node=TypedIntermediate(arity=1, value=b), " +
                  "children=[" +
                  "TypedTree(node=TypedLeaf(value=d), children=[])]), " +
                  "TypedTree(node=TypedLeaf(value=d), children=[]), " +
                  "TypedTree(node=TypedLeaf(value=c), children=[])" +
                  "]" +
                  ")"
        }
    }
})
