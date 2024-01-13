/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.mutation.Mutator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.withEdgecases
import io.kotest.property.checkAll

/**
 * Tests the individual rate property of a given mutator.
 *
 * ## Overview
 * This function tests the behavior of the individual rate property in a mutator. It checks that the property has a
 * default value, accepts valid values, and throws exceptions for invalid values.
 *
 * ### Test Scenarios
 * - **Default Value Test**: Verifies that the individual rate defaults to a specified value.
 * - **Valid Value Test**: Checks if the mutator correctly handles valid individual rates between 0 and 1.
 * - **Invalid Value Test**: Ensures that the mutator throws an exception when set to an invalid rate.
 *
 * @param T The type of the individuals in the genetic algorithm.
 * @param G The type of the genes, which should inherit from `Gene<T, G>`.
 * @param defaultName The name of the default value for comparison in tests.
 * @param defaultValue The expected default value of the individual rate.
 * @param mutatorFactory1 A factory function to create a mutator given chromosome and gene rates.
 * @param mutatorFactory2 A factory function to create a mutator given individual, chromosome, and gene rates.
 */
fun <T, G> `test individual rate property`(
    defaultName: String,
    defaultValue: Double,
    mutatorFactory1: (Double, Double) -> Mutator<T, G>,
    mutatorFactory2: (Double, Double, Double) -> Mutator<T, G>,
) where G : Gene<T, G> = freeSpec {
    "Should have an individual rate property that" - {
        "defaults to [$defaultName]" {
            checkAll(Arb.probability(), Arb.probability()) { chromosomeRate, geneRate ->
                mutatorFactory1(chromosomeRate, geneRate).individualRate shouldBe defaultValue
            }
        }

        "can be set to a value between 0 and 1" {
            checkAll(
                /* individualRate = */ Arb.probability(),
                /* chromosomeRate = */ Arb.probability(),
                /* geneRate = */ Arb.probability()
            ) { individualRate, chromosomeRate, geneRate ->
                mutatorFactory2(individualRate, chromosomeRate, geneRate).individualRate shouldBe individualRate
            }
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            checkAll(
                Arb.double().filter { it !in 0.0..1.0 }.withEdgecases(),
                Arb.probability(),
                Arb.probability(),
            ) { individualRate, chromosomeRate, geneRate ->
                shouldThrow<CompositeException> {
                    mutatorFactory2(individualRate, chromosomeRate, geneRate)
                }.shouldHaveInfringement<MutatorConfigException>(
                    "The individual rate ($individualRate) must be in 0.0..1.0"
                )
            }
        }
    }
}
