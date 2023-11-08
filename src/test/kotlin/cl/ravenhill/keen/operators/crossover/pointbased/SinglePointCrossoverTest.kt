/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.keen.arbs.probability
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.checkAll

class SinglePointCrossoverTest : FreeSpec({

    "A [SinglePointCrossover] operator" - {
        "when created" - {
            "without an explicit [chromosomeRate] defaults to 1.0" {
                checkAll(Arb.boolean()) { exclusivity ->
                    val crossover = SinglePointCrossover<Nothing, NothingGene>(exclusivity = exclusivity)
                    crossover.chromosomeRate shouldBe 1.0
                    crossover.exclusivity shouldBe exclusivity
                }
            }

            "without an explicit [exclusivity] defaults to false" {
                checkAll(Arb.probability()) { chromosomeRate ->
                    val crossover = SinglePointCrossover<Nothing, NothingGene>(chromosomeRate)
                    crossover.exclusivity.shouldBeFalse()
                    crossover.chromosomeRate shouldBe chromosomeRate
                }
            }
        }
    }
})
