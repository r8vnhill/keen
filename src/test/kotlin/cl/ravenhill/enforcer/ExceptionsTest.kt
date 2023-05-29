/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.enforcer

import cl.ravenhill.unfulfilledConstraint
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/***************************************************************************************************
 * The ExceptionsTest class is a suite of tests for the custom exceptions used by an enforcement
 * mechanism in the system.
 * It includes tests for various types of requirement exceptions, such as IntRequirementException,
 * LongRequirementException, PairRequirementException, DoubleRequirementException, and
 * CollectionRequirementException.
 * The test suite checks that these exceptions can be successfully created with a requirement and a
 * message, and the message is correctly included in the exception.
 * Furthermore, the suite tests the EnforcementException, verifying that it can be created with a
 * list of violations, and the resulting exception's message correctly formats the messages from the
 * violations.
 * An accompanying helper function Arb.Companion.requirementException() is defined to generate
 * instances of these exceptions for testing.
 **************************************************************************************************/

/**
 * Tests for the exceptions used by the enforcer.
 *
 * @see Arb.Companion.requirementException
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 *
 */
class ExceptionsTest : FreeSpec({
    "Requirement exceptions" - {
        "can be created with a requirement and a message" {
            checkAll(Arb.requirementException()) { (exception, message) ->
                exception().message shouldBe unfulfilledConstraint(message)
            }
        }
    }

    "An EnforcementException" - {
        "can be created with a list of violations" {
            checkAll(Arb.list(Arb.requirementException())) { violations ->
                val exceptions = violations.map { it.first() }
                val messages = violations.map { it.second }
                val exception = EnforcementException(exceptions)
                exception.message shouldBe
                        "Unfulfilled contract: [ " +
                        messages.joinToString(", ") {
                            "{ ${unfulfilledConstraint(it)} }"
                        } + " ]"
            }
        }
    }
})

/**
 * Generates an arbitrary instance of a subclass of [UnfulfilledRequirementException].
 *
 * @receiver The `Arb.Companion` object.
 * @return An [Arb] instance that generates pairs where the first component is a
 * [UnfulfilledRequirementException] and the second component is the message associated with the
 * exception.
 *
 * @see UnfulfilledRequirementException
 * @see IntRequirementException
 * @see LongRequirementException
 * @see PairRequirementException
 * @see DoubleRequirementException
 * @see CollectionRequirementException
 */
private fun Arb.Companion.requirementException() = arbitrary {
    val message = string().bind()
    element(
        { IntRequirementException { message } } to message,
        { LongRequirementException { message } } to message,
        { PairRequirementException { message } } to message,
        { DoubleRequirementException { message } } to message,
        { CollectionRequirementException { message } } to message,
        { UnfulfilledRequirementException { message } } to message).bind()
}
