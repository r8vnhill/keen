/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.enforcer.requirements

import cl.ravenhill.enforcer.PairRequirementException
import cl.ravenhill.enforcer.requirements.PairRequirement.BeFinite
import cl.ravenhill.enforcer.requirements.PairRequirement.BeStrictlyOrdered
import cl.ravenhill.keen.util.shouldBeFinite
import cl.ravenhill.orderedPair
import cl.ravenhill.unfulfilledConstraint
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.pair
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll


/**
 * This class contains unit tests for the PairRequirement subclasses and related functions.
 *
 * Tests are organized as a free spec, with each major test category defined as a string.
 *
 * ## Test Categories
 *
 * 1. Generating an exception: Tests that invoking `generateException` on a PairRequirement
 *    instance produces a PairRequirementException with the expected message.
 * 2. BeStrictlyOrdered requirement: Tests that a BeStrictlyOrdered instance
 *    - can be converted to a string representation correctly.
 *    - correctly validates whether a pair of elements is strictly ordered.
 * 3. BeFinite requirement: Tests that a BeFinite instance
 *    - can be converted to a string representation correctly.
 *    - correctly validates whether a pair of elements is finite.}
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class PairRequirementTest : FreeSpec({
    "Generating an exception should return a [PairRequirementException]" {
        checkAll(Arb.pairRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<PairRequirementException>()
                message shouldBe unfulfilledConstraint(description)
            }
        }
    }

    "A [BeStrictlyOrdered] requirement" - {
        "can be converted to a [String]" {
            BeStrictlyOrdered<Int>().toString() shouldBe "BeStrictlyOrdered<~>"
        }

        "when _validating_ that a pair is strictly ordered should return" - {
            "[true] if the pair is strictly ordered" {
                checkAll(Arb.orderedPair(Arb.int(), strict = true, reverted = false)) { pair ->
                    BeStrictlyOrdered<Int>().validator(pair).shouldBeTrue()
                }
            }

            "[false] if the pair is not strictly ordered" {
                checkAll(Arb.orderedPair(Arb.int(), strict = false, reverted = true)) { pair ->
                    BeStrictlyOrdered<Int>().validator(pair).shouldBeFalse()
                }
            }
        }
    }

    "A [BeFinite] requirement" - {
        "can be converted to a [String]" {
            BeFinite.toString() shouldBe "BeFinite"
        }
        "when _validating_ that a pair is finite should return" - {
            "[true] if the pair is finite" {
                checkAll(Arb.pair(Arb.double(), Arb.double())) { test ->
                    assume {
                        test.first.shouldBeFinite()
                        test.second.shouldBeFinite()
                    }
                    BeFinite.validator(test).shouldBeTrue()
                }
            }

            "[false] if the pair is not finite" {
                checkAll(
                    Arb.pair(
                        Arb.element(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
                        Arb.element(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
                    )
                ) { data ->
                    BeFinite.validator(data).shouldBeFalse()
                }
            }
        }
    }
})

/**
 * This function provides an arbitrary generator of [PairRequirement] instances.
 */
private fun Arb.Companion.pairRequirement() = arbitrary {
    element(
        BeStrictlyOrdered<Int>(),
        BeFinite
    ).bind()
}
