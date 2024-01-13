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
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
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
    exceptionMessage: (Double) -> String
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
                /* individualRate = */ Arb.double().filter { it !in 0.0..1.0 }.withEdgecases(),
                /* chromosomeRate = */ Arb.probability(),
                /* geneRate = */ Arb.probability(),
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

/**
 * Tests the chromosome rate property of a given mutator.
 *
 * ## Overview
 * This function tests the behavior of the chromosome rate property in a mutator. It verifies that the property
 * has a default value, correctly accepts valid values, and throws exceptions for invalid values.
 *
 * @param T The type of the individuals in the genetic algorithm.
 * @param G The type of the genes, which should inherit from `Gene<T, G>`.
 * @param defaultName The name of the default value for comparison in tests.
 * @param defaultValue The expected default value of the chromosome rate.
 * @param defaultValueFactory A factory function to create a mutator given individual and gene rates.
 * @param completeFactory A factory function to create a mutator given individual, chromosome, and gene rates.
 */
fun <T, G> `test chromosome rate property`(
    defaultName: String,
    defaultValue: Double,
    defaultValueFactory: (Double, Double) -> Mutator<T, G>,
    completeFactory: (Double, Double, Double) -> Mutator<T, G>,
    exceptionMessage: (Double) -> String
) where G : Gene<T, G> = freeSpec {
    "Should have a chromosome rate property that" - {
        `defaults to default rate`(defaultName, defaultValue, defaultValueFactory)

        `can be set to a value between 0 and 1`(completeFactory)

        `throws an exception if set to a value outside of 0 to 1`(completeFactory, exceptionMessage)
    }
}

/**
 * Extension function for `FreeSpecContainerScope` to test the default value of a rate property in a mutator.
 *
 * ## Overview
 * This function is designed to validate that a specific rate property (e.g., chromosome rate) of a mutator
 * defaults to a given value. It is utilized within property-based testing frameworks to ensure the correct
 * default behavior of mutator properties.
 *
 * @param defaultName The name of the default rate property being tested. This is used primarily for display purposes
 *   in test output.
 * @param defaultValue The expected default value of the rate property.
 * @param defaultValueFactory A lambda function to create instances of Mutator with varied configurations.
 */
private suspend fun FreeSpecContainerScope.`defaults to default rate`(
    defaultName: String, defaultValue: Double, defaultValueFactory: (Double, Double) -> Mutator<*, *>,
) {
    "defaults to [$defaultName]" {
        checkAll(Arb.probability(), Arb.probability()) { rate1, rate2 ->
            defaultValueFactory(rate1, rate2).chromosomeRate shouldBe defaultValue
        }
    }
}

/**
 * Extension function for `FreeSpecContainerScope` to test that a mutator's rate property can be set to a valid value
 * within the range of 0 to 1.
 *
 * ## Overview
 * This function verifies that a specific rate property (e.g., chromosome rate) of a mutator can be correctly set to
 * any value within the 0 to 1 range. It is an essential part of property-based testing to ensure the flexible
 * configuration of mutator properties.
 *
 * @param completeFactory A factory function that creates a mutator instance. It takes three Double parameters
 *   representing individual rate, chromosome rate, and gene rate, and returns a mutator of type `Mutator<T, G>`.
 * @param T The type of the individuals in the genetic algorithm.
 * @param G The type of the genes, which should inherit from `Gene<T, G>`.
 */
private suspend fun <T, G> FreeSpecContainerScope.`can be set to a value between 0 and 1`(
    completeFactory: (Double, Double, Double) -> Mutator<T, G>,
) where G : Gene<T, G> {
    "can be set to a value between 0 and 1" {
        checkAll(
            Arb.probability(),
            Arb.probability(),
            Arb.probability()
        ) { rate1, rate2, rate3 ->
            completeFactory(rate2, rate1, rate3).chromosomeRate shouldBe rate2
        }
    }
}

/**
 * Extension function for `FreeSpecContainerScope` to test that a mutator's rate property throws an exception when set
 * to a value outside the range of 0 to 1.
 *
 * ## Overview
 * This function is designed to validate that a specific rate property (e.g., chromosome rate) of a mutator throws an
 * exception if set to any value outside the valid range (0 to 1). It is an important part of property-based testing
 * for verifying the robustness and error handling in mutator configurations.
 *
 * @param completeFactory A factory function that creates a mutator instance. It takes three Double parameters
 *   representing individual rate, chromosome rate, and gene rate, and returns a mutator of type `Mutator<T, G>`.
 * @param exceptionMessage A lambda function that takes a Double parameter and returns a String. This is used to
 *   generate the expected exception message for the test.
 * @param T The type of the individuals in the genetic algorithm.
 * @param G The type of the genes, which should inherit from `Gene<T, G>
. */
private suspend fun <T, G> FreeSpecContainerScope.`throws an exception if set to a value outside of 0 to 1`(
    completeFactory: (Double, Double, Double) -> Mutator<T, G>,
    exceptionMessage: (Double) -> String,
) where G : Gene<T, G> {
    "should throw an exception if set to a value that's not between 0 and 1" {
        checkAll(
            Arb.double().filter { it !in 0.0..1.0 }.withEdgecases(),
            Arb.probability(),
            Arb.probability(),
        ) { invalidRate, validRate1, validRate2 ->
            shouldThrow<CompositeException> {
                completeFactory(invalidRate, validRate1, validRate2)
            }.shouldHaveInfringement<MutatorConfigException>(exceptionMessage(invalidRate))
        }
    }
}
