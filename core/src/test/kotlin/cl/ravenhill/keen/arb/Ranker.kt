/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.ranking.IndividualRanker
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

/**
 * Generates an arbitrary [IndividualRanker] instance for property-based testing in evolutionary algorithms.
 *
 * This function creates instances of [IndividualRanker] for use in scenarios involving
 * evolutionary algorithms with individuals represented by [Double] values and [DoubleGene]. The generated ranker
 * compares individuals based on their fitness values, which is a common criterion in evolutionary algorithms.
 *
 * ## Usage:
 * This arbitrary generator can be used in property-based testing frameworks like Kotest to create [IndividualRanker]
 * instances. This is particularly useful for testing components of evolutionary algorithms that rely on the ranking
 * of individuals, such as selection mechanisms or fitness evaluations.
 *
 * ### Example:
 * ```kotlin
 * val rankerArb = Arb.individualRanker<Double, DoubleGene>()
 * val ranker = rankerArb.bind() // Instance of IndividualRanker
 * // Use the ranker for comparing individuals based on fitness in evolutionary algorithm testing
 * ```
 * In this example, `rankerArb` provides an [IndividualRanker] instance that ranks individuals based on their fitness.
 * The bound `ranker` can then be used in tests to simulate fitness-based ranking processes in an evolutionary
 * algorithm.
 *
 * @return An [Arb] that generates [IndividualRanker] instances, which rank individuals based on fitness values.
 */
fun Arb.Companion.individualRanker(): Arb<IndividualRanker<Double, DoubleGene>> = arbitrary {
    object : IndividualRanker<Double, DoubleGene> {
        override fun invoke(first: Individual<Double, DoubleGene>, second: Individual<Double, DoubleGene>) =
            first.fitness.compareTo(second.fitness)
    }
}
