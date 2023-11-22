/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arbs.evolution.fitnessFunction
import cl.ravenhill.keen.arbs.genetic.chromosomes.intChromosomeFactory
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class EngineFactoryTest : FreeSpec({
    "An Evolution Engine Factory" - {
        "should have a genotype factory that" - {
            "returns the value provided to the constructor" {
                checkAll(Arb.intGenotypeFactory(), Arb.fitnessFunction()) { genotypeFactory, fitnessFunction ->
                    val factory = EvolutionEngine.Factory(fitnessFunction, genotypeFactory)
                    factory.genotypeFactory shouldBe genotypeFactory
                }
            }
        }

        "should have a fitness function that" - {
            "returns the value provided to the constructor" {
                checkAll(Arb.intGenotypeFactory(), Arb.fitnessFunction()) { genotypeFactory, fitnessFunction ->
                    val factory = EvolutionEngine.Factory(fitnessFunction, genotypeFactory)
                    factory.fitnessFunction shouldBe fitnessFunction
                }
            }
        }

        "should have a population size that" - {
            "starts at 50" {
                checkAll(Arb.evolutionEngineFactory(Arb.fitnessFunction(), Arb.intChromosomeFactory(), populationSize = null)) { factory ->
                    factory.populationSize shouldBe 50
                }
            }
        }
    }
})
