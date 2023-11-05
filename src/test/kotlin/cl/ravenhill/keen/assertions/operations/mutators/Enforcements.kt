/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.operations.mutators

import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeDouble
import io.kotest.property.arbitrary.positiveDouble
import io.kotest.property.assume
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
        shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
            mutatorBuilder(probability, rate)
        }.shouldHaveInfringement<cl.ravenhill.jakt.exceptions.DoubleRequirementException>(
            unfulfilledConstraint(
                "The $parameterName [$rate] must be in 0.0..1.0"
            )
        )
    }
}

/**
 * Ensures that a mutator throws an exception when given a negative gene mutation rate.
 *
 * This function tests the specified [mutatorBuilder] by providing it with various negative
 * gene mutation rates. The expected behavior is for the mutator to throw an exception
 * in such cases, ensuring that negative mutation rates are invalid.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the mutator operates on, which holds [T] type data.
 * @param mutatorBuilder A function that constructs a [Mutator] instance given a probability
 *        and a gene mutation rate.
 *
 * @throws AssertionError if the mutator accepts a negative gene mutation rate.
 * @see Gene
 */
suspend fun <T, G> `throw exception on negative gene rate`(
    mutatorBuilder: (probability: Double, geneRate: Double) -> Mutator<T, G>
) where G : Gene<T, G> {
    `should enforce valid mutation probability`(
        Arb.negativeDouble(),
        "gene rate"
    ) { probability, rate ->
        mutatorBuilder(probability, rate)
    }
}

/**
 * Validates that a mutator throws an exception when provided a gene mutation rate greater than one.
 *
 * The function checks the behavior of the specified [mutatorBuilder] by passing it values of
 * gene mutation rates that are greater than 1.0. It's expected that the mutator would
 * consider these values invalid and throw an exception as a result.
 *
 * @param T Represents the genetic data or information.
 * @param G Represents the gene type, containing [T] type data.
 * @param mutatorBuilder A function that produces a [Mutator] instance given a probability
 *        and a gene mutation rate.
 *
 * @throws AssertionError if the mutator does not throw an exception for gene mutation rates
 *         greater than one.
 * @see Gene
 */
suspend fun <T, G> `throw exception on gene rate greater than one`(
    mutatorBuilder: (probability: Double, geneRate: Double) -> Mutator<T, G>
) where G : Gene<T, G> {
    `should enforce valid mutation probability`(
        Arb.positiveDouble(),
        "gene rate",
        { assume { it shouldBeGreaterThan 1.0 } }
    ) { probability, rate ->
        mutatorBuilder(probability, rate)
    }
}

suspend fun <T, G> `throw exception on negative chromosome rate`(
    mutatorBuilder: (probability: Double, chromosomeRate: Double) -> Mutator<T, G>
) where G : Gene<T, G> {
    `should enforce valid mutation probability`(
        Arb.negativeDouble(),
        "chromosome mutation probability"
    ) { probability, rate ->
        mutatorBuilder(probability, rate)
    }
}

suspend fun <T, G> `throw exception if chromosome rate exceeds 1`(
    mutatorBuilder: (probability: Double, chromosomeRate: Double) -> Mutator<T, G>
) where G : Gene<T, G> {
    `should enforce valid mutation probability`(
        Arb.positiveDouble(),
        "chromosome mutation probability",
        { assume { it shouldBeGreaterThan 1.0 } }
    ) { probability, rate ->
        mutatorBuilder(probability, rate)
    }
}
