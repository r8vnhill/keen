package cl.ravenhill.keen.exceptions

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CrossoverExceptionTest : FreeSpec({
    "A Crossover Exception" - {
        "should have the expected message" {
            val message = "This is a test message"
            val exception = CrossoverException(message)
            exception.message shouldBe message
        }
    }
})
