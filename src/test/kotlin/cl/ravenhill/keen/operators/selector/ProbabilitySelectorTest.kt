/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.incremental
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

class ProbabilitySelectorTest : FreeSpec({
    "A [ProbabilitySelector]" - {
        "can be created with a sorted flag" {
            DummyProbabilitySelector(true).sorted.shouldBeTrue()
            DummyProbabilitySelector(false).sorted.shouldBeFalse()
        }

        "can select a random individual based on the probabilities" {
            checkAll(
                Arb.population(),
                Arb.int(1..5),
                Arb.optimizer<Int, IntGene>(),
                Arb.boolean(),
                Arb.long()
            ) { population, count, optimizer, sorted, seed ->
                Core.random = Random(seed)
                val random = Random(seed)
                val pop = if (sorted) optimizer.sort(population) else population
                val selector = DummyProbabilitySelector(sorted)
                val probabilities = selector.probabilities(pop, count, optimizer)
                probabilities.incremental()
                val expected = List(count) {
                    pop[probabilities.indexOfFirst { random.nextDouble() <= it }]
                }
                val actual = selector.select(population, count, optimizer)
                actual shouldBe expected
            }
        }
    }
}) {

    class DummyProbabilitySelector(
        override val sorted: Boolean
    ) : AbstractSelector<Int, IntGene>(),
        ProbabilitySelector<Int, IntGene> {

        override fun probabilities(
            population: Population<Int, IntGene>,
            count: Int,
            optimizer: IndividualOptimizer<Int, IntGene>
        ): DoubleArray {
            val values = DoubleArray(population.size) { Random(11).nextDouble() }
            val sum = values.sum()

            return values.map { it / sum }.toDoubleArray()
        }
    }
}
