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
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.pair
import io.kotest.property.checkAll

class SyncFitnessMaxRankerTest : FreeSpec({
    "A SyncFitnessMaxRanker" - {
        "when comparing two individuals" - {
            "should return -1 if the first individual's fitness is less than the second individual's fitness" {
                checkAll(
                    arbOrderedPair(Arb.double(includeNonFiniteEdgeCases = false), strict = true)
                        .flatMap { (first, second) ->
                            Arb.pair(
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(first)),
                                arbIndividual(arbSimpleRepresentation(arbSimpleFeature()), Arb.constant(second))
                            )
                        }
                ) { (first, second) ->
                    val ranker = FitnessMaxRanker.sync<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                    ranker(first, second) shouldBe -1
                }
            }

            "should return zero if the first individual's fitness is equal to the second individual's fitness" {
            }

            "should return a positive integer if the first individual's fitness is greater than the second individual's fitness" {
            }
        }
    }
})