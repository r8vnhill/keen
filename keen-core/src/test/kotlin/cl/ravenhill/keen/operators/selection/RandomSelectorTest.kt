/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.anyRanker
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.rngPair
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class RandomSelectorTest : FreeSpec({

    "A Random Selector instance" - {
        "when selecting individuals from a population" - {
            "should return the specified number of individuals" {
                checkAll(
                    arbPopulation(arbIndividual(Arb.genotype(arbNothingChromosome())), 1..25),
                    Arb.int(0..100),
                    KeenArb.anyRanker<Nothing, NothingGene>()
                ) { population, n, ranker ->
                    RandomSelector<Nothing, NothingGene>().select(population, n, ranker).size shouldBe n
                }
            }

            "should return the expected individuals" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    arbPopulation(arbIndividual(Arb.genotype(Arb.intChromosome())), 1..25),
                    Arb.int(0..100),
                    KeenArb.anyRanker<Int, IntGene>(),
                    Arb.rngPair()
                ) { population, n, ranker, (rng1, rng2) ->
                    Domain.random = rng1
                    val selected = RandomSelector<Int, IntGene>().select(population, n, ranker)
                    selected.forEach {
                        it shouldBe population.random(rng2)
                    }
                }
            }
        }
    }
})
