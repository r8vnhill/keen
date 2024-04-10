/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils.trees

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.assertions.should.shouldBeInRange
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.pair
import io.kotest.property.checkAll

@OptIn(ExperimentalKeen::class)
class GenerationTest : FreeSpec({

    "When generating a Random Tree it" - {
        "should return a tree with height in the given range" {
            checkAll(
                Arb.list(Arb.char().map { TypedLeaf(it) }, 1..10),
                Arb.list(Arb.pair(Arb.char(), Arb.int(1..3)).map { (v, a) -> TypedIntermediate(a, v) }, 1..10),
                arbRange(Arb.int(1..4), Arb.int(2..5))
            ) { leaves, intermediates, heights ->
                val condition = { maxHeight: Int, currentDepth: Int -> currentDepth >= maxHeight }
                val leafFactory = { leaf: Leaf<Char> -> TypedTree(leaf) }
                val intermediateFactory = { intermediate: Intermediate<Char>, children: List<TypedTree<Char>> ->
                    TypedTree(intermediate, children)
                }
                val tree = Tree.generate(
                    leaves to intermediates,
                    heights.start..heights.endInclusive,
                    condition,
                    leafFactory,
                    intermediateFactory
                )
                tree.height shouldBeInRange heights
            }
        }

        "should throw an exception" - {
            "if there are no leaf generators" {
                checkAll(
                    Arb.list(Arb.pair(Arb.char(), Arb.int(1..3)).map { (v, a) -> TypedIntermediate(a, v) }, 1..10),
                    arbRange(Arb.int(1..4), Arb.int(2..5))
                ) { intermediates, heights ->
                    val condition = { maxHeight: Int, currentDepth: Int -> currentDepth >= maxHeight }
                    val leafFactory = { leaf: Leaf<Char> -> TypedTree(leaf) }
                    val intermediateFactory = { intermediate: Intermediate<Char>, children: List<TypedTree<Char>> ->
                        TypedTree(intermediate, children)
                    }
                    shouldThrow<CompositeException> {
                        Tree.generate(
                            emptyList<Leaf<Char>>() to intermediates,
                            heights.start..heights.endInclusive,
                            condition,
                            leafFactory,
                            intermediateFactory
                        )
                    }.shouldHaveInfringement<CollectionConstraintException>("There should be at least one leaf node.")
                }
            }

            "if the maximum height is less than 0" {
                checkAll(
                    Arb.list(Arb.char().map { TypedLeaf(it) }, 1..10),
                    Arb.list(Arb.pair(Arb.char(), Arb.int(1..3)).map { (v, a) -> TypedIntermediate(a, v) }, 1..10),
                    Arb.int(),
                    Arb.negativeInt()
                ) { leaves, intermediates, min, max ->
                    val condition = { maxHeight: Int, currentDepth: Int -> currentDepth >= maxHeight }
                    val leafFactory = { leaf: Leaf<Char> -> TypedTree(leaf) }
                    val intermediateFactory = { intermediate: Intermediate<Char>, children: List<TypedTree<Char>> ->
                        TypedTree(intermediate, children)
                    }
                    shouldThrow<CompositeException> {
                        Tree.generate(
                            leaves to intermediates,
                            min..max,
                            condition,
                            leafFactory,
                            intermediateFactory
                        )
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The maximum height ($max) must be positive."
                    )
                }
            }

            "if the minimum height is less than 0" {
                checkAll(
                    Arb.list(Arb.char().map { TypedLeaf(it) }, 1..10),
                    Arb.list(Arb.pair(Arb.char(), Arb.int(1..3)).map { (v, a) -> TypedIntermediate(a, v) }, 1..10),
                    Arb.negativeInt(),
                    Arb.int()
                ) { leaves, intermediates, min, max ->
                    val condition = { maxHeight: Int, currentDepth: Int -> currentDepth >= maxHeight }
                    val leafFactory = { leaf: Leaf<Char> -> TypedTree(leaf) }
                    val intermediateFactory = { intermediate: Intermediate<Char>, children: List<TypedTree<Char>> ->
                        TypedTree(intermediate, children)
                    }
                    shouldThrow<CompositeException> {
                        Tree.generate(
                            leaves to intermediates,
                            min..max,
                            condition,
                            leafFactory,
                            intermediateFactory
                        )
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The minimum height ($min) must be positive."
                    )
                }
            }
        }
    }
})
