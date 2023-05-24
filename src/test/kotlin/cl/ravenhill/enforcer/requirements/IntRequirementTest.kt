/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.enforcer.requirements

import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.enforcer.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.enforcer.requirements.IntRequirement.BeAtMost
import cl.ravenhill.enforcer.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.enforcer.requirements.IntRequirement.BeInRange
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import cl.ravenhill.orderedPair
import cl.ravenhill.orderedTriple
import cl.ravenhill.unfulfilledConstraint
import cl.ravenhill.utils.IntToInt
import cl.ravenhill.utils.toRange
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
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
            checkAll(Arb.beInRangeData()) { (range, _, requirement) ->
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

    "A [BeAtLeast] requirement" - {
        "can be converted to a [String]" {
            checkAll(Arb.beAtLeastData()) { (least, _, requirement) ->
                requirement.toString() shouldBe "BeAtLeast { min: $least }"
            }
        }

        "can be created with a minimum value" {
            checkAll(Arb.int()) { least ->
                BeAtLeast(least).least shouldBe least
            }
        }

        "when _validating_ that a value is at least the minimum value should return" - {
            "[true] if it is at least the minimum value" {
                checkAll(Arb.beAtLeastData()) { (_, value, requirement) ->
                    requirement.validator(value).shouldBeTrue()
                }
            }

            "[false] if it is not at least the minimum value" {
                checkAll(Arb.beAtLeastData(above = false)) { (_, value, requirement) ->
                    requirement.validator(value).shouldBeFalse()
                }
            }
        }
    }

    "A [BeAtMost] requirement" - {
        "can be converted to a [String]" {
            checkAll(Arb.beAtMostData()) { (most, _, requirement) ->
                requirement.toString() shouldBe "BeAtMost { max: $most }"
            }
        }

        "can be created with a maximum value" {
            checkAll(Arb.int()) { most ->
                BeAtMost(most).most shouldBe most
            }
        }

        "when _validating_ that a value is at most the maximum value should return" - {
            "[true] if it is at most the maximum value" {
                checkAll(Arb.beAtMostData()) { (_, value, requirement) ->
                    requirement.validator(value).shouldBeTrue()
                }
            }

            "[false] if it is not at most the maximum value" {
                checkAll(Arb.beAtMostData(below = false)) { (_, value, requirement) ->
                    requirement.validator(value).shouldBeFalse()
                }
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
            checkAll(Arb.int()) { expected ->
                BeEqualTo(expected).expected shouldBe expected
            }
        }

        "when _validating_ that a value is equal to the expected value should return" - {
            "[true] if it is equal to the expected value" {
                checkAll(Arb.beEqualToData()) { (_, value, requirement) ->
                    requirement.validator(value).shouldBeTrue()
                }
            }

            "[false] if it is not equal to the expected value" {
                checkAll(Arb.beEqualToData(equal = false)) { (_, value, requirement) ->
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
    element(
        BePositive,
        beInRangeData().bind().requirement,
        beAtLeastData().bind().requirement,
        beAtMostData().bind().requirement
    ).bind()
}

// region : -== BE IN RANGE ==-
/**
 * Generates an [Arb] of [BeInRangeData] given an [Arb]<[Int]> and a boolean value indicating
 * whether the generated test value should be inside the range.
 *
 * @param value An [Arb]<[Int]> used to generate the range of values and the test value. Defaults to [Arb.int()].
 * @param insideRange A boolean value indicating whether the generated test value should be inside
 * the range.
 * If `true`, the test value will be in the range [lo, hi].
 * If `false`, the test value will be either lower than `lo` or higher than `hi`.
 * Defaults to `true`.
 *
 * @param value An [Arb]<[Int]> used to generate the range of values and the test value.
 * Defaults to [Arb.int()].
 * @param insideRange A boolean value indicating whether the generated test value should be inside
 * the range.
 * If `true`, the test value will be in the range [lo, hi]. If `false`, the test value will be
 * either lower than `lo` or higher than `hi`.
 * Defaults to `true`.
 */
private fun Arb.Companion.beInRangeData(
    value: Arb<Int> = Arb.int(),
    insideRange: Boolean = true
) = arbitrary {
    val (lo, mid, hi) = orderedTriple(value, strict = !insideRange).bind()
    if (insideRange) {
        BeInRangeData(lo to hi, mid)
    } else {
        if (boolean().bind()) {
            BeInRangeData(lo to mid, hi)
        } else {
            BeInRangeData(mid to hi, lo)
        }
    }
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
 * @param test The generated value of type Int that may or may not be inside the `range`.
 * @param requirement The [BeInRange] requirement for this data. Defaults to [BeInRange] of the
 * specified range.
 */
data class BeInRangeData(
    val range: IntToInt,
    val test: Int,
    val requirement: BeInRange = BeInRange(range)
)
// endregion BE IN RANGE

// region : -== BE AT LEAST ==-
/**
 * Generates an [Arb] of [BeAtLeastData] given an [Arb]<[Int]> and a boolean value indicating
 * whether the generated test value should be above or equal to the minimum value.
 *
 * @param value An [Arb]<[Int]> used to generate the minimum permissible value and the test value.
 * Defaults to [Arb.int()].
 * @param above A boolean value indicating whether the generated test value should be above
 * (`true`) or equal to (`false`) the minimum value.
 * If `true`, the test value will be greater than the minimum value.
 * If `false`, the test value will be equal to the minimum value.
 * Defaults to `true`.
 *
 * @return An [Arb] of [BeAtLeastData] containing the minimum permissible value and the generated
 * test value.
 */
private fun Arb.Companion.beAtLeastData(
    value: Arb<Int> = Arb.int(),
    above: Boolean = true
) = arbitrary {
    orderedPair(value, strict = !above, reverted = !above).bind().let { (min, mid) ->
        BeAtLeastData(min, mid)
    }
}

/**
 * Data class that encapsulates a minimum permissible value, a test value, and a [BeAtLeast]
 * requirement.
 *
 * @property least The minimum permissible value that [test] should be at least equal to.
 * @property test The value that is being tested against the [least] value.
 * @property requirement The [BeAtLeast] requirement that encapsulates the [least] value, which
 * the [test] value should meet.
 */
data class BeAtLeastData(
    val least: Int,
    val test: Int,
    val requirement: BeAtLeast = BeAtLeast(least)
)
// endregion BE AT LEAST

// region : -== BE AT MOST ==-
/**
 * Generates an [Arb] of [BeAtMostData] given an [Arb]<[Int]> and a boolean value indicating
 * whether the generated test value should be below or equal to the maximum value.
 *
 * @param value An [Arb]<[Int]> used to generate the maximum permissible value and the test value.
 *              Defaults to [Arb.int()].
 * @param below A boolean value indicating whether the generated test value should be below
 * (`true`) or equal to (`false`) the maximum value.
 * If `true`, the test value will be less than the maximum value.
 * If `false`, the test value will be equal to the maximum value.
 *
 * @return An [Arb] of [BeAtMostData] containing the maximum permissible value and the generated
 * test value.
 */
private fun Arb.Companion.beAtMostData(
    value: Arb<Int> = Arb.int(),
    below: Boolean = true
) = arbitrary {
    orderedPair(value, strict = !below, reverted = !below).bind().let { (a, b) ->
        BeAtMostData(a, b)
    }
}

/**
 * This data class is used for testing the [BeAtMost] requirement.
 * It encapsulates the maximum permissible value, a test value, and a [BeAtMost] requirement.
 *
 * @property most The maximum permissible value that [test] should be at most equal to.
 * @property test The value that is being tested against the [most] value.
 * @property requirement The [BeAtMost] requirement that encapsulates the [most] value, which
 * the [test] value should meet.
 */
data class BeAtMostData(
    val most: Int,
    val test: Int,
    val requirement: BeAtMost = BeAtMost(most)
)
// endregion BE AT MOST

// region : -== BE EQUAL TO ==-
/**
 * Generates an [Arb] of [BeEqualToData] given an [Arb]<[Int]> and a boolean value indicating
 * whether the generated test value should be equal to the value.
 *
 * @param value An [Arb]<[Int]> used to generate the value and the test value.
 * Defaults to [Arb.int()].
 * @param equal A boolean value indicating whether the generated test value should be equal to
 * the value.
 * If `true`, the test value will be equal to the value.
 * If `false`, the test value will be different from the value.
 * Defaults to `true`.
 *
 * @return An [Arb] of [BeEqualToData] containing the value and the generated test value.
 */
private fun Arb.Companion.beEqualToData(
    value: Arb<Int> = Arb.int(),
    equal: Boolean = true
) = arbitrary {
    if (equal) {
        value.bind().let { BeEqualToData(it, it) }
    } else {
        orderedPair(value, strict = true).bind().let { (a, b) ->
            BeEqualToData(a, b)
        }
    }
}

/**
 * This data class is used for testing the [BeEqualTo] requirement.
 * It encapsulates the value, a test value, and a [BeEqualTo] requirement.
 *
 * @property value The value that [test] should be equal to.
 * @property test The value that is being tested against the [value].
 * @property requirement The [BeEqualTo] requirement that encapsulates the [value], which
 * the [test] value should meet.
 */
data class BeEqualToData(
    val value: Int,
    val test: Int,
    val requirement: BeEqualTo = BeEqualTo(value)
)
// endregion BE EQUAL TO

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
