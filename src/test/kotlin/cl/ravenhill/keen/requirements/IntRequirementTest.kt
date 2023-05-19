/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.IntRequirementException
import cl.ravenhill.keen.requirements.IntRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.unfulfilledConstraint
import cl.ravenhill.keen.util.IntToInt
import cl.ravenhill.keen.util.orderedPair
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class IntRequirementTest : FreeSpec({
    "Generating an exception should return an [IntRequirementException]" {
        checkAll(Arb.intRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<IntRequirementException>()
                message shouldBe unfulfilledConstraint(description)
            }
        }
    }

    "A [BePositive] requirement" - {
        "can be converted to a [String]" {
            BePositive.toString() shouldBe "BePositive"
        }

        "when _validating_ that a value is positive should return" - {
            "[true] if it is positive" {
                checkAll(Arb.positiveInt()) { value ->
                    BePositive.validator(value).shouldBeTrue()
                }
            }

            "[false] if it is not positive" {
                checkAll(Arb.nonPositiveInt()) { value ->
                    BePositive.validator(value).shouldBeFalse()
                }
            }
        }
    }

    "A [BeInRange] requirement" - {
        "can be converted to a [String]" {
            checkAll(
                Arb.beInRangeData(Arb.orderedPair(Arb.int(), Arb.int()))
            ) { (requirement, range) ->

            }
        }
    }
})

/**
 * Generates an arbitrary instance of [IntRequirement].
 *
 * @receiver The `Arb.Companion` object.
 * @return An [Arb] instance that generates [IntRequirement]s.
 */
private fun Arb.Companion.intRequirement() = arbitrary {
    choice(constant(BePositive)).bind()
}

private fun Arb.Companion.beInRangeData(
    range: Arb<IntToInt> = Arb.orderedPair(Arb.int(), Arb.int())
) = arbitrary {
    BeInRangeData(range.bind())
}

/**
 * Data class that encapsulates a range of integer values, represented as a [IntToInt],
 * and a corresponding [BeInRange] requirement.
 *
 * The class provides a convenient way to package a range of values together with the
 * associated [BeInRange] requirement that uses this range.
 * The class also overrides the `component2` function to return the `requirement` property,
 * which allows for destructuring declarations.
 *
 * @property range The range of values that are allowed, represented as a [IntToInt].
 * @property requirement A [BeInRange] requirement that uses the [range].
 */
data class BeInRangeData(val range: IntToInt) {
    val requirement = BeInRange(range)

    /**
     * Returns the [requirement] property when this class is destructured.
     */
    operator fun component2() = requirement
}