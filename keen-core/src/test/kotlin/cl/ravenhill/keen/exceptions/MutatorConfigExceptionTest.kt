/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class MutatorConfigExceptionTest : FreeSpec({

    "A MutatorConfigException instance" - {
        "should have a message" {
            checkAll<String> { message ->
                MutatorConfigException(message).message shouldBe message
            }
        }
    }
})
