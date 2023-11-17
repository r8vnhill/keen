package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class EngineTest : FreeSpec({

    "An evolution Engine" - {
        "have a generation property that" - {
            "starts at 0" {
                    engine.generation shouldBe 0
                }
            }
        }

        "should be able to start the evolution from" - {
            "an empty state" {
                fail("Not implemented yet")
            }

            "a given state" {
                checkAll(Arb.engine<Int, IntGene>(Arb.intGenotypeFactory(), Arb.intAlterer()
            }
        }
    }
})
