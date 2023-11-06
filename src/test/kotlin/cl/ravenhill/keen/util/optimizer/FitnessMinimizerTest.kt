/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.arbs.genetic.intGenotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.arbs.datatypes.orderedPair
import cl.ravenhill.keen.arbs.datatypes.real
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll


class FitnessMinimizerTest : FreeSpec({
    "A [FitnessMinimizer]" - {
        "can compare two individuals when" - {
            "both have the same fitness" {
                checkAll(Arb.intGenotype(), Arb.intGenotype(), Arb.real()) { g1, g2, f ->
                    val i1 = Individual(g1, f)
                    val i2 = Individual(g2, f)
                    FitnessMinimizer<Int, IntGene>().compare(i1, i2) shouldBe 0
                }
            }

            "the first has a higher fitness" {
                checkAll(
                    Arb.intGenotype(),
                    Arb.intGenotype(),
                    Arb.orderedPair(Arb.real(), Arb.real())
                ) { g1, g2, (f1, f2) ->
                    val i1 = Individual(g1, f1)
                    val i2 = Individual(g2, f2)
                    FitnessMinimizer<Int, IntGene>().compare(i1, i2) shouldBe 1
                }
            }

            "the second has a higher fitness" {
                checkAll(
                    Arb.intGenotype(),
                    Arb.intGenotype(),
                    Arb.orderedPair(Arb.real(), Arb.real())
                ) { g1, g2, (f1, f2) ->
                    val i1 = Individual(g1, f1)
                    val i2 = Individual(g2, f2)
                    FitnessMinimizer<Int, IntGene>().compare(i2, i1) shouldBe -1
                }
            }
        }
    }
})
