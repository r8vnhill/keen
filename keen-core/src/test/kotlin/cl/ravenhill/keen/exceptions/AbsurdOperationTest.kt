/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec


class AbsurdOperationTest : FreeSpec({

    "An Absurd Operation throwable" - {
        "should be able to be thrown" {
            shouldThrow<AbsurdOperation> {
                throw AbsurdOperation
            }
        }
    }
})
