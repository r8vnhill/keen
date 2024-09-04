/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.SimpleFeature
import cl.ravenhill.keen.SimpleRepresentation
import cl.ravenhill.keen.arbFitnessMinRanker
import cl.ravenhill.keen.arbSimpleFeature
import cl.ravenhill.keen.arbSimpleRepresentation
import cl.ravenhill.keen.utils.SortingStrategy
import cl.ravenhill.utils.arbIndividual
import cl.ravenhill.utils.arbOrderedPair
import cl.ravenhill.keen.arbPopulation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.pair
import io.kotest.property.checkAll

class FitnessMinRankerTest : FreeSpec({
    "A FitnessMinRanker" - {
        "when comparing two individuals" - {
            "should return 1 if the first individual's fitness is less than the second individual's fitness" {
                checkAll(
                    arbOrderedPair(Arb.double(includeNonFiniteEdgeCases = false), strict = true)
                        .flatMap { (first, second) ->
                            Arb.pair(
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(first)),
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(second))
                            )
                        },
                    arbFitnessMinRanker<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                ) { (first, second), ranker ->
                    ranker(first, second) shouldBe 1
                }
            }

            "should return zero if the first individual's fitness is equal to the second individual's fitness" {
                checkAll(
                    Arb.double(includeNonFiniteEdgeCases = false)
                        .flatMap { fitness ->
                            Arb.pair(
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(fitness)),
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(fitness))
                            )
                        },
                    arbFitnessMinRanker<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                ) { (first, second), ranker ->
                    ranker(first, second) shouldBe 0
                }
            }

            "should return -1 if the first individual's fitness exceeds the second's" {
                checkAll(
                    arbOrderedPair(Arb.double(includeNonFiniteEdgeCases = false), strict = true)
                        .flatMap { (first, second) ->
                            Arb.pair(
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(second)),
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(first))
                            )
                        },
                    arbFitnessMinRanker<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                ) { (first, second), ranker ->
                    ranker(first, second) shouldBe -1
                }
            }
        }

        "when sorting a population" - {
            "in ascending order" - {
                "should return the population sorted by fitness in ascending order" {
                    checkAll(
                        arbPopulation(arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.double())),
                        arbFitnessMinRanker<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                    ) { population, ranker ->
                        val sorted = ranker.sort(population)
                        sorted.zipWithNext { first, second ->
                            first.fitness >= second.fitness
                        }
                    }
                }
            }

            "in descending order" - {
                "should return the population sorted by fitness in descending order" {
                    checkAll(
                        arbPopulation(arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.double())),
                        arbFitnessMinRanker<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                    ) { population, ranker ->
                        val sorted = ranker.sort(population, SortingStrategy.DESCENDING)
                        sorted.zipWithNext { first, second ->
                            first.fitness <= second.fitness
                        }
                    }
                }
            }

            "without ordering" - {
                "should return the population unsorted" {
                    checkAll(
                        arbPopulation(arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.double()), 1..20),
                        arbFitnessMinRanker<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                    ) { population, ranker ->
                        val sorted = ranker.sort(population, SortingStrategy.UNSORTED)
                        sorted.forEachIndexed { index, individual ->
                            individual shouldBe population[index]
                        }
                    }
                }
            }
        }
    }
})
