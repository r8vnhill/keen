package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arbs.evolution.engine
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class EvolutionEngineTest : FreeSpec({

    "An evolution Engine" - {
        "have a generation property that" - {
            "starts at 0" {
                checkAll(Arb.engine()) { engine ->
                    engine.generation shouldBe 0
                }
            }
        }

        "should be able to start the evolution from" - {
            "an empty state" {
                fail("Not implemented yet")
            }

            "a given state" {
                fail("Not implemented yet")
            }
        }
    }
})
