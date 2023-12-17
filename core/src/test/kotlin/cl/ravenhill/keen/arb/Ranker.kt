/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
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

/**
 * Creates an arbitrary generator for [IndividualRanker]<[T], [G]> instances.
 *
 * This function is a part of the [Arb.Companion] object and is designed to generate arbitrary instances of
 * `IndividualRanker<T, G>`. Each generated `IndividualRanker` is capable of comparing two individuals
 * (`Individual<T, G>`) based on their fitness values. This is especially useful in evolutionary algorithms
 * where individuals need to be ranked or sorted according to their fitness.
 *
 * ## Functionality:
 * - The generated `IndividualRanker` compares two individuals by their fitness values using the `compareTo` method.
 * - It returns a positive number, zero, or a negative number if the first individual's fitness is greater than,
 *   equal to, or less than the second individual's fitness, respectively.
 *
 * ## Usage:
 * Employ this arbitrary in scenarios involving genetic algorithms or evolutionary computations where a mechanism
 * to rank individuals based on fitness is required. It provides a straightforward way to generate rankers for
 * testing or simulation purposes.
 *
 * ### Example:
 * ```kotlin
 * val rankerGen = Arb.ranker<Double, SomeGeneClass>()
 * val ranker = rankerGen.bind() // Generates an IndividualRanker instance
 * val individual1 = Individual(...) // Individual with some fitness
 * val individual2 = Individual(...) // Another individual with different fitness
 * val comparisonResult = ranker(individual1, individual2)
 * // comparisonResult indicates which individual has higher fitness
 * ```
 * In this example, `ranker` is an `IndividualRanker` instance used to compare two individuals based on their fitness.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @return An [Arb]<[IndividualRanker]<[T], [G]>> that generates `IndividualRanker` instances.
 */
fun <T, G> Arb.Companion.ranker(): Arb<IndividualRanker<T, G>> where G : Gene<T, G> = arbitrary {
    object : IndividualRanker<T, G> {
        override fun invoke(first: Individual<T, G>, second: Individual<T, G>) = first.fitness.compareTo(second.fitness)
    }
}
