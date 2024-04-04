/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.limits

import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.MaxGenerations
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int

/**
 * Generates an arbitrary `GenerationLimit` for evolutionary algorithms.
 *
 * This function is a part of the Arb (Arbitrary) companion object, which provides a way to generate random
 * instances of `GenerationLimit`. It is particularly useful in property-based testing where you need to
 * create various instances of `GenerationLimit` with different generation counts.
 *
 * ## Parameters:
 * - **generations**: An arbitrary (Arb) of integers that provides the generation count for the limit. The
 *    default is simply `int()`, which generates any integer, but this can be customized as needed.
 *
 * ## Usage:
 * ```kotlin
 * // Create an arbitrary generator for GenerationLimit
 * val generationLimitArb = Arb.generationLimit<MyDataType, MyGene>()
 *
 * // Use it in property-based testing
 * checkAll(generationLimitArb) { generationLimit ->
 *     // Property test using the generated GenerationLimit
 * }
 * ```
 * In this example, `generationLimitArb` is an arbitrary generator for `GenerationLimit`. This can then be
 * used in property-based testing scenarios with tools like Kotest.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @return An `Arb<GenerationLimit<T, G>>` capable of generating `GenerationLimit` instances with random
 *         generation counts.
 */
fun <T, G> KeenArb.generationLimit(generations: Arb<Int> = Arb.int()) where G : Gene<T, G> = arbitrary {
    MaxGenerations<T, G>(generations.bind())
}
