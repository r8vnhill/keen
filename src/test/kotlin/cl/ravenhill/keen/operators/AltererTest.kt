/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.utils.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.negativeDouble
import io.kotest.property.assume
import io.kotest.property.checkAll

class AltererTest : FreeSpec({
    "An [Alterer]" - {
        "should throw an exception when" - {
            "the probability is less than 0" {
                checkAll(Arb.negativeDouble()) { probability ->
                    shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                        DummyAlterer(probability)
                    }.shouldHaveInfringement<cl.ravenhill.jakt.exceptions.DoubleConstraintException>(
                        unfulfilledConstraint(
                            "The alteration probability [$probability] must be between " +
                                "0.0 and 1.0"
                        )
                    )
                }
            }

            "the probability is greater than 1" {
                checkAll(Arb.double(1.0..Double.MAX_VALUE)) { probability ->
                    assume { probability shouldBeGreaterThan 1.0 }
                    shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                        DummyAlterer(probability)
                    }.shouldHaveInfringement<cl.ravenhill.jakt.exceptions.DoubleConstraintException>(
                        unfulfilledConstraint(
                            "The alteration probability [$probability] must be between " +
                                "0.0 and 1.0"
                        )
                    )
                }
            }
        }
    }
})

private class DummyAlterer(probability: Double) :
    AbstractAlterer<Nothing, NothingGene>(probability) {
    override fun invoke(
        population: Population<Nothing, NothingGene>,
        generation: Int
    ) = AltererResult(population, 0)
}
