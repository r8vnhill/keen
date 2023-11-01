/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.UnfulfilledRequirementException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.arbs.real
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.isSorted
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.doubleArray
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class ProbabilitySelectorTest : FreeSpec({
    "A [ProbabilitySelector]" - {
        "can be created with a sorted flag" {
            DummyProbabilitySelector(true).sorted.shouldBeTrue()
            DummyProbabilitySelector(false).sorted.shouldBeFalse()
        }

        "when performing a serial search of an index" - {
            "should find the first index exceeding the value or return -1 if none is found" {
                checkAll(
                    Arb.doubleArray(Arb.int(0..50), Arb.real()),
                    Arb.boolean(),
                    Arb.long()
                ) { array, sorted, seed ->
                    val random = Random(seed)
                    Core.random = random
                    val sortedArray = array.sortedArray()
                    val selector = DummyProbabilitySelector(sorted)
                    val value = random.nextDouble()
                    val expected = sortedArray.indexOfFirst { it >= value }
                    selector.serialSearchIndex(sortedArray).shouldBe(expected)
                }
            }

            "should throw an exception if the array isn't sorted" {
                checkAll(Arb.doubleArray(Arb.int(0..50), Arb.real())) { array ->
                    assume {
                        array.isSorted().shouldBeFalse()
                    }
                    shouldThrow<EnforcementException> {
                        DummyProbabilitySelector(true).serialSearchIndex(array)
                    }.shouldHaveInfringement<UnfulfilledRequirementException>(
                        unfulfilledConstraint("The array must be sorted")
                    )
                }
            }
        }
    }
}) {

    class DummyProbabilitySelector(sorted: Boolean) :
        AbstractProbabilitySelector<Int, IntGene>(sorted) {

        override fun probabilities(
            population: Population<Int, IntGene>,
            count: Int,
            optimizer: IndividualOptimizer<Int, IntGene>
        ) = DoubleArray(population.size) { 0.0 }
    }
}
