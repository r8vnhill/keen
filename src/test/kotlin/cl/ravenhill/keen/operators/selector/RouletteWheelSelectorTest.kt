/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.fitness
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.sub
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.math.min

class RouletteWheelSelectorTest : FreeSpec({
    "A [RouletteWheelSelector]" - {
        "can be sorted" {
            RouletteWheelSelector<Int, IntGene>().sorted.shouldBeFalse()
            RouletteWheelSelector<Int, IntGene>(true).sorted.shouldBeTrue()
            RouletteWheelSelector<Int, IntGene>(false).sorted.shouldBeFalse()
        }

        "when calculating probabilities" - {
            "should return probabilities proportional to the fitness of the individuals" {
                checkAll(
                    Arb.population(),
                    Arb.int(0..100),
                    Arb.optimizer<Int, IntGene>()
                ) { population, count, optimizer ->
                    val selector = RouletteWheelSelector<Int, IntGene>()
                    val probabilities =
                        selector.probabilities(population, count, optimizer)
                    val fitness = population.fitness.let { it sub min(it.min(), 0.0) }
                    val cums = fitness.reduce { acc, d -> acc + d }
                    if (cums == 0.0) {
                        probabilities.forEach { it shouldBe 1.0 / population.size }
                    } else {
                        probabilities.forEachIndexed { i, d ->
                            d shouldBe (fitness[i] / cums)
                        }
                    }
                }
            }
        }
    }
})
