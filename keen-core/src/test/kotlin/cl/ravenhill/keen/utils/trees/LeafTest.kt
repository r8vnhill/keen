/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils.trees

import cl.ravenhill.keen.ExperimentalKeen
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe


@OptIn(ExperimentalKeen::class)
class LeafTest : FreeSpec({

    "A Leaf instance" - {
        "should have an arity of 0" {
            object : Leaf<Any?> {}.arity shouldBe 0
        }
    }
})