/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.trees

import cl.ravenhill.any
import cl.ravenhill.enforcer.CollectionRequirementException
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.orderedPair
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.ints.shouldBeNonNegative
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll


/**
 * `GeneratorsTest` class provides a set of tests for verifying the properties and behaviors of
 * generating trees.
 *
 * It uses the `FreeSpec` style of the Kotest framework to organize and execute the tests in a
 * descriptive manner.
 *
 * This class primarily tests:
 * 1. The creation of a tree using a recursive method.
 * 2. Constraints and exceptions raised during the tree creation.
 *
 * Key tests include:
 * - Verifying that the generated tree's height falls within the specified range.
 * - Ensuring appropriate exceptions are thrown under various conditions such as absence of leaf
 *   nodes, non-positive minimum height, non-positive maximum height, and when the maximum height is
 *   less than the minimum height.
 */
class GeneratorsTest : FreeSpec({
    "Generating a [Tree] using the recursive method should" - {
        "create a tree with a maximum height between the minimum and maximum heights." {
            checkAll(
                Arb.list(Arb.leaf(Arb.any()), 1..10),
                Arb.list(Arb.intermediate<Any>(), 1..10),
                Arb.orderedPair(Arb.positiveInt(10), Arb.positiveInt(10), strict = true),
                Arb.condition()
            ) { leaves, intermediates, (minHeight, maxHeight), condition ->
                val tree = Tree.generate(
                    leaves,
                    intermediates,
                    minHeight,
                    maxHeight,
                    condition.next(),
                    { leafFactory(it) },
                    { intermediate, children ->
                        intermediateFactory(intermediate, children)
                    })
                tree.height
                    .shouldBeNonNegative()
                    .shouldBeLessThanOrEqual(maxHeight)
            }
        }

        "throw an exception when" - {
            "there are no leaf nodes." {
                with(Arb) {
                    checkAll(
                        orderedPair(positiveInt(), positiveInt(), strict = true),
                        list(intermediate<Any>()),
                        condition()
                    ) { (minHeight, maxHeight), intermediates, condition ->
                        shouldThrow<EnforcementException> {
                            Tree.generate(
                                emptyList(),
                                intermediates,
                                minHeight,
                                maxHeight,
                                condition.next(),
                                { leafFactory(it) },
                                { intermediate, children ->
                                    intermediateFactory(intermediate, children)
                                })
                        }.shouldHaveInfringement<CollectionRequirementException>(
                            unfulfilledConstraint("There should be at least one leaf node.")
                        )
                    }
                }
            }

            "the minimum height is not positive." {
                checkAll(
                    Arb.list(Arb.leaf(Arb.any())),
                    Arb.list(Arb.intermediate<Any>()),
                    Arb.nonPositiveInt(),
                    Arb.positiveInt(),
                    Arb.condition()
                ) { leaves, intermediates, minHeight, maxHeight, condition ->
                    shouldThrow<EnforcementException> {
                        Tree.generate(
                            leaves,
                            intermediates,
                            minHeight,
                            maxHeight,
                            condition.next(),
                            { leafFactory(it) },
                            { intermediate, children ->
                                intermediateFactory(intermediate, children)
                            })
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint("The minimum height must be positive.")
                    )
                }
            }

            "the maximum height is not positive." {
                checkAll(
                    Arb.list(Arb.leaf(Arb.any())),
                    Arb.list(Arb.intermediate<Any>()),
                    Arb.positiveInt(),
                    Arb.nonPositiveInt(),
                    Arb.condition()
                ) { leaves, intermediates, minHeight, maxHeight, condition ->
                    shouldThrow<EnforcementException> {
                        Tree.generate(
                            leaves,
                            intermediates,
                            minHeight,
                            maxHeight,
                            condition.next(),
                            { leafFactory(it) },
                            { intermediate, children ->
                                intermediateFactory(intermediate, children)
                            })
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint("The maximum height must be positive.")
                    )
                }
            }

            "the maximum height is less than or equal to the minimum" {
                checkAll(
                    Arb.list(Arb.leaf(Arb.any())),
                    Arb.list(Arb.intermediate<Any>()),
                    Arb.orderedPair(Arb.positiveInt(), Arb.positiveInt()),
                    Arb.condition()
                ) { leaves, intermediates, (maxHeight, minHeight), condition ->
                    shouldThrow<EnforcementException> {
                        Tree.generate(
                            leaves,
                            intermediates,
                            minHeight,
                            maxHeight,
                            condition.next(),
                            { leafFactory(it) },
                            { intermediate, children ->
                                intermediateFactory(intermediate, children)
                            })
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint("The maximum height [$maxHeight] must be greater than the minimum height [$minHeight].")
                    )
                }
            }
        }
    }
})

/**
 * Creates an arbitrary condition for generating values based on the depth and maximum height.
 * The condition is used in conjunction with other arbitrary generators to control the
 * generation process.
 */
private fun Arb.Companion.condition() = arbitrary {
    element(
        { depth: Int, maxHeight: Int -> depth < maxHeight },
        { depth: Int, maxHeight: Int -> depth >= maxHeight },
        { depth: Int, maxHeight: Int -> depth == maxHeight })
}
