/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.arbs.individual
import cl.ravenhill.keen.arbs.intGenotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class IndividualOptimizerTest : FreeSpec({
    "An [IndividualOptimizer]" - {
        "can be invoked to compare two individuals" {
            checkAll(
                Arb.individual(Arb.intGenotype()),
                Arb.individual(Arb.intGenotype())
            ) { i1, i2 ->
                TestOptimizer()(i1, i2) shouldBe i1.compareTo(i2)
            }
        }

        "have a comparator" {
            checkAll(
                Arb.individual(Arb.intGenotype()),
                Arb.individual(Arb.intGenotype())
            ) { i1, i2 ->
                TestOptimizer().comparator.compare(i1, i2) shouldBe i1.compareTo(i2)
            }
        }

        "can sort a list of individuals" {
            checkAll(Arb.list(Arb.individual(Arb.intGenotype()))) { individuals ->
                val sorted = TestOptimizer().sort(individuals)
                sorted.zipWithNext { i1, i2 ->
                    i1.compareTo(i2) shouldBeGreaterThanOrEqualTo 0
                }
            }
        }
    }
})

/**
 * A test implementation of [IndividualOptimizer] for individuals with genes of type [IntGene].
 *
 * This optimizer compares two individuals based on their natural order using the [Individual.compareTo] method.
 */
private class TestOptimizer : IndividualOptimizer<Int, IntGene> {
    override fun compare(p1: Individual<*, *>, p2: Individual<*, *>) = p1.compareTo(p2)
}
