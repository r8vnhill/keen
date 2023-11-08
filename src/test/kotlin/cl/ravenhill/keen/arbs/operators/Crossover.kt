/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.operators

import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean

/**
 * Provides an arbitrary generator for creating instances of [SinglePointCrossover] with random parameters.
 *
 * Usage within a property-based test could look like this:
 *
 * ```kotlin
 * checkAll(Arb.singlePointCrossover()) { crossover ->
 *     // Perform tests with the generated crossover instance
 * }
 * ```
 *
 * @return An [Arb] (arbitrary) instance that generates [SinglePointCrossover] objects with random crossover
 * probabilities and offspring variety flags.
 */
fun Arb.Companion.singlePointCrossover(chromosomeRate: Arb<Double> = probability()) = arbitrary {
    SinglePointCrossover<Int, IntGene>(chromosomeRate.bind(), boolean().bind())
}
