/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.arbIndividual
import cl.ravenhill.keen.arbOrderedPair
import cl.ravenhill.keen.arbPopulation
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class IndividualRankerTest : FreeSpec({

    "An Individual Ranker" - {
        "should have a comparator that works according to the invoke method" {
            checkAll(
                arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))),
                arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))),
            ) { i1, i2 ->
                SimpleRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(i1, i2)
                    .shouldBe(
                        SimpleRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(
                            i1, i2
                        )
                    )
            }
        }

        "when invoked to compare two individuals should" - {
            "return 1 if the first individual is better than the second" {
                checkAll(
                    arbOrderedPair(Arb.double(), strict = true, reverted = true).flatMap {
                        arbIndividual(
                            arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                            Arb.constant(it.first)
                        ).flatMap { i1 ->
                            arbIndividual(
                                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                                Arb.constant(it.second)
                            ).map { i2 -> i1 to i2 }
                        }
                    }) { (i1, i2) ->
                    SimpleRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(
                        i1, i2
                    ) shouldBe 1
                }
            }

            "return -1 if the first individual is better than the second" {
                checkAll(
                    arbOrderedPair(Arb.double(), strict = true).flatMap {
                        arbIndividual(
                            arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                            Arb.constant(it.first)
                        ).flatMap { i1 ->
                            arbIndividual(
                                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                                Arb.constant(it.second)
                            ).map { i2 -> i1 to i2 }
                        }
                    }) { (i1, i2) ->
                    SimpleRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(
                        i1, i2
                    ) shouldBe -1
                }
            }

            "return 0 if the individuals are equal" {
                checkAll(Arb.double().filterNot { it.isNaN() }.flatMap {
                    arbIndividual(
                        arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                        Arb.constant(it)
                    ).flatMap { i1 ->
                        arbIndividual(
                            arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                            Arb.constant(it)
                        ).map { i2 -> i1 to i2 }
                    }
                }) { (i1, i2) ->
                    SimpleRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(
                        i1, i2
                    ) shouldBe 0
                }
            }
        }

        "can sort a population of individuals according to the ranking criteria" {
            checkAll(
                arbPopulation(arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))))
            ) { population ->
                val ranker =
                    SimpleRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()
                val sorted = ranker.sort(population)
                sorted.zipWithNext { i1, i2 ->
                    ranker(i1, i2) shouldBeGreaterThanOrEqualTo 0
                }
            }
        }

        "should have a fitness transformation method that returns the same list" {
            checkAll(Arb.list(Arb.double().filterNot { it.isNaN() })) { fitness ->
                val ranker =
                    SimpleRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()
                ranker.fitnessTransform(fitness) shouldBe fitness
            }
        }
    }
})


private class SimpleRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
    override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
        first.fitness.compareTo(second.fitness)
}
