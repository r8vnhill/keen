/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.genetic.genes.numerical.NumberGene
import cl.ravenhill.keen.operators.crossover.combination.AverageCrossover
import cl.ravenhill.keen.operators.crossover.combination.CombineCrossover
import cl.ravenhill.keen.operators.crossover.permutation.AbstractPermutationCrossover
import cl.ravenhill.keen.operators.crossover.permutation.OrderedCrossover
import cl.ravenhill.keen.operators.crossover.permutation.PermutationCrossover
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.int

/**
 * Provides an arbitrary generator for [AverageCrossover] instances.
 * The [AverageCrossover] operator is used in genetic algorithms for numeric types. It creates
 * offspring by averaging the values of corresponding genes from multiple parents.
 *
 * This function is particularly useful for property-based testing where different configurations
 * of the [AverageCrossover] operator need to be tested.
 *
 * @param T A [Number] type representing the type of the gene's value.
 * @param G The specific type of [NumberGene] which contains [T] type values.
 * @param chromosomeRate An arbitrary generator for the chromosome crossover rate, representing
 *                       the probability with which a chromosome will be selected for crossover.
 * @param geneRate An arbitrary generator for the gene crossover rate, representing
 *                 the probability with which an individual gene within a chromosome will undergo crossover.
 *
 * @return An [Arb] of [AverageCrossover].
 */
fun <T, G> Arb.Companion.averageCrossover(
    chromosomeRate: Arb<Double> = probability(),
    geneRate: Arb<Double> = probability()
) where T : Number, G : NumberGene<T, G> = arbitrary {
    AverageCrossover<T, G>(chromosomeRate.bind(), geneRate.bind())
}

/**
 * Provides an arbitrary generator for [CombineCrossover] instances.
 * The [CombineCrossover] operator is a genetic algorithm operator that creates offspring by combining
 * genes from multiple parents using a specified combiner function.
 *
 * This function allows for flexible testing of [CombineCrossover] with various combiner functions
 * and crossover rates, making it useful for property-based testing.
 *
 * @param T A [Number] type representing the type of the gene's value.
 * @param G The specific type of [NumberGene] which contains [T] type values.
 * @param combiner A function that combines a list of genes [G] into a single gene. This function
 *                 defines the crossover behavior of the operator.
 * @param chromosomeRate An arbitrary generator for the chromosome crossover rate, representing
 *                       the probability with which a chromosome will be selected for crossover.
 * @param geneRate An arbitrary generator for the gene crossover rate, representing
 *                 the probability with which an individual gene within a chromosome will undergo crossover.
 *
 * @return An [Arb] of [CombineCrossover].
 */
fun <T, G> Arb.Companion.combineCrossover(
    combiner: (List<G>) -> G,
    chromosomeRate: Arb<Double> = probability(),
    geneRate: Arb<Double> = probability()
) where T : Number, G : NumberGene<T, G> = arbitrary {
    CombineCrossover<T, G>(combiner, chromosomeRate.bind(), geneRate.bind())
}


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
fun <T, G> Arb.Companion.singlePointCrossover(
    chromosomeRate: Arb<Double> = probability(),
    exclusivity: Arb<Boolean> = boolean()
) where G : Gene<T, G> = arbitrary {
    SinglePointCrossover<Int, IntGene>(chromosomeRate.bind(), exclusivity.bind())
}

/**
 * Generates an arbitrary [PermutationCrossover] for property-based testing.
 *
 * This generator creates instances of a dummy [PermutationCrossover] implementation, suitable for
 * testing various configurations and behaviors in a controlled environment. The created
 * [PermutationCrossover] instances are configured with random values for the number of parents and
 * offspring, exclusivity, and chromosome crossover rate.
 *
 * The dummy implementation of [PermutationCrossover.performPermutationCrossover] in this context simply replicates the
 * genes of the first chromosome across all offspring, without performing any actual permutation logic.
 * This allows for straightforward testing of crossover framework mechanics without the complexity
 * of a real-world permutation crossover logic.
 *
 * @param numParents An [Arb]<[Int]> for generating the number of parent chromosomes involved in the crossover.
 * @param numOffspring An [Arb]<[Int]> for generating the number of offspring chromosomes produced by the crossover.
 * @param exclusivity An [Arb]<[Boolean]> for determining whether the crossover operation is exclusive.
 * @param chromosomeRate An [Arb]<[Double]> for generating the rate at which chromosomes are selected for crossover.
 *
 * @return An [Arb] that produces instances of [PermutationCrossover] with randomly configured properties.
 *
 * @param T The type of the gene's value.
 * @param G The specific type of [Gene] for which the [PermutationCrossover] is applicable.
 */
fun <T, G> Arb.Companion.dummyPermutationCrossover(
    numParents: Arb<Int> = int(2..10),
    numOffspring: Arb<Int> = int(2..10),
    exclusivity: Arb<Boolean> = boolean(),
    chromosomeRate: Arb<Double> = probability()
): Arb<PermutationCrossover<T, G>> where G : Gene<T, G> = arbitrary {
    val boundParents = numParents.bind()
    val boundOffspring = numOffspring.bind()
    val boundExclusivity = exclusivity.bind()
    val boundChromosomeRate = chromosomeRate.bind()
    object : AbstractPermutationCrossover<T, G>(
        numParents = boundParents,
        numOffspring = boundOffspring,
        exclusivity = boundExclusivity,
        chromosomeRate = boundChromosomeRate
    ) {
        override fun performPermutationCrossover(chromosomes: List<Chromosome<T, G>>) =
            List(boundOffspring) { chromosomes[0].genes }
    }
}

/**
 * Provides an arbitrary generator for creating instances of [OrderedCrossover].
 *
 * The [orderedCrossover] function is designed for property-based testing,
 * facilitating the generation of [OrderedCrossover] instances with various configurations.
 * It leverages Kotest's [Arb] (Arbitrary) API to produce probabilities and rates for
 * the crossover operator's parameters.
 *
 * ## Example Usage:
 * ```
 * val orderedCrossoverArb = Arb.orderedCrossover<Int, IntGene>()
 * val crossoverInstance = orderedCrossoverArb.bind() // Instance of OrderedCrossover
 * ```
 *
 * @param probability An [Arb]<[Double]> that generates the probability of crossover occurring.
 *        It determines the likelihood that a pair of parent chromosomes will undergo
 *        crossover to produce offspring.
 * @param chromosomeRate An [Arb]<[Double]> that generates the rate at which chromosomes are selected
 *        for crossover. This rate affects how frequently chromosomes within a population
 *        are chosen to participate in the crossover process.
 *
 * @return An arbitrary generator ([Arb]) that yields instances of [OrderedCrossover] with random configurations
 *         based on the provided probabilities and rates.
 *
 * @param T The type representing the genetic data or information.
 * @param G The specific type of [Gene] for which the [OrderedCrossover] is applicable.
 */
fun <T, G> Arb.Companion.orderedCrossover(
    probability: Arb<Double> = probability(),
    chromosomeRate: Arb<Double> = probability()
) where G : Gene<T, G> = arbitrary {
    OrderedCrossover<T, G>(probability.bind(), chromosomeRate.bind())
}

/**
 * Generates an arbitrary instance of crossover operators tailored for [IntGene]. This generator
 * provides a range of crossover strategies, each suitable for different scenarios in genetic algorithms
 * involving integer genes. It leverages Kotest's property-based testing framework to randomly select
 * among various crossover types, facilitating thorough and diverse testing approaches.
 *
 * The function creates instances of several crossover operators, each with distinct mechanisms:
 * - [SinglePointCrossover]: Executes crossover at a singular point within the chromosome, effectively
 *   swapping gene sequences between two parent chromosomes from that point.
 * - [CombineCrossover]: Merges genes from multiple parent chromosomes, selecting genes randomly to
 *   construct offspring chromosomes.
 * - [AverageCrossover]: Calculates the average of corresponding genes from parent chromosomes to
 *   produce offspring genes, ideal for numeric optimization problems.
 * - [PermutationCrossover]: A mock implementation of permutation crossover, suitable for
 *   testing scenarios where permutations are involved.
 * - [OrderedCrossover]: Specialized for sequence-based problems, ensuring the order of genes is
 *   maintained while recombining genetic material.
 *
 * The generation of chromosome and gene crossover rates in [CombineCrossover] employs probability
 * distributions, adding variability and realism to the simulation of genetic operations.
 *
 * ## Example Usage:
 * ```
 * val crossoverArb = Arb.intCrossover()
 * val crossoverOperator = crossoverArb.bind() // Instance of a crossover operator for IntGene
 * ```
 *
 * @return An [Arb] (Arbitrary generator) that yields diverse instances of crossover operators for [IntGene],
 *         enabling extensive testing of crossover strategies in genetic algorithms with integer gene types.
 */
fun Arb.Companion.intCrossover() = choice(
    singlePointCrossover<Int, IntGene>(),
    combineCrossover(
        { genes -> genes.random() },
        chromosomeRate = probability(),
        geneRate = probability()
    ),
    averageCrossover(),
    dummyPermutationCrossover(),
    orderedCrossover()
)
