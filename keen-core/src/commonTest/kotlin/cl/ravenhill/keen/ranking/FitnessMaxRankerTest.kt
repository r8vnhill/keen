/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.SimpleRepresentation
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import cl.ravenhill.utils.arbIndividual
import cl.ravenhill.utils.arbOrderedPair
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.pair
import io.kotest.property.checkAll

class FitnessMaxRankerTest : FreeSpec({
    "A FitnessMaxRanker" - {
        "when comparing two individuals" - {
            "should return -1 if the first individual's fitness is less than the second individual's fitness" {
                checkAll(
                    arbOrderedPair(Arb.double(includeNonFiniteEdgeCases = false), strict = true)
                        .flatMap { (first, second) ->
                            Arb.pair(
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(first)),
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(second))
                            )
                        },
                    arbFitnessMaxRanker()
                ) { (first, second), ranker ->
                    ranker(first, second) shouldBe -1
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
                    arbFitnessMaxRanker()
                ) { (first, second), ranker ->
                    ranker(first, second) shouldBe 0
                }
            }

            "should return a positive integer if the first individual's fitness exceeds the second's" {
                checkAll(
                    arbOrderedPair(Arb.double(includeNonFiniteEdgeCases = false), strict = true)
                        .flatMap { (first, second) ->
                            Arb.pair(
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(second)),
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(first))
                            )
                        },
                    arbFitnessMaxRanker()
                ) { (first, second), ranker ->
                    ranker(first, second) shouldBe 1
                }
            }
        }

        "when sorting a population"
    }
})

/**
 * Generates an arbitrary fitness maximization ranker for testing purposes.
 *
 * @return An `Arb` that randomly selects between a synchronous or asynchronous fitness maximization ranker.
 */
fun arbFitnessMaxRanker() =
    Arb.element(FitnessMaxRanker.sync<_, _, SimpleRepresentation<Int, SimpleFeature>>(), FitnessMaxRanker.async())
