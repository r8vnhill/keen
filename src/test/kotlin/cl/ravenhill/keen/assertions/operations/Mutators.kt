/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.operations

import cl.ravenhill.enforcer.DoubleRequirementException
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.mutator.GeneMutator
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.real
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

/**
 * Validates that the mutation probability for a chromosome is within the permissible
 * range [0.0, 1.0].
 * Any mutation probability outside this range should throw an `EnforcementException`.
 *
 * @param probabilityArb Arbitrary probability values within [0.0, 1.0] for mutation.
 * @param mutatorBuilder A function that returns a `Mutator` instance given the mutation
 *                       probability and chromosome mutation rate.
 */
suspend fun <T, G> `should enforce valid mutation probability`(
    probabilityArb: Arb<Double>,
    parameterName: String,
    assumption: (rate: Double) -> Unit = {},
    mutatorBuilder: (probability: Double, rate: Double) -> Mutator<T, G>
) where G : Gene<T, G> {
    checkAll(Arb.real(0.0..1.0), probabilityArb) { probability, rate ->
        assumption(rate)
        shouldThrow<EnforcementException> {
            mutatorBuilder(probability, rate)
        }.shouldHaveInfringement<DoubleRequirementException>(
            unfulfilledConstraint(
                "The $parameterName [$rate] must be in 0.0..1.0"
            )
        )
    }
}

/**
 * Tests the default behavior of the chromosome mutation rate for a mutator.
 *
 * This function checks that when a mutator is built using the provided [mutatorBuilder],
 * its chromosome mutation rate defaults to 0.5 (or one half) irrespective of the given
 * probability and gene mutation rate parameters.
 *
 * The function iterates over a range of probabilities (between 0.0 and 1.0) and gene
 * mutation rates (between 0.0 and 0.1) to ensure the default behavior holds true across
 * these different scenarios.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the mutator operates on, which holds [T] type data.
 * @param mutatorBuilder A builder function that returns a [GeneMutator] instance.
 *        This function takes in two parameters: probability and geneRate.
 *
 * @throws AssertionError if any of the assertions within the function fail.
 *
 * @see GeneMutator
 * @see Gene
 */
suspend fun <T, G> `mutator chromosome rate defaults to one half`(
    mutatorBuilder: (probability: Double, geneRate: Double) -> GeneMutator<T, G>
) where G : Gene<T, G> {
    validateMutatorDefaults(0.0..1.0, 0.0..0.1, mutatorBuilder, chromosomeExpected = 0.5)
}

/**
 * Verifies that the default gene mutation rate for a mutator is set to 0.5.
 *
 * When constructing a mutator using the [mutatorBuilder], this test ensures that the gene
 * mutation rate defaults to 0.5, regardless of the input probability and chromosome mutation rate.
 *
 * The validation is performed over:
 * - Probabilities ranging from 0.0 to 1.0
 * - Chromosome mutation rates from 0.0 to 1.0
 *
 * @param T Represents the genetic data or information.
 * @param G Represents the gene type, containing [T] type data.
 *
 * @param mutatorBuilder A function that produces a [GeneMutator] instance given a probability and chromosomeRate.
 *
 * @throws AssertionError if any assertions fail during the test.
 *
 * @see GeneMutator
 * @see Gene
 */
suspend fun <T, G> `mutator gene rate defaults to one half`(
    mutatorBuilder: (probability: Double, chromosomeRate: Double) -> GeneMutator<T, G>
) where G : Gene<T, G> {
    validateMutatorDefaults(0.0..1.0, 0.0..1.0, mutatorBuilder, geneExpected = 0.5)
}

/**
 * Validates the default values of a gene mutator's properties.
 *
 * This function is a generalized utility to check whether the constructed mutator, using the
 * provided [mutatorBuilder], exhibits the expected default behaviors for its chromosome
 * mutation rate and gene mutation rate properties.
 *
 * It iteratively creates mutators using combinations of probability and rate values
 * specified by [probRange] and [rateRange] respectively and then verifies the mutator's
 * properties against the expected values.
 *
 * @param probRange A range specifying the probabilities to be used for testing.
 * @param rateRange A range specifying the mutation rates (either chromosome or gene
 *        rate) to be used for testing.
 * @param mutatorBuilder A function that constructs a [GeneMutator] based on given
 *        probability and rate.
 * @param chromosomeExpected An optional expected value for the chromosome mutation rate.
 *        If specified, the function checks the mutator's chromosome rate against this
 *        value.
 * @param geneExpected An optional expected value for the gene mutation rate. If
 *        specified, the function checks the mutator's gene rate against this value.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the mutator operates on, which holds [T] type data.
 *
 * @throws AssertionError if any of the assertions within the function fail.
 *
 * @see GeneMutator
 * @see Gene
 */
private suspend fun <T, G> validateMutatorDefaults(
    probRange: ClosedFloatingPointRange<Double>,
    rateRange: ClosedFloatingPointRange<Double>,
    mutatorBuilder: (Double, Double) -> GeneMutator<T, G>,
    chromosomeExpected: Double? = null,
    geneExpected: Double? = null
) where G : Gene<T, G> {
    checkAll(Arb.real(probRange), Arb.real(rateRange)) { probability, rate ->
        val mutator = mutatorBuilder(probability, rate)
        mutator.probability shouldBe probability

        chromosomeExpected?.let {
            mutator.chromosomeRate shouldBe it
        }

        geneExpected?.let {
            mutator.geneRate shouldBe it
        }
    }
}
