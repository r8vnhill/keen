package cl.ravenhill.keen

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
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
                val exception = EnforcementException(exceptions)
                exception.message shouldBe
                        "Unfulfilled contract: ${messages.joinToString(", ") { "{ Unfulfilled constraint: $it }" }}"
            }
        }
    }
})

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