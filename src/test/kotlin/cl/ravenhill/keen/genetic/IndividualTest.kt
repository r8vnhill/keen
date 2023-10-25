/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arbs.intGenotype
import cl.ravenhill.real
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.doubles.shouldBeNaN
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({
    "A [Individual]" - {
        "when creating a new one" - {
            "without fitness defaults to NaN" {
                checkAll(Arb.intGenotype()) { genotype ->
                    val individual = Individual(genotype)
                    individual.genotype shouldBe genotype
                    individual.fitness.shouldBeNaN()
                }
            }

            "with fitness should have the given fitness" {
                checkAll(Arb.intGenotype(), Arb.real()) { genotype, fitness ->
                    val individual = Individual(genotype, fitness)
                    individual.genotype shouldBe genotype
                    individual.fitness shouldBe fitness
                }
            }
        }
    }
})
