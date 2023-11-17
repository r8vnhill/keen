package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evolutionState
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.intPopulation
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

class EvolutionEngineTest : FreeSpec({
    "An evolution Engine" - {
        "should be able to start the evolution from" - {
            "an empty state" {
                checkAll(
                    Arb.long().compose {
                        Core.random = Random(it)
                        Arb.evolutionState<Int, IntGene>(Arb.constant(emptyList()))
                    },
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { (seed, state), engine ->
                    with(engine.startEvolution(state)) {
                        generation shouldBe state.generation
                        population.size shouldBe engine.populationSize
                        Core.random = Random(seed)
                        population shouldBe List(engine.populationSize) { Individual(engine.genotypeFactory.make()) }
                    }
                }
            }

            "a given state" {
                checkAll(
                    Arb.evolutionState(Arb.intPopulation()),
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { state, engine ->
                    val result = engine.startEvolution(state)
                    result shouldBe state
                }
            }
        }
    }
})
