/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.UnfulfilledRequirementException
import cl.ravenhill.keen.any
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


/**
 * Creates a requirement using the provided validator function.
 *
 * @param validator the function that validates the requirement.
 * @return a new [Requirement] object.
 */
private fun requirement(validator: (Any) -> Boolean) = object : Requirement<Any> {
    override val validator: (Any) -> Boolean
        get() = validator

    override fun generateException(description: String) =
        UnfulfilledRequirementException { description }
}

class RequirementTest : FreeSpec({
    "Validating that a condition is met should" - {
        "return a success if the condition is met" {
            checkAll(Arb.any(), Arb.string()) { value, description ->
                val requirement = requirement { true }
                with(requirement.validate(value, description)) {
                    shouldBeSuccess()
                    getOrNull() shouldBe value
                }
            }
        }

        "return a failure if the condition is not met" {
            checkAll(Arb.any(), Arb.string()) { value, description ->
                val requirement = requirement { false }
                with(requirement.validate(value, description)) {
                    shouldBeFailure()
                    exceptionOrNull() shouldBe requirement.generateException(description)
                }
            }
        }
    }

    "Validating that a condition is not met should" - {
        "return a success if the condition is not met" {
            checkAll(Arb.any(), Arb.string()) { value, description ->
                val requirement = requirement { false }
                with(requirement.validateNot(value, description)) {
                    shouldBeSuccess()
                    getOrNull() shouldBe value
                }
            }
        }

        "return a failure if the condition is met" {
            checkAll(Arb.any(), Arb.string()) { value, description ->
                val requirement = requirement { true }
                with(requirement.validateNot(value, description)) {
                    shouldBeFailure()
                    exceptionOrNull() shouldBe requirement.generateException(description)
                }
            }
        }
    }

    "Generating an exception should return an exception with the specified description" {
        checkAll(Arb.string()) { description ->
            with(requirement { true }.generateException(description)) {
                message shouldBe "Unfulfilled constraint: $description"
                shouldBeInstanceOf<UnfulfilledRequirementException>()
            }
        }
    }
})