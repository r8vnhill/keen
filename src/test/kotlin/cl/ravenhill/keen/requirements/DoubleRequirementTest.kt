/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.DoubleRequirementException
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.unfulfilledConstraint
import cl.ravenhill.keen.util.DoubleToDouble
import cl.ravenhill.keen.util.contains
import cl.ravenhill.keen.util.orderedPair
import cl.ravenhill.keen.util.real
import cl.ravenhill.keen.util.toRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
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
// region : -== SHOULD ASSERTIONS ==-
/**
 * Creates a matcher that checks if a double value is within a specified range.
 *
 * @param range the range to check against, represented as a pair of doubles (start to end).
 * @return a matcher for checking if a value is within the specified range.
 */
private fun beInRange(range: DoubleToDouble) = Matcher { value: Double ->
    MatcherResult(
        value in range,
        { "Value $value should be in range $range" },
        { "Value $value should not be in range $range" }
    )
}

/**
 * This function is an infix function that checks if the receiver `Double` is within the given `range`.
 *
 * ## Examples
 * ### Example 1: Checking if a number is in range
 * ```
 * val range = 1.0 to 5.0
 * 3.0 shouldBeInRange range // Passes as 3.0 is within 1.0 to 5.0
 * ```
 * ### Example 2: Checking if a number is out of range
 * ```
 * val range = 1.0 to 5.0
 * 6.0 shouldBeInRange range // Fails as 6.0 is outside 1.0 to 5.0
 * ```
 *
 * @receiver The `Double` value to be checked.
 * @param range The [DoubleToDouble] range to check against.
 */
private infix fun Double.shouldBeInRange(range: DoubleToDouble) = this should beInRange(range)

/**
 * This function is an infix function that checks if the receiver `Double` is not within the given `range`.
 *
 * ## Examples
 * ### Example 1: Checking if a number is in range
 * ```
 * val range = 1.0 to 5.0
 * 3.0 shouldNotBeInRange range // Fails as 3.0 is within 1.0 to 5.0
 * ```
 * ### Example 2: Checking if a number is out of range
 * ```
 * val range = 1.0 to 5.0
 * 6.0 shouldNotBeInRange range // Passes as 6.0 is outside 1.0 to 5.0
 * ```
 *
 * @receiver The `Double` value to be checked.
 * @param range The [DoubleToDouble] range to check against.
 */
private infix fun Double.shouldNotBeInRange(range: DoubleToDouble) =
    this shouldNot beInRange(range)
// endregion SHOULD ASSERTIONS

// region : -== ARBITRARY GENERATORS ==-
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
 * Creates an arbitrary for generating a requirement for double values.
 */
private fun Arb.Companion.doubleRequirement() = arbitrary {
    choice(beInRange()).bind()
}

/**
 * Restricts an arbitrary value generator to a specified range defined by a requirement.
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
    val req = requirement.next()
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
// endregion ARBITRARY GENERATORS ==-

class DoubleRequirementTest : FreeSpec({
    "Generating an exception should return a DoubleRequirementException" {
        checkAll(Arb.doubleRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<DoubleRequirementException>()
                message shouldBe unfulfilledConstraint(description)
            }
        }
    }

    "Validating that a double is in range should" - {
        "return a success if the double is in range" {
            checkAll(
                Arb.restrictedToRange(Arb.beInRange()) { Arb.real(it) },
                Arb.string()
            ) { (requirement, value), description ->
                with(requirement.validate(value, description)) {
                    shouldBeSuccess()
                    getOrNull() shouldBe value
                }
            }
        }

        "return a failure if the double is not in range" {
            checkAll(
                Arb.restrictedToOutsideRange(Arb.beInRange(-1.0..1.0), Arb.real()),
                Arb.string()
            ) { (requirement, value), description ->
                with(requirement.validate(value, description)) {
                    shouldBeFailure()
                    exceptionOrNull() shouldBe requirement.generateException(description)
                }
            }
        }
    }
})
