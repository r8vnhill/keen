/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import cl.ravenhill.keen.arb.random
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll
import kotlin.random.Random

class DomainTest : FreeSpec({

    afterEach { Domain.random = Random.Default }

    "The Domain" - {
        "should have a random number generator that" - {
            "defaults to Random.Default" {
                Domain.random shouldBe Random.Default
            }

            "can be changed" {
                checkAll(Arb.random()) { random ->
                    Domain.random = random
                    Domain.random shouldBe random
                }
            }
        }
    }
})
