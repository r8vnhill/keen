/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.arbOrderedPair
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class FitnessMaxRankerTest : FreeSpec({

    "A FitnessMaxRanker" - {
        "should return -1 if the fitness of the first individual is greater than the second" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                arbOrderedPair(Arb.double(), strict = true)
            ) { g1, g2, (f1, f2) ->
                FitnessMaxRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(
                    Individual(g1, f1), Individual(g2, f2)
                ) shouldBe -1
            }
        }

        "should return 1 if the fitness of the first individual is less than the second" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                arbOrderedPair(Arb.double(), strict = true, reverted = true)
            ) { g1, g2, (f1, f2) ->
                FitnessMaxRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(
                    Individual(g1, f1), Individual(g2, f2)
                ) shouldBe 1
            }
        }

        "should return 0 if the fitness of the first individual is equal to the second" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                Arb.double()
            ) { g, f ->
                FitnessMaxRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()(
                    Individual(g, f), Individual(g, f)
                ) shouldBe 0
            }
        }
    }
})
