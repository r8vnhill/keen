/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class CrossoverInvocationExceptionTest : FreeSpec({

    "CrossoverInvocationException" - {
        "has the correct message" {
            checkAll<String> { message ->
                CrossoverInvocationException(message).message shouldBe message
            }
        }
    }
})
