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
    checkAll(
        Arb.real(0.0..1.0),
        Arb.real(0.0..0.1)
    ) { probability, geneRate ->
        val mutator = mutatorBuilder(probability, geneRate)
        mutator.probability shouldBe probability
        mutator.chromosomeRate shouldBe 0.5
        mutator.geneRate shouldBe geneRate
    }
}
