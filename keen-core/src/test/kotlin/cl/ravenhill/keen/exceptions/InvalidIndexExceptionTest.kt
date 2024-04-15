package cl.ravenhill.keen.exceptions

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class InvalidIndexExceptionTest : FreeSpec({
    "An InvalidIndexException instance" - {
        "should have a message" {
            InvalidIndexException("message").message shouldBe "message"
        }
    }
})