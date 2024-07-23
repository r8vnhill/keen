/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils.trees

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.annotations.ExperimentalKeen
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random


@OptIn(ExperimentalKeen::class)
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
            singleElementTree.toSimpleString() shouldBe "TypedLeaf(contents=a)"
        }

        "can be converted to a detailed string representation" {
            singleElementTree.toString() shouldBe "TypedTree(" +
                    "value=TypedLeaf(contents=a), " +
                    "size=1, " +
                    "arity=0, " +
                    "height=0, " +
                    "children=[], " +
                    "descendants=[]" +
                    ")"
        }

        "when getting a random node" - {
            "should return the root node" {
                checkAll(Arb.random()) { random ->
                    Domain.random = random
                    singleElementTree.random() shouldBe singleElementTree
                }
            }
        }

        "when searching for a node should" - {
            "return the index of the node if it is in the tree" {
                singleElementTree.indexOfFirst { it.value.contents == 'a' } shouldBe 0..<1
            }

            "throw an exception if the node is not in the tree" {
                shouldThrow<CompositeException> {
                    singleElementTree.indexOfFirst { it.value.contents == 'b' }
                }.shouldHaveInfringement<IntConstraintException>("Predicate does not hold for any node in the tree.")
            }
        }

        "when creating a new instance from a top-down list of nodes" - {
            "from a list with a single element should return the same element" {
                val nodes = listOf(
                    singleElementTree
                )
                val tree = singleElementTree.fromTopDown(nodes)
                tree shouldBe singleElementTree
            }

            "from a list with more than one element should return the provided multi-element tree" {
                val nodes = listOf(
                    intermediateNodeB,
                    leafNodeD
                )
                val tree = singleElementTree.fromTopDown(nodes)
                tree shouldBe intermediateNodeB
            }

            "from an empty list should throw a [CompositeException] with a [CollectionConstraintException]" {
                val nodes = emptyList<TypedTree<Char>>()
                shouldThrow<CompositeException> {
                    singleElementTree.fromTopDown(nodes)
                }.shouldHaveInfringement<CollectionConstraintException>(
                    "Cannot create a tree from an empty list of nodes."
                )
            }
        }

        "when converting to " - {
            "top-down list of nodes " - {
                "should return a list with a single element" {
                    singleElementTree.toTopDownList() shouldBe listOf(singleElementTree)
                }
            }

            "top-down sequence of nodes" - {
                "should return a sequence with a single element" {
                    singleElementTree.toTopDownSequence().toList() shouldBe listOf(singleElementTree)
                }
            }
        }

        "can replace its root node" {
            val newRoot = intermediateNodeB
            val newTree = singleElementTree.replaceFirst(newRoot) { it.value.contents == 'a' }
            newTree shouldBe newRoot
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
                TypedIntermediate(arity=2, contents=a) {
                  TypedIntermediate(arity=1, contents=b) {
                    TypedLeaf(contents=d)
                  }
                  TypedLeaf(contents=c)
                }
            """.trimIndent()
        }

        "can be converted to a detailed string representation" {
            multiElementTree.toDetailedString() shouldBe "TypedTree(" +
                    "value=TypedIntermediate(arity=2, contents=a), " +
                    "size=4, " +
                    "arity=2, " +
                    "height=2, " +
                    "children=[" +
                    "TypedTree(" +
                    "node=TypedIntermediate(arity=1, contents=b), " +
                    "children=[TypedTree(node=TypedLeaf(contents=d), children=[])]" +
                    "), " +
                    "TypedTree(node=TypedLeaf(contents=c), children=[])" +
                    "], " +
                    "descendants=[" +
                    "TypedTree(" +
                    "node=TypedIntermediate(arity=1, contents=b), " +
                    "children=[" +
                    "TypedTree(node=TypedLeaf(contents=d), children=[])]), " +
                    "TypedTree(node=TypedLeaf(contents=d), children=[]), " +
                    "TypedTree(node=TypedLeaf(contents=c), children=[])" +
                    "]" +
                    ")"
        }

        "when getting a random node" - {
            "should return a random node from the tree" {
                checkAll(Arb.long()) { seed ->
                    Domain.random = Random(seed)
                    val random = Random(seed)
                    multiElementTree.random() shouldBe multiElementTree.nodes.random(random)
                }
            }
        }

        "when searching for a node should" - {
            "return the index of the node if it is in the tree" {
                multiElementTree.indexOfFirst { it.value.contents == 'b' } shouldBe 1..<3
            }

            "throw a [NoSuchElementException] if the node is not in the tree" {
                shouldThrowWithMessage<NoSuchElementException>("Node not found in tree") {
                    multiElementTree.indexOfFirst { it.value.contents == 'e' }
                }
            }
        }

        "when creating a new instance from a top-down list of nodes" - {
            "from a list with a single element should return the same element" {
                val nodes = listOf(
                    singleElementTree
                )
                val tree = multiElementTree.fromTopDown(nodes)
                tree shouldBe singleElementTree
            }

            "from a list with more than one element should return the provided multi-element tree" {
                val nodes = listOf(
                    intermediateNodeB,
                    leafNodeD
                )
                val tree = multiElementTree.fromTopDown(nodes)
                tree shouldBe intermediateNodeB
            }

            "from an empty list should throw a [CompositeException] with a [CollectionConstraintException]" {
                val nodes = emptyList<TypedTree<Char>>()
                shouldThrow<CompositeException> {
                    multiElementTree.fromTopDown(nodes)
                }.shouldHaveInfringement<CollectionConstraintException>(
                    "Cannot create a tree from an empty list of nodes."
                )
            }
        }

        "can replace" - {
            "its root node" {
                val newRoot = singleElementTree
                val newTree = multiElementTree.replaceFirst(newRoot) { it.value.contents == 'a' }
                newTree shouldBe newRoot
            }

            "an intermediate node" {
                val replacement = singleElementTree
                val newTree = multiElementTree.replaceFirst(replacement) { it.value.contents == 'b' }
                newTree shouldBe TypedTree(TypedIntermediate(2, 'a'), listOf(leafNodeC, replacement))
            }
        }

        "when converting to " - {
            "top-down list of nodes" - {
                "should return a list with all the nodes in the tree" {
                    multiElementTree.toTopDownList() shouldBe listOf(
                        multiElementTree,
                        intermediateNodeB,
                        leafNodeD,
                        leafNodeC
                    )
                }
            }

            "top-down sequence of nodes" - {
                "should return a sequence with all the nodes in the tree" {
                    multiElementTree.toTopDownSequence().toList() shouldBe listOf(
                        multiElementTree,
                        intermediateNodeB,
                        leafNodeD,
                        leafNodeC
                    )
                }
            }
        }
    }
})

/**
 * Represents an intermediate node in a typed tree structure.
 * An intermediate node is a node that has a specific arity, which determines the number of child nodes it can have.
 *
 * @param T the type of data contained in the node.
 * @property arity the arity of the intermediate node, which determines the number of child nodes it can have.
 *
 * @constructor Creates a new instance of [TypedIntermediate] with the specified [arity].
 */
@OptIn(ExperimentalKeen::class)
data class TypedIntermediate<T>(override val arity: Int, override val contents: T? = null) : Intermediate<T>

/**
 * Represents a leaf node in a typed tree structure.
 * A leaf node is a node that does not have any child nodes.
 *
 * @param T the type of data contained in the node.
 * @property contents the value associated with the leaf node.
 *
 * @constructor Creates a new instance of [TypedLeaf] with the specified [contents].
 */
@OptIn(ExperimentalKeen::class)
data class TypedLeaf<T>(override val contents: T) : Leaf<T>

/**
 * Represents a typed tree structure with nodes of type [Node]<[V]>.
 *
 * @param V The type parameter for the values held by the nodes in the tree.
 * @param value The value of the current node, of type [Node]<[V]>.
 * @param children The children of the current node, each an instance of [TypedTree]<[V]>.
 * @constructor Creates a new instance of [TypedTree] with the given node value and list of children.
 */
@OptIn(ExperimentalKeen::class)
data class TypedTree<V>(
    override val value: Node<V>,
    override val children: List<TypedTree<V>> = emptyList()
) : Tree<Node<V>, TypedTree<V>> {

    override val arity: Int = value.arity

    override fun createNode(value: Node<V>, children: List<TypedTree<V>>) = TypedTree(value, children)

    override val nodes: List<TypedTree<V>> get() = listOf(this) + children.flatMap { it.nodes }
}
