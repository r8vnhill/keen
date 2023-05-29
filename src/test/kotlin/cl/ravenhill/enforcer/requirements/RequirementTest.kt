/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.enforcer.requirements

import cl.ravenhill.enforcer.UnfulfilledRequirementException
import cl.ravenhill.any
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