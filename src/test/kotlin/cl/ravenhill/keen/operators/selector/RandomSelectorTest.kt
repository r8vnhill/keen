/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.arbs.genetic.intPopulation
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class RandomSelectorTest : FreeSpec({
    "A [RandomSelector]" - {
        "is not sorted" {
            RandomSelector<Int, IntGene>().sorted.shouldBeFalse()
        }

        "when calculating probabilities should return the same value for all individuals" {
            checkAll(
                Arb.intPopulation(),
                Arb.int(0..100),
                Arb.optimizer<Int, IntGene>()
            ) { population, count, optimizer ->
                val probabilities = RandomSelector<Int, IntGene>().probabilities(
                    population,
                    count,
                    optimizer
                )
                probabilities.forEach { it shouldBe 1.0 / population.size }
            }
        }
    }
})
