/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.anyRanker
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.operators.arbRouletteWheelSelector
import cl.ravenhill.keen.assertions.should.shouldBeEq
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class RouletteWheelSelectorTest : FreeSpec({

    "A Roulette Wheel Selector instance" - {
        "should have a sorted property that" - {
            "defaults to false" {
                RouletteWheelSelector<Nothing, NothingGene>().sorted shouldBe false
            }

            "is set accordingly to the constructor" {
                RouletteWheelSelector<Nothing, NothingGene>(true).sorted shouldBe true
            }
        }

        "when assigning a probability to each individual" - {
            "should return a list of probabilities that sum 1" {
                checkAll(
                    arbRouletteWheelSelector<Nothing, NothingGene>(),
                    arbPopulation(arbIndividual(Arb.genotype(Arb.nothingChromosome())), 1..100),
                    KeenArb.anyRanker<Nothing, NothingGene>()
                ) { selector, population, ranker ->
                    val probabilities = selector.probabilities(population, ranker)
                    probabilities.sum() shouldBeEq 1.0
                }
            }

            "should assign probabilities proportional to the fitness of the individuals" - {
                withData(
                    SamplePopulation(
                        population = listOf(
                            Individual(Genotype(), 1.0),
                            Individual(Genotype(), 1.0),
                            Individual(Genotype(), 1.0),
                            Individual(Genotype(), 1.0)
                        ),
                        ranker = FitnessMaxRanker(),
                        expected = listOf(0.25, 0.25, 0.25, 0.25)
                    ),
                    SamplePopulation(
                        population = listOf(
                            Individual(Genotype(), 1.0),
                            Individual(Genotype(), 2.0),
                            Individual(Genotype(), 3.0),
                            Individual(Genotype(), 4.0)
                        ),
                        ranker = FitnessMaxRanker(),
                        expected = listOf(0.1, 0.2, 0.3, 0.4)
                    ),
                    SamplePopulation(
                        population = listOf(
                            Individual(Genotype(), 1.0),
                            Individual(Genotype(), 2.0),
                            Individual(Genotype(), 3.0),
                            Individual(Genotype(), 4.0)
                        ),
                        ranker = FitnessMinRanker(),
                        expected = listOf(9.0 / 30, 8.0 / 30, 7.0 / 30, 6.0 / 30)
                    ),
                    SamplePopulation(
                        population = listOf(
                            Individual(Genotype(), -1.0),
                            Individual(Genotype(), 0.0),
                            Individual(Genotype(), 1.0),
                        ),
                        ranker = FitnessMaxRanker(),
                        expected = listOf(0.0, 1.0 / 3, 2.0 / 3)
                    )
                ) { (population, ranker, expected) ->
                    val probabilities = RouletteWheelSelector<Nothing, NothingGene>().probabilities(population, ranker)
                    probabilities shouldBe expected
                }
            }
        }

        "when selecting individuals from a population" - {
            "should return the specified number of individuals" {
                checkAll(
                    arbRouletteWheelSelector<Nothing, NothingGene>(),
                    arbPopulation(arbIndividual(Arb.genotype(Arb.nothingChromosome())), 1..25),
                    Arb.int(0..100),
                    KeenArb.anyRanker<Nothing, NothingGene>()
                ) { selector, population, n, ranker ->
                    selector.select(population, n, ranker).size shouldBe n
                }
            }
        }
    }
})

private data class SamplePopulation(
    val population: List<Individual<Nothing, NothingGene>>,
    val ranker: IndividualRanker<Nothing, NothingGene>,
    val expected: List<Double>,
)