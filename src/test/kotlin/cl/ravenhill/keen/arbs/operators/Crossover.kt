/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.operators

import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.genetic.genes.numerical.NumberGene
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.crossover.combination.AverageCrossover
import cl.ravenhill.keen.operators.crossover.combination.CombineCrossover
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.choice

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
 * Provides an arbitrary generator for different types of crossover operators specifically designed for
 * integer genes. This generator creates instances of [SinglePointCrossover], [CombineCrossover], or
 * [AverageCrossover] for [IntGene] types.
 *
 * The crossover operators generated are tailored to work with [IntGene], allowing for testing
 * various crossover strategies in genetic algorithms. The function uses Kotest's property-based
 * testing features to randomly select among different crossover operators, each with its unique
 * crossover behavior.
 *
 * - [SinglePointCrossover]: Performs crossover at a single point in the chromosome.
 * - [CombineCrossover]: Combines genes from parent chromosomes using a random selection.
 * - [AverageCrossover]: Averages the values of corresponding genes from parent chromosomes.
 *
 * The rates for chromosome crossover and gene crossover in [CombineCrossover] are generated
 * using probability distributions.
 *
 * @return An [Arb] that generates instances of crossover operators for [IntGene].
 */
fun Arb.Companion.intCrossover() = choice(
    singlePointCrossover<Int, IntGene>(),
    combineCrossover(
        { genes -> genes.random() },
        chromosomeRate = probability(),
        geneRate = probability()
    ),
    averageCrossover()
)
