/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.CollectionRequirementException
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.assume
import io.kotest.property.checkAll

class SelectorTest : FreeSpec({
    "A [Selector]" - {
        "should throw an exception when" - {
            "the population size is empty" {
                checkAll(Arb.nonNegativeInt()) { count ->
                    shouldThrow<EnforcementException> {
                        DummySelector()(listOf(), count, FitnessMinimizer())
                    }.shouldHaveInfringement<CollectionRequirementException>(
                        unfulfilledConstraint("Population size [0] must be at least 1")
                    )
                }
            }

            "the selection count is negative" {
                checkAll(
                    Arb.population(),
                    Arb.negativeInt(),
                    Arb.optimizer<Int, IntGene>()
                ) { population, count, optimizer ->
                    shouldThrow<EnforcementException> {
                        DummySelector()(population, count, optimizer)
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint(
                            "Selection count [$count] must be at least 0"
                        )
                    )
                }
            }
        }

        "should return [count] individuals from the population" {
            checkAll(
                Arb.population(),
                Arb.int(0..5),
                Arb.optimizer<Int, IntGene>()
            ) { population, count, optimizer ->
                assume { count shouldBeLessThanOrEqualTo population.size }
                DummySelector()(population, count, optimizer).size shouldBe count
            }
        }
    }
}) {

    class DummySelector : AbstractSelector<Int, IntGene>() {
        override fun select(
            population: Population<Int, IntGene>,
            count: Int,
            optimizer: IndividualOptimizer<Int, IntGene>
        ) = population.take(count)
    }
}