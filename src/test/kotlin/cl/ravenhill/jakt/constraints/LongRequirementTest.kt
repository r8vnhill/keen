/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.jakt.constraints

import cl.ravenhill.jakt.constraints.LongConstraint.BeEqualTo
import cl.ravenhill.orderedPair
import cl.ravenhill.unfulfilledConstraint
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


/**
 * Test class for verifying the behaviour of [LongConstraint].
 * It tests various scenarios related to [LongConstraint] and ensures the expected behaviour is
 * observed.
 *
 * @see LongConstraint
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class LongRequirementTest : FreeSpec({
    "Generating an exception should return a [LongRequirementException]" {
        checkAll(Arb.longRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<cl.ravenhill.jakt.exceptions.LongRequirementException>()
                message shouldBe unfulfilledConstraint(description)
            }
        }
    }

    "A [BeEqualTo] requirement" - {
        "can be converted to a [String]" {
            checkAll(Arb.beEqualToData()) { (expected, _, requirement) ->
                requirement.toString() shouldBe "BeEqualTo { expected: $expected }"
            }
        }

        "can be created with an expected value" {
            checkAll(Arb.long()) { expected ->
                BeEqualTo(expected).expected shouldBe expected
            }
        }

        "when _validating_ that a value is equal to the expected value should return" - {
            "[true] if the value is equal to the expected value" {
                checkAll(Arb.beEqualToData()) { (_, test, requirement) ->
                    requirement.validator(test) shouldBe true
                }
            }

            "[false] if the value is not equal to the expected value" {
                checkAll(Arb.beEqualToData(equal = false)) { (_, test, requirement) ->
                    requirement.validator(test) shouldBe false
                }
            }
        }
    }
})

/**
 * Generates an arbitrary instance of [LongConstraint].
 */
private fun Arb.Companion.longRequirement() = arbitrary {
    element(beEqualToData().bind().requirement).bind()
}

/**
 * Generates an [Arb] of [BeEqualToLData] containing Long values and a corresponding [BeEqualTo]
 * requirement.
 *
 * @param value An [Arb] of [Long] used to generate the expected and test values.
 * Defaults to [Arb.long()].
 * @param equal A boolean value indicating whether the expected and test values should be equal.
 * If `true`, the expected and test values will be the same.
 * If `false`, distinct values will be generated.
 *
 * @return An [Arb] of [BeEqualToLData] containing Long values and a corresponding [BeEqualTo]
 * requirement.
 */
private fun Arb.Companion.beEqualToData(
    value: Arb<Long> = Arb.long(),
    equal: Boolean = true
) = arbitrary {
    if (equal) {
        value.bind().let { BeEqualToLData(it, it) }
    } else {
        orderedPair(value, strict = true).bind().let { (expected, test) ->
            BeEqualToLData(expected, test)
        }
    }
}

/**
 * Data class used for testing the [BeEqualTo] requirement for Long values.
 * It encapsulates the expected value, test value, and a [BeEqualTo] requirement.
 *
 * @property expected The expected Long value.
 * @property test The value that is being tested against the [expected].
 * @property requirement The [BeEqualTo] requirement that encapsulates the [expected] value.
 */
private data class BeEqualToLData(
    val expected: Long,
    val test: Long,
    val requirement: BeEqualTo = BeEqualTo(expected)
)