/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.arb.datatypes.orderedPair
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.individual
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll

class FitnessMaxRankerTest : FreeSpec({

    "A FitnessMaxRanker" - {
        "should return -1 if the fitness of the first individual is greater than the second" {
            checkAll(
                Arb.genotype(Arb.nothingChromosome()),
                Arb.genotype(Arb.nothingChromosome()),
                Arb.orderedPair(Arb.double(), strict = true)
            ) { g1, g2, (f1, f2) ->
                FitnessMaxRanker<Nothing, NothingGene>()(Individual(g1, f1), Individual(g2, f2)) shouldBe -1
            }
        }

        "should return 1 if the fitness of the first individual is less than the second" {
            checkAll(
                Arb.genotype(Arb.nothingChromosome()),
                Arb.genotype(Arb.nothingChromosome()),
                Arb.orderedPair(Arb.double(), strict = true, reverted = true)
            ) { g1, g2, (f1, f2) ->
                FitnessMaxRanker<Nothing, NothingGene>()(Individual(g1, f1), Individual(g2, f2)) shouldBe 1
            }
        }

        "should return 0 if the fitness of the first individual is equal to the second" {
            checkAll(
                Arb.genotype(Arb.nothingChromosome()),
                Arb.genotype(Arb.nothingChromosome()),
                Arb.double()
            ) { g1, g2, f ->
                FitnessMaxRanker<Nothing, NothingGene>()(Individual(g1, f), Individual(g2, f)) shouldBe 0
            }
        }
    }
})
