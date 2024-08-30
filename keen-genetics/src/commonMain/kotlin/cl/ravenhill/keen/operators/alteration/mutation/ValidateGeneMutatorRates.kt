/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.mixins.Validator

/**
 * Validates the mutation rates for genes, chromosomes, and individuals in a genetic algorithm.
 *
 * The `ValidateGeneMutatorRates` class serves as a `Validator` for ensuring that the mutation rates provided to a
 * gene mutator are within the valid range of [0, 1]. This class is intended to be used through delegation in mutation
 * operators or other components that require validation of mutation rates.
 *
 * ## Usage:
 * This class is typically used in the context of genetic algorithms where mutation rates must be validated to avoid
 * invalid operations or unexpected behavior. By implementing the `Validator` interface, this class can be easily
 * integrated into other components through delegation.
 *
 * ### Example: Using `ValidateGeneMutatorRates` for Validation
 * ```kotlin
 * class MyGeneMutator<T, G>(
 *     individualRate: Double,
 *     chromosomeRate: Double,
 *     geneRate: Double
 * ) : Mutator<T, G>, Validator by ValidateGeneMutatorRates(individualRate, chromosomeRate, geneRate)
 *     where G : Gene<T, G> {
 *     // Mutator implementation here
 * }
 * ```
 *
 * In this example, the `ValidateGeneMutatorRates` class is used to validate the mutation rates for individuals,
 * chromosomes, and genes when constructing a `MyGeneMutator` object. If any of the rates are outside the valid range,
 * an exception is thrown during the initialization of the `MyGeneMutator`.
 *
 * @param individualRate The mutation rate for individuals, which must be in the range [0, 1].
 * @param chromosomeRate The mutation rate for chromosomes, which must be in the range [0, 1].
 * @param geneRate The mutation rate for genes, which must be in the range [0, 1].
 * @constructor Performs validation of the mutation rates for individuals, chromosomes, and genes.
 * @throws CompositeException if any of the mutation rates fall outside the valid range.
 */
class ValidateGeneMutatorRates(individualRate: Double, chromosomeRate: Double, geneRate: Double) : Validator {
    init {
        constrained {
            "Individual rate must be in the range [0, 1] but was $individualRate" {
                individualRate must BeInRange(0.0..1.0)
            }
            "Chromosome rate must be in the range [0, 1] but was $chromosomeRate" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "Gene rate must be in the range [0, 1] but was $geneRate" {
                geneRate must BeInRange(0.0..1.0)
            }
        }.onLeft { throw it }
    }
}
