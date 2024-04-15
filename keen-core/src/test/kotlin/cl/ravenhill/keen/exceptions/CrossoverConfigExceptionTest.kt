package cl.ravenhill.keen.exceptions

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class CrossoverConfigExceptionTest : FreeSpec({

    "A CrossoverConfigException instance" - {
        "should have a message" {
            checkAll<String> {
                CrossoverConfigException(it).message shouldBe it
            }
        }
    }
})