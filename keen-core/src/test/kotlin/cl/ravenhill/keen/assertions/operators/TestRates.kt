/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions.operators

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.mutation.GeneMutator
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
 * @param defaultFactory A factory function to create a mutator given chromosome and gene rates.
 * @param completeFactory A factory function to create a mutator given individual, chromosome, and gene rates.
 * @param exceptionMessage A lambda function that takes a Double parameter and returns a String. This is used to
 *   generate the expected exception message for the test.
 */
fun <T, G> `test individual rate property`(
    defaultName: String,
    defaultValue: Double,
    defaultFactory: (Double, Double) -> Mutator<T, G>,
    completeFactory: (Double, Double, Double) -> Mutator<T, G>,
    exceptionMessage: (Double) -> String
) where G : Gene<T, G> = freeSpec {
    "individual rate" - {
        `test rate property`(defaultName, defaultValue, defaultFactory, completeFactory, exceptionMessage) {
            individualRate
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
    "chromosome rate" - {
        `test rate property`(defaultName, defaultValue, defaultValueFactory, completeFactory, exceptionMessage) {
            chromosomeRate
        }
    }
}

/**
 * Function to test the gene rate property of a `GeneMutator`.
 *
 * ## Overview
 * This function provides a structured approach to test the gene rate property of a `GeneMutator`. It validates that the gene rate has a default value, can be set within a valid range (0 to 1), and ensures proper exception handling for values outside this range.
 *
 * ## Parameters
 * - `defaultName`: The name of the default gene rate property being tested, used for display purposes in test output.
 * - `defaultValue`: The expected default value of the gene rate property.
 * - `defaultValueFactory`: A factory function to create a `GeneMutator` given two rate parameters. This is used to test the default value of the gene rate.
 * - `completeFactory`: A factory function to create a `GeneMutator` given three rate parameters. This is used for testing the gene rate within a valid range and for exception testing.
 * - `exceptionMessage`: A function that returns the expected exception message for an invalid gene rate.
 *
 * ## Usage
 * This function should be called within a `freeSpec` testing block. It orchestrates a series of assertions to verify the correct behavior of the gene rate property in a `GeneMutator`.
 *
 * @param defaultName Descriptive name of the default gene rate property for readability.
 * @param defaultValue Double value expected to be the default for the gene rate property.
 * @param defaultValueFactory Lambda function to create instances of `GeneMutator` for testing the default gene rate.
 * @param completeFactory Lambda function for creating `GeneMutator` instances for testing valid range and exception handling.
 * @param exceptionMessage Function providing the expected exception message for invalid gene rate values.
 * @param T The type of the individuals in the genetic algorithm.
 * @param G The type of the genes, inheriting from `Gene<T, G>`.
 */
fun <T, G> `test gene rate property`(
    defaultName: String,
    defaultValue: Double,
    defaultValueFactory: (Double, Double) -> GeneMutator<T, G>,
    completeFactory: (Double, Double, Double) -> GeneMutator<T, G>,
    exceptionMessage: (Double) -> String
) where G : Gene<T, G> = freeSpec {
    "Should have a gene rate property that" - {
        `test rate property`(defaultName, defaultValue, defaultValueFactory, completeFactory, exceptionMessage) {
            geneRate
        }
    }
}

/**
 * Extension function for `FreeSpecContainerScope` to comprehensively test a rate property of a mutator.
 *
 * ## Overview
 * This function consolidates multiple tests for a rate property (e.g., chromosome rate) in a mutator. It checks that
 * the rate property has a default value, can be set within a valid range (0 to 1), and throws an exception for values
 * outside this range.
 *
 * @param defaultName The name of the default rate property being tested. This is used primarily for display purposes
 *   in test output.
 * @param defaultValue The expected default value of the rate property.
 * @param defaultValueFactory A factory function to create a mutator given two rate parameters. Used to test the
 *   default value of the rate property.
 * @param completeFactory A factory function to create a mutator given three rate parameters. Used for testing setting
 *   the rate within a valid range and for exception testing.
 * @param exceptionMessage A function that returns the expected exception message given an invalid rate value.
 * @param T The type of the individuals in the genetic algorithm.
 * @param G The type of the genes, inheriting from `Gene<T, G>`.
 */
private suspend fun <G, T, M> FreeSpecContainerScope.`test rate property`(
    defaultName: String,
    defaultValue: Double,
    defaultValueFactory: (Double, Double) -> M,
    completeFactory: (Double, Double, Double) -> M,
    exceptionMessage: (Double) -> String,
    rateProperty: M.() -> Double
) where G : Gene<T, G>, M : Mutator<T, G> {
    `defaults to default rate`(defaultName, defaultValue, defaultValueFactory, rateProperty)

    `can be set to a value between 0 and 1`(completeFactory, rateProperty)

    `throws an exception if set to a value outside of 0 to 1`(completeFactory, exceptionMessage)
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
private suspend fun <T, G, M> FreeSpecContainerScope.`defaults to default rate`(
    defaultName: String, defaultValue: Double,
    defaultValueFactory: (Double, Double) -> M,
    rateProperty: M.() -> Double
) where G : Gene<T, G>, M : Mutator<T, G> {
    "defaults to [$defaultName]" {
        checkAll(Arb.probability(), Arb.probability()) { rate1, rate2 ->
            defaultValueFactory(rate1, rate2).rateProperty() shouldBe defaultValue
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
private suspend fun <T, G, M> FreeSpecContainerScope.`can be set to a value between 0 and 1`(
    completeFactory: (Double, Double, Double) -> M,
    rateProperty: M.() -> Double
) where G : Gene<T, G>, M : Mutator<T, G> {
    "can be set to a value between 0 and 1" {
        checkAll(
            Arb.probability(),
            Arb.probability(),
            Arb.probability()
        ) { rate1, rate2, rate3 ->
            completeFactory(rate2, rate1, rate3).rateProperty() shouldBe rate2
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
