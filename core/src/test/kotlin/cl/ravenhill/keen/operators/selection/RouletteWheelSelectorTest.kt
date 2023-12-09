/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class RouletteWheelSelectorTest : FreeSpec({

    "A RouletteWheelSelector" - {
        "should assign the correct probabilities to individuals when" - {
            "the fitness values are all equal" {
                val population = listOf(
                    Individual(Genotype<Nothing, NothingGene>(), 1.0),
                    Individual(Genotype(), 1.0),
                    Individual(Genotype(), 1.0)
                )
                val selector = RouletteWheelSelector<Nothing, NothingGene>()
                val probabilities = selector.probabilities(population,)
                probabilities shouldBe listOf(1 / 3.0, 1 / 3.0, 1 / 3.0)
            }

            "the fitness values are all different" {
                val population = listOf(
                    Individual(Genotype<Nothing, NothingGene>(), 1.0),
                    Individual(Genotype(), 2.0),
                    Individual(Genotype(), 3.0)
                )
                val selector = RouletteWheelSelector<Nothing, NothingGene>()
                val probabilities = selector.probabilities(population,)
                probabilities shouldBe listOf(1 / 6.0, 2 / 6.0, 3 / 6.0)
            }

            "the fitness values are all equal except for one" {
                val population = listOf(
                    Individual(Genotype<Nothing, NothingGene>(), 1.0),
                    Individual(Genotype(), 1.0),
                    Individual(Genotype(), 1.0),
                    Individual(Genotype(), 2.0)
                )
                val selector = RouletteWheelSelector<Nothing, NothingGene>()
                val probabilities = selector.probabilities(population,)
                probabilities shouldBe listOf(1 / 4.0, 1 / 4.0, 1 / 4.0, 1 / 4.0)
            }

            "there's negative fitness values" {
                val population = listOf(
                    Individual(Genotype<Nothing, NothingGene>(), -1.0),
                    Individual(Genotype(), 0.0),
                    Individual(Genotype(), 1.0)
                )
                val selector = RouletteWheelSelector<Nothing, NothingGene>()
                val probabilities = selector.probabilities(population,)
                probabilities shouldBe listOf(0.0, 1 / 3.0, 2 / 3.0)
            }
        }
    }
})
