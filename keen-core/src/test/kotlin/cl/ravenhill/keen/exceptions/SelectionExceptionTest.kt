/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
class SelectionExceptionTest : FreeSpec({

    "A Selection Exception" - {
        "can be created with a lazy message" {
            checkAll<String> { message ->
                SelectionException { message }.message shouldBe message
            }
        }
    }
})
