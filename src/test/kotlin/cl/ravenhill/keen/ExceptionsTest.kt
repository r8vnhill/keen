package cl.ravenhill.keen

import cl.ravenhill.enforcer.UnfulfilledRequirementException
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ExceptionsTest : FreeSpec({
    "A KeenException" - {
        "can be created with a prefix and a message" {
            checkAll<String, String> { prefix, message ->
                val exception = KeenException(prefix) { message }
                exception.message shouldBe "$prefix $message"
            }
        }
    }

    "An InvalidStateException" - {
        "can be created with a state and a message" {
            checkAll<String, String> { state, message ->
                val exception = InvalidStateException(state) { message }
                exception.message shouldBe "Invalid state ($state): $message"
            }
        }

        "equality" - {
            "is true for another InvalidStateException with the same state and message" {
                checkAll<String, String> { state, message ->
                    val exception = InvalidStateException(state) { message }
                    val other = InvalidStateException(state) { message }
                    exception shouldBe other
                }
            }

            "is false for another InvalidStateException with a different state" {
                checkAll(Arb.uniqueStrings(2..2), Arb.string()) { states, message ->
                    val exception = InvalidStateException(states[0]) { message }
                    val other = InvalidStateException(states[1]) { message }
                    exception shouldNotBe other
                }
            }

            "is false for another InvalidStateException with a different message" {
                checkAll(Arb.string(), Arb.uniqueStrings(2..2)) { state, messages ->
                    val exception = InvalidStateException(state) { messages[0] }
                    val other = InvalidStateException(state) { messages[1] }
                    exception shouldNotBe other
                }
            }
        }

        "hashing" - {
            "is the same for another InvalidStateException with the same state and message" {
                checkAll<String, String> { state, message ->
                    val exception = InvalidStateException(state) { message }
                    val other = InvalidStateException(state) { message }
                    exception.hashCode() shouldBe other.hashCode()
                }
            }

            "is different for another InvalidStateException with a different state" {
                checkAll(Arb.uniqueStrings(2..2), Arb.string()) { states, message ->
                    val exception = InvalidStateException(states[0]) { message }
                    val other = InvalidStateException(states[1]) { message }
                    exception.hashCode() shouldNotBe other.hashCode()
                }
            }

            "is different for another InvalidStateException with a different message" {
                checkAll(Arb.string(), Arb.uniqueStrings(2..2)) { state, messages ->
                    val exception = InvalidStateException(state) { messages[0] }
                    val other = InvalidStateException(state) { messages[1] }
                    exception.hashCode() shouldNotBe other.hashCode()
                }
            }
        }
    }

    "Requirement exceptions" - {
        "can be created with a requirement and a message" {
            checkAll(Arb.requirementException()) { (exception, message) ->
                exception().message shouldBe "Unfulfilled constraint: $message"
            }
        }
    }

    "An EnforcementException" - {
        "can be created with a list of violations" {
            checkAll(Arb.list(Arb.requirementException())) { violations ->
                val exceptions = violations.map { it.first() }
                val messages = violations.map { it.second }
                val exception = cl.ravenhill.enforcer.EnforcementException(exceptions)
                exception.message shouldBe
                        "Unfulfilled contract: ${messages.joinToString(", ") { "{ Unfulfilled constraint: $it }" }}"
            }
        }
    }
})

private fun Arb.Companion.requirementException() = arbitrary {
    val message = string().bind()
    element(
        { cl.ravenhill.enforcer.IntRequirementException { message } } to message,
        { cl.ravenhill.enforcer.LongRequirementException { message } } to message,
        { cl.ravenhill.enforcer.PairRequirementException { message } } to message,
        { cl.ravenhill.enforcer.DoubleRequirementException { message } } to message,
        { cl.ravenhill.enforcer.CollectionRequirementException { message } } to message,
        { UnfulfilledRequirementException { message } } to message).bind()
}