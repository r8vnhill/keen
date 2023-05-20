/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.IntRequirementException
import cl.ravenhill.keen.orderedPair
import cl.ravenhill.keen.requirements.IntRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.unfulfilledConstraint
import cl.ravenhill.keen.util.IntToInt
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
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
                Arb.intToIntBeInRangeData(Arb.orderedPair(Arb.int(), Arb.int()))
            ) { (range, requirement) ->
                requirement.toString() shouldBe "BeInRange { range: $range }"
            }
        }

        "can be created from" - {
            "an [IntToInt]" {
                checkAll(Arb.intToIntBeInRangeData()) { (range, requirement) ->
                    requirement.range shouldBe range
                }
            }

            "an [IntRange]" {
                checkAll(Arb.intRangeBeInRangeData()) { (range, requirement) ->
                    requirement.range shouldBe range
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
    }
})

/**
 * Generates an arbitrary instance of [IntRequirement].
 *
 * @receiver The `Arb.Companion` object.
 * @return An [Arb] instance that generates [IntRequirement]s.
 */
private fun Arb.Companion.intRequirement() = arbitrary {
    element(BePositive, intToIntBeInRangeData().bind().requirement).bind()
}

/**
 * Generates an arbitrary instance of [BeInRangeData].
 *
 * This function uses the provided `range` generator to create a range of integer values,
 * represented as a [IntToInt], and then wraps this range in a [BeInRangeData] object.
 * If no `range` generator is provided, the function defaults to generating a range of
 * two arbitrary integers.
 *
 * @receiver The `Arb.Companion` object.
 * @param range An [Arb] instance that generates [IntToInt]s.
 * Defaults to generating a range of two arbitrary integers.
 * @return An [Arb] instance that generates [BeInRangeData]s.
 */
private fun Arb.Companion.intToIntBeInRangeData(
    range: Arb<IntToInt> = Arb.orderedPair(Arb.int(), Arb.int(), true)
) = arbitrary {
    BeInRangeData(range.bind())
}

/**
 * Generates an arbitrary instance of [BeInRangeData].
 *
 * This function uses the provided `range` generator to create a range of integer values,
 * represented as an [IntRange], and then wraps this range in a [BeInRangeData] object.
 *
 * @receiver The `Arb.Companion` object.
 * @param range An [Arb] instance that generates [IntRange]s.
 * Defaults to an arbitrary [IntRange].
 * @return An [Arb] instance that generates [BeInRangeData]s.
 */
private fun Arb.Companion.intRangeBeInRangeData(
    range: Arb<IntRange> = Arb.intRange()
) = arbitrary {
    BeInRangeData(range.bind())
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
    val (first, last) = Arb.orderedPair(gen, gen, true).bind()
    first..last
}

/**
 * Data class that encapsulates a range of integer values, represented as a [IntToInt],
 * and a corresponding [BeInRange] requirement.
 *
 * The class provides a convenient way to package a range of values together with the
 * associated [BeInRange] requirement that uses this range.
 *
 * @property range The range of values that are allowed, represented as a [IntToInt].
 * @property requirement A [BeInRange] requirement.
 *
 * @constructor Creates a [BeInRangeData] instance with a range of integer values specified as an
 * [IntToInt].
 * @param range The [IntToInt] of allowed values.
 * @param requirement The [BeInRange] requirement for this data. Defaults to [BeInRange] of the
 * specified range.
 */
data class BeInRangeData(val range: IntToInt, val requirement: BeInRange = BeInRange(range)) {

    /**
     * Creates a [BeInRangeData] instance with a range of integer values specified as an
     * [IntRange].
     *
     * @param range The [IntRange] of allowed values.
     */
    constructor(range: IntRange) : this(range.first to range.last, BeInRange(range))
}
