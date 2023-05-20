/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.DoubleRequirementException
import cl.ravenhill.keen.requirements.DoubleRequirement.BeEqualTo
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.unfulfilledConstraint
import cl.ravenhill.keen.util.contains
import cl.ravenhill.keen.orderedPair
import cl.ravenhill.keen.util.real
import cl.ravenhill.keen.util.toRange
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.negativeDouble
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/**
 * Type alias for a range of `Double` values.
 *
 * A `DoubleRange` represents a range of `Double` values that are bounded on both ends (closed
 * range).
 *
 * ## Examples
 * ### Example 1: Creating a `DoubleRange`
 * ```
 * val range: DoubleRange = 1.0..5.0
 * ```
 * ### Example 2: Checking if a number is in a `DoubleRange`
 * ```
 * val range: DoubleRange = 1.0..5.0
 * val number = 3.0
 * println(number in range) // Prints: true
 * ```
 */
private typealias DoubleRange = ClosedFloatingPointRange<Double>

// region : -== ARBITRARY GENERATORS ==-
/**
 * Generates an arbitrary non-negative `Double` value up to the provided upper limit, excluding NaN
 * and infinity.
 *
 * By default, if no upper limit is provided, it generates values up to `Double.MAX_VALUE`.
 *
 * @receiver The `Arb.Companion` object.
 * @param hi The maximum value that the generated `Double` can take.
 * Defaults to `Double.MAX_VALUE`.
 * @return An [Arb] instance that generates non-negative `Double` values up to the specified limit.
 */
private fun Arb.Companion.nonNegativeReal(hi: Double = Double.MAX_VALUE) = arbitrary {
    double(0.0, hi).next()
}

/**
 * Generates an arbitrary negative `Double` value down to the provided lower limit, excluding NaN
 * and infinity.
 *
 * By default, if no lower limit is provided, it generates values down to `-Double.MAX_VALUE`.
 *
 * @receiver The `Arb.Companion` object.
 * @param lo The minimum value (negative) that the generated `Double` can take.
 * Defaults to `Double.MAX_VALUE`.
 * @return An [Arb] instance that generates negative `Double` values down to the specified limit.
 */
private fun Arb.Companion.negativeReal(lo: Double = -Double.MAX_VALUE) = arbitrary {
    negativeDouble(lo).next()
}

// region : -== REQUIREMENTS ==-
/**
 * Creates an arbitrary for generating a requirement to check if a value is within a specified
 * range.
 */
private fun Arb.Companion.beInRange(
    range: DoubleRange = Double.MIN_VALUE..Double.MAX_VALUE
) = arbitrary {
    val (first, last) = orderedPair(real(range), real(range)).bind()
    BeInRange(first to last)
}

/**
 * Generates an arbitrary [BeEqualTo] instance using provided arbitrary [Double] values for
 * `value` and `tolerance`.
 *
 * @receiver The `Arb.Companion` object.
 * @param value The [Arb] instance for generating `value` in `BeEqualTo`.
 * Defaults to `Arb.real()`.
 * @param tolerance The [Arb] instance for generating `tolerance` in `BeEqualTo`.
 * Defaults to `Arb.nonNegativeReal()`.
 * @return An [Arb] instance that generates `BeEqualTo` instances with arbitrary `value` and
 * `tolerance`.
 *
 * @see Arb.Companion.real
 * @see Arb.Companion.nonNegativeReal
 */
private fun Arb.Companion.beEqualTo(
    value: Arb<Double> = Arb.real(),
    tolerance: Arb<Double> = Arb.nonNegativeReal()
) = arbitrary {
    BeEqualTo(value.bind(), tolerance.bind())
}

/**
 * Creates an arbitrary for generating a requirement for double values.
 */
private fun Arb.Companion.doubleRequirement() = arbitrary {
    choice(beInRange(), beEqualTo()).bind()
}

/**
 * Restricts an arbitrary value generator to a specified range defined by a requirement.
 *
 * ## Examples
 * ### Example 1: Generating a value within a specified range
 * ```
 * val rangeRequirement = Arb.constant(BeInRange(1.0..5.0))
 * val arbDouble = Arb.restrictedToRange(rangeRequirement) { range ->
 *      Arb.double(range.start, range.endInclusive)
 * }
 * val valueInRange = arbDouble.next()
 * println(valueInRange) // Prints: a value that is within the range 1.0 to 5.0, inclusive
 * ```
 * ### Example 2: Using custom value generator within a specified range
 * ```
 * val rangeRequirement = Arb.constant(BeInRange(10.0..50.0))
 * val arbDouble = Arb.restrictedToRange(rangeRequirement) { range ->
 *      Arb.double(range.start * 2, range.endInclusive * 2)
 * }
 * val valueInRange = arbDouble.next()
 * println(valueInRange) // Prints: a value that is within the range 20.0 to 100.0, inclusive
 * ```
 *
 * @param requirement The [Arb] instance representing the requirement of being within a certain
 * range.
 * @param value A function that takes a [ClosedFloatingPointRange] and returns an [Arb] instance
 * for generating values within that range.
 * @return An [Arb] instance that generates values satisfying the specified range requirement.
 */
private fun Arb.Companion.restrictedToRange(
    requirement: Arb<BeInRange>,
    value: (ClosedFloatingPointRange<Double>) -> Arb<Double>
) = arbitrary {
    val req = requirement.bind()
    req to value(req.range.toRange()).bind()
}


/**
 * Generates arbitrary values outside a specified range defined by a requirement.
 *
 * @param requirement The [Arb] instance representing the requirement of being outside a certain
 * range.
 * @param value A function that takes a [ClosedFloatingPointRange] and returns an [Arb] instance
 * for generating values.
 * @return An [Arb] instance that generates values not satisfying the specified range requirement.
 */
private fun Arb.Companion.restrictedToOutsideRange(
    requirement: Arb<BeInRange>,
    value: Arb<Double>
) = arbitrary {
    val req = requirement.bind()
    var potentialValue = value.bind()
    while (potentialValue in req.range) {
        potentialValue = value.bind()
    }
    req to potentialValue
}

/**
 * Generates an arbitrary pair where the first element is a `BeEqualTo` requirement and the second
 * element is a `Double` value that satisfies this requirement.
 *
 * The `Double` value is generated using a provided generator function, `value`, which is invoked
 * with the `expected` value and `tolerance` of the `BeEqualTo` requirement.
 *
 * ## Examples
 * ### Example 1: Generating a pair with `BeEqualTo` requirement and a satisfying `Double` value
 * ```
 * val arbPair = Arb.beEqualToValue(Arb.beEqualTo()) { expected: Double, tolerance: Double ->
 *     Arb.real(expected - tolerance..expected + tolerance)
 * }
 * val pair = arbPair.next()
 * println(pair) // Prints: Pair(BeEqualTo instance, value in range [expected - tolerance, expected + tolerance])
 * ```
 *
 * @receiver The `Arb.Companion` object.
 * @param requirement The [Arb] instance generating the `BeEqualTo` requirements.
 * @param value A function that takes an `expected` `Double` value and `tolerance` `Double`,
 * and returns an [Arb] instance for generating `Double` values within the range defined by the `expected` and `tolerance`.
 * @return An [Arb] instance that generates pairs where the first element is a `BeEqualTo` requirement and the second
 * element is a `Double` value satisfying this requirement.
 */
private fun Arb.Companion.beEqualToValue(
    requirement: Arb<BeEqualTo>,
    value: (expected: Double, tolerance: Double) -> Arb<Double>,
) = arbitrary {
    val req = requirement.bind()
    val expected = value(req.expected, req.tolerance).bind()
    req to expected
}

/**
 * Generates an arbitrary pair where the first element is a `BeEqualTo` requirement and the second
 * element is a `Double` value that lies outside of this requirement's tolerance range.
 *
 * The `Double` value is generated using a provided generator function, `value`, which is invoked
 * with a range that excludes the tolerance range of the `BeEqualTo` requirement.
 *
 * ## Examples
 * ### Example 1:
 * Generating a pair with `BeEqualTo` requirement and a `Double` value outside the tolerance range
 * ```
 * val arbPair = Arb.notBeEqualToValue(Arb.beEqualTo(Arb.real(-1e100, 1e100)) {
 *     Arb.real(it)
 * }
 * val pair = arbPair.next()
 * println(pair) // Prints: Pair(BeEqualTo instance, value not in range [expected - tolerance, expected + tolerance])
 * ```
 *
 * @receiver The `Arb.Companion` object.
 * @param requirement The [Arb] instance generating the `BeEqualTo` requirements.
 * @param value A function that takes a [ClosedFloatingPointRange] and returns an [Arb] instance for generating `Double` values within that range.
 * @return An [Arb] instance that generates pairs where the first element is a `BeEqualTo` requirement and the second
 * element is a `Double` value that lies outside of this requirement's tolerance range.
 */
private fun Arb.Companion.notBeEqualToValue(
    requirement: Arb<BeEqualTo>,
    value: (ClosedFloatingPointRange<Double>) -> Arb<Double>
) = arbitrary {
    val req = requirement.bind()
    val unexpected = Arb.choice(
        value(-Double.MAX_VALUE..req.expected - req.tolerance),
        value(req.expected + req.tolerance..Double.MAX_VALUE)
    ).bind()
    req to unexpected
}
// endregion REQUIREMENTS
// endregion ARBITRARY GENERATORS

class DoubleRequirementTest : FreeSpec({
    "Generating an exception should return a [DoubleRequirementException]" {
        checkAll(Arb.doubleRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<DoubleRequirementException>()
                message shouldBe unfulfilledConstraint(description)
            }
        }
    }

    "A [BeInRange] requirement" - {
        "can be created from" - {
            "a pair of doubles" {
                checkAll(Arb.orderedPair(Arb.real(), Arb.real())) { (first, last) ->
                    val requirement = BeInRange(first to last)
                    requirement.range shouldBe (first to last)
                }
            }

            "a range" {
                checkAll(Arb.orderedPair(Arb.real(), Arb.real())) { (first, last) ->
                    val requirement = BeInRange(first..last)
                    requirement.range shouldBe (first to last)
                }
            }
        }

        "should throw an exception when the first value is greater than the second" {
            checkAll(Arb.orderedPair(Arb.real(), Arb.real())) { (first, last) ->
                shouldThrow<IllegalArgumentException> {
                    BeInRange(last to first)
                }.message shouldBe "The first value in the range must be less than or equal to the second value."
            }
        }

        "when _validating_ that a [Double] is in range should return" - {
            "[true] if it is in range" {
                checkAll(
                    Arb.restrictedToRange(Arb.beInRange()) { Arb.real(it) }
                ) { (requirement, value) ->
                    requirement.validator(value).shouldBeTrue()
                }
            }

            "[false] if it is not in range" {
                checkAll(
                    Arb.restrictedToOutsideRange(Arb.beInRange(-1.0..1.0), Arb.real()),
                ) { (requirement, value) ->
                    requirement.validator(value).shouldBeFalse()
                }
            }
        }

        "can be converted to a string" {
            checkAll(Arb.beInRange()) { requirement ->
                requirement.toString() shouldBe "BeInRange { range: ${requirement.range} }"
            }
        }
    }

    "A [BeEqualTo] requirement" - {
        "can be created with" - {
            "an expected value and a default tolerance" {
                checkAll(Arb.real()) { expected ->
                    val requirement = BeEqualTo(expected)
                    requirement.expected shouldBe expected
                    requirement.tolerance shouldBe 1e-8
                }
            }

            "an expected value and a tolerance" {
                checkAll(Arb.real(), Arb.nonNegativeReal()) { expected, tolerance ->
                    val requirement = BeEqualTo(expected, tolerance)
                    requirement.expected shouldBe expected
                    requirement.tolerance shouldBe tolerance
                }
            }
        }

        "should throw an exception if the tolerance is negative" {
            checkAll(Arb.real(), Arb.negativeReal()) { expected, tolerance ->
                shouldThrow<IllegalArgumentException> {
                    BeEqualTo(expected, tolerance)
                }.message shouldBe "The tolerance must be non-negative."
            }
        }

        "can be converted to a string" {
            checkAll(Arb.real(), Arb.nonNegativeReal()) { expected, tolerance ->
                val requirement = BeEqualTo(expected, tolerance)
                requirement.toString() shouldBe "BeEqualTo { expected: $expected, tolerance: $tolerance }"
            }
        }

        "when validating that a double is equal to the expected value should return a" - {
            "[Success] if the double is equal to the expected value" {
                checkAll(
                    Arb.beEqualToValue(Arb.beEqualTo()) { expected: Double, tolerance: Double ->
                        Arb.real(expected - tolerance..expected + tolerance)
                    },
                    Arb.string()
                ) { (requirement, value), description ->
                    with(requirement.validate(value, description)) {
                        shouldBeSuccess()
                        getOrNull() shouldBe value
                    }
                }
            }

            "[Failure] if the double is not equal to the expected value" {
                checkAll(
                    Arb.notBeEqualToValue(Arb.beEqualTo(Arb.real(-1e-100..1e-100))) { Arb.real(it) },
                    Arb.string()
                ) { (requirement, value), description ->
                    with(requirement.validate(value, description)) {
                        shouldBeFailure()
                        exceptionOrNull() shouldBe requirement.generateException(description)
                    }
                }
            }
        }
    }
})
