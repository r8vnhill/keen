/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.operations

import cl.ravenhill.enforcer.DoubleRequirementException
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.real
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
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
