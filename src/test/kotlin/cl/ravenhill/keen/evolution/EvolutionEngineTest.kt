package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evolutionState
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.intPopulation
import cl.ravenhill.keen.arbs.operators.intAlterer
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class EvolutionEngineTest : FreeSpec({
    "An evolution Engine" - {
        "should be able to start the evolution from" - {
            "an empty state" {
                fail("Not implemented yet")
            }

            "a given state" {
                checkAll(
                    Arb.evolutionState(Arb.intPopulation()),
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { state, engine ->
                    val result = engine.startEvolution(state)
                    result shouldNotBe state
                }
            }
        }
    }
})