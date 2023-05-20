/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.enforcer.requirements

import cl.ravenhill.keen.orderedPair
import cl.ravenhill.enforcer.requirements.IntRequirement.BeInRange
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import cl.ravenhill.unfulfilledConstraint
import cl.ravenhill.keen.util.IntToInt
import cl.ravenhill.keen.util.toRange
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class IntRequirementTest : FreeSpec({
    "Generating an exception should return an [IntRequirementException]" {
        checkAll(Arb.intRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<cl.ravenhill.enforcer.IntRequirementException>()
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
            ) { (range, _, requirement) ->
                requirement.toString() shouldBe "BeInRange { range: $range }"
            }
        }

        "can be created from" - {
            "an [IntToInt]" {
                checkAll(Arb.orderedPair(Arb.int())) { range ->
                    BeInRange(range).range shouldBe range
                }
            }

            "an [IntRange]" {
                checkAll(Arb.intRange()) { range ->
                    BeInRange(range).range shouldBe (range.first to range.last)
                }
            }
        }

        "should throw an exception when created from an [IntToInt] with a start value greater than the end value" {
            checkAll(
                Arb.orderedPair(Arb.int(), Arb.int(), strict = true, reverted = true)
            ) { range ->
                shouldThrowWithMessage<IllegalArgumentException>(
                    "The first value in the range [${range.first}] must be less than or equal to the second value [${range.second}]."
                ) {
                    BeInRange(range)
                }
            }
        }

        "when _validating_ that a value is in range should return" - {
            "[true] if it is in range" {
                checkAll(Arb.beInRangeData()) { (_, value, requirement) ->
                    requirement.validator(value).shouldBeTrue()
                }
            }

            "[false] if it is not in range" {
                checkAll(Arb.beInRangeData(insideRange = false)) { (_, value, requirement) ->
                    requirement.validator(value).shouldBeFalse()
                }
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
    element(BePositive, beInRangeData().bind().requirement).bind()
}

/**
 * Generates an arbitrary instance of [BeInRangeData].
 *
 * This function uses the provided `arbRange` to create a range of integer values, represented as an
 * [IntToInt], and then wraps this range and a generated value in a [BeInRangeData] object.
 * If no `arbRange` is provided, the function defaults to generating a range of two arbitrary
 * integers.
 * The generated value can be either inside or outside the range, depending on the `insideRange`
 * parameter.
 *
 * @receiver The `Arb.Companion` object.
 * @param arbRange An [Arb] instance that generates [IntToInt]s.
 * Defaults to generating a range of two arbitrary integers.
 * @param insideRange Whether the generated value should be inside the generated range.
 * Defaults to `true`.
 * @return An [Arb] instance that generates [BeInRangeData]s.
 */
@OptIn(ExperimentalStdlibApi::class)
private fun Arb.Companion.beInRangeData(
    arbRange: Arb<IntToInt> = Arb.orderedPair(Arb.int(-1_000_000, 1_000_000), true),
    insideRange: Boolean = true
) = arbitrary {
    val range = arbRange.bind()
    val value = if (insideRange) {
        int(range.first..range.second)
    } else {
        choice(
            int(Int.MIN_VALUE..<range.first),
            int(range.second + 1..Int.MAX_VALUE)
        )
    }.bind()
    BeInRangeData(range, value)
}

/**
 * Generates an arbitrary [IntRange].
 *
 * This function uses the provided `gen` generator to create a pair of arbitrary integers, which
 * are then used to create an [IntRange].
 * The pair of integers is always ordered, with the smaller integer as the start of the range and
 * the larger integer as the end of the range.
 *
 * @receiver The `Arb.Companion` object.
 * @param gen An [Arb] instance that generates Integers.
 * Defaults to generating arbitrary integers.
 * @return An [Arb] instance that generates [IntRange]s.
 */
private fun Arb.Companion.intRange(gen: Arb<Int> = Arb.int()) = arbitrary {
    orderedPair(gen, true).bind().toRange()
}

/**
 * Data class that encapsulates a range of integer values, represented as a [IntToInt],
 * a value of type Int and a corresponding [BeInRange] requirement.
 *
 * The class provides a convenient way to package a range of values together with a
 * generated value, and the associated [BeInRange] requirement that uses this range.
 *
 * @constructor Creates a [BeInRangeData] instance with a range of integer values specified as an
 * [IntToInt], and a generated value of type Int.
 * @param range The [IntToInt] representing the range of allowed values.
 * @param value The generated value of type Int that may or may not be inside the `range`.
 * @param requirement The [BeInRange] requirement for this data. Defaults to [BeInRange] of the
 * specified range.
 */
data class BeInRangeData(
    val range: IntToInt,
    val value: Int,
    val requirement: BeInRange = BeInRange(range)
)
