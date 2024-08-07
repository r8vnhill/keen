/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.arb.datatypes.arbOrderedPair
import cl.ravenhill.keen.arb.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.arbRanker
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll

class IndividualRankerTest : FreeSpec({

    "An Individual Ranker" - {
        "should have a comparator that works according to the invoke method" {
            val individualArb = arbIndividual(Arb.genotype(arbNothingChromosome()))
            checkAll(individualArb, individualArb, arbRanker<Nothing, NothingGene>()) { i1, i2, ranker ->
                ranker.comparator.compare(i1, i2) shouldBe ranker(i1, i2)
            }
        }

        "when invoked to compare two individuals should" - {
            "return 1 if the first individual is better than the second" {
                checkAll(
                    arbOrderedPair(Arb.double(), strict = true, reverted = true).map {
                        arbIndividual(Arb.genotype(), Arb.constant(it.first)).next() to
                              arbIndividual(Arb.genotype(), Arb.constant(it.second)).next()
                    }) { (i1, i2) ->
                    DummyRanker()(i1, i2) shouldBe 1
                }
            }

            "return -1 if the first individual is better than the second" {
                checkAll(
                    arbOrderedPair(Arb.double(), strict = true).map {
                        arbIndividual(Arb.genotype(), Arb.constant(it.first)).next() to
                              arbIndividual(Arb.genotype(), Arb.constant(it.second)).next()
                    }) { (i1, i2) ->
                    DummyRanker()(i1, i2) shouldBe -1
                }
            }

            "return 0 if the individuals are equal" {
                checkAll(Arb.double().filterNot { it.isNaN() }.map {
                    arbIndividual(Arb.genotype(), Arb.constant(it)).next() to
                          arbIndividual(Arb.genotype(), Arb.constant(it)).next()
                }) { (i1, i2) ->
                    DummyRanker()(i1, i2) shouldBe 0
                }
            }
        }

        "can sort a population of individuals according to the ranking criteria" {
            checkAll(arbPopulation(arbIndividual(Arb.genotype(arbChromosome())))) { population ->
                val sorted = DummyRanker().sort(population)
                sorted.zipWithNext { i1, i2 ->
                    DummyRanker()(i1, i2) shouldBeGreaterThanOrEqualTo 0
                }
            }
        }

        "should have a fitness transformation method that returns the same list" {
             checkAll(arbRanker<Nothing, NothingGene>(), Arb.list(Arb.double())) { ranker, fitness ->
                 ranker.fitnessTransform(fitness) shouldBe fitness
             }
        }
    }
})

private class DummyRanker : IndividualRanker<Int, DummyGene> {
    override fun invoke(first: Individual<Int, DummyGene>, second: Individual<Int, DummyGene>) =
        first.fitness.compareTo(second.fitness)
}
