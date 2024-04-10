/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant

/**
 * Generates an arbitrary `IndividualRanker` instance for property-based testing in evolutionary algorithms.
 *
 * @return An `Arb` that generates `IndividualRanker` instances, which rank individuals based on fitness values.
 */
fun arbIndividualRanker(): Arb<IndividualRanker<Double, DoubleGene>> = arbitrary {
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
fun <T, G> arbRanker(): Arb<IndividualRanker<T, G>> where G : Gene<T, G> = arbitrary {
    object : IndividualRanker<T, G> {
        override fun invoke(first: Individual<T, G>, second: Individual<T, G>) = first.fitness.compareTo(second.fitness)
    }
}

/**
 * Creates an arbitrary generator for various types of [IndividualRanker]<[T], [G]> instances.
 *
 * This function, part of the [Arb.Companion] object, is designed to generate different arbitrary instances
 * of `IndividualRanker<T, G>`. It offers a choice among a custom ranker created via the `ranker()` function,
 * and predefined rankers like `FitnessMaxRanker()` and `FitnessMinRanker()`. This variety allows for testing
 * different ranking strategies in genetic algorithms and evolutionary computations.
 *
 * ## Functionality:
 * - The `ranker()` option generates a custom ranker based on the fitness comparison of individuals.
 * - The `constant(FitnessMaxRanker())` and `constant(FitnessMinRanker())` options provide predefined rankers
 *   focusing on the maximum and minimum fitness values, respectively.
 * - The `choice` function randomly selects one of these options, offering diverse possibilities for ranking
 *   individuals.
 *
 * ## Usage:
 * Use this arbitrary when you need to test or simulate genetic algorithms with varying individual ranking strategies.
 * It allows for the creation of rankers with different behaviors, adding robustness to your testing suite.
 *
 * ### Example:
 * ```kotlin
 * val rankerGen = Arb.anyRanker<Double, SomeGeneClass>()
 * val ranker = rankerGen.bind() // Generates a random IndividualRanker instance
 * // ranker could be a custom ranker, a FitnessMaxRanker, or a FitnessMinRanker
 * ```
 * In this example, `rankerGen` is an arbitrary that generates a random `IndividualRanker` instance. Depending on
 * the random choice, this could be a custom ranker or one of the predefined max or min fitness rankers.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @return An [Arb]<[IndividualRanker]<[T], [G]>> that generates various types of `IndividualRanker` instances.
 */
fun <T, G> KeenArb.anyRanker(): Arb<IndividualRanker<T, G>> where G : Gene<T, G> = with(Arb) {
    choice(arbRanker(), constant(FitnessMaxRanker()), constant(FitnessMinRanker()))
}
