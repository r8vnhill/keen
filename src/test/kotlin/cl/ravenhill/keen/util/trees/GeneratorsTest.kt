/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.EnforcementException
import cl.ravenhill.keen.IntRequirementException
import cl.ravenhill.keen.any
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.unfulfilledConstraint
import cl.ravenhill.keen.util.orderedPair
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll


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


class GeneratorsTest : FreeSpec({
    "Generating a [Tree] using the recursive method should" - {
        "throw an exception when" - {
            "there are no intermediate or leaf nodes." {
                checkAll(
                    Arb.positiveInt(),
                    Arb.positiveInt(),
                    Arb.condition()
                ) { minHeight, maxHeight, condition ->
                    shouldThrow<EnforcementException> {
                        Tree.generate(
                            emptyList<Leaf<Any>>(),
                            emptyList(),
                            minHeight,
                            maxHeight,
                            condition.next(),
                            { leafFactory(it) },
                            { intermediate, children ->
                                intermediateFactory(intermediate, children)
                            })
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint("There should be at least one intermediate or leaf node.")
                    )
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