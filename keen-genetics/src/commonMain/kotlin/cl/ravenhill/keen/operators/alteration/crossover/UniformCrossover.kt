package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.utils.Exclusivity
import kotlin.random.Random

/**
 * A crossover operator that implements uniform crossover in an evolutionary algorithm.
 *
 * The `UniformCrossover` class represents a genetic operator that performs uniform crossover on a population of
 * individuals in an evolutionary algorithm. In uniform crossover, each gene in the offspring is independently chosen
 * from the corresponding genes of one of the parent individuals. The probability of choosing a gene from a particular
 * parent is uniform across all parents, ensuring that each gene has an equal chance of being inherited from any parent.
 *
 * ## Theoretical Background:
 * Uniform crossover is a type of genetic recombination used in evolutionary algorithms to produce offspring from two or
 * more parents. Unlike traditional crossover methods such as one-point or two-point crossover, where large blocks of
 * genes are exchanged between parents, uniform crossover considers each gene position independently. This allows for
 * greater genetic diversity in the offspring, as genes can be mixed at a finer level of granularity.
 *
 * ## Usage:
 * The `UniformCrossover` operator can be used in evolutionary algorithms where maintaining high genetic diversity in
 * the offspring is important. It is particularly useful in problems where fine-grained control over gene mixing is
 * required.
 *
 * ### Example: Using `UniformCrossover`
 * ```kotlin
 * val crossover = UniformCrossover<Int, MyGene>(
 *     numParents = 2,
 *     chromosomeRate = 0.8,
 *     geneRate = 0.5,
 *     exclusivity = Exclusivity.EXCLUSIVE,
 *     random = Random(420)
 * )
 * val offspring = crossover(parents)
 * ```
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene, which must extend [Gene].
 * @param numParents The number of parents involved in the crossover.
 * @param chromosomeRate The probability that a chromosome will be subject to crossover.
 * @param geneRate The probability that a gene within a chromosome will be subject to crossover.
 * @param exclusivity The exclusivity policy for gene selection across multiple crossover operations.
 * @param random The random number generator used for crossover operations. Default is [Domain.random].
 */
class UniformCrossover<T, G>(
    numParents: Int = DEFAULT_NUM_PARENTS,
    chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    geneRate: Double = DEFAULT_GENE_RATE,
    exclusivity: Exclusivity = DEFAULT_EXCLUSIVITY,
    random: Random = Domain.random
) : CombineCrossover<T, G>(
    combiner = { genes -> genes.random(random) },
    chromosomeRate = chromosomeRate,
    geneRate = geneRate,
    numParents = numParents,
    exclusivity = exclusivity
) where G : Gene<T, G> {

    companion object {

        /**
         * The default number of parents involved in the crossover. By default, two parents are used.
         */
        const val DEFAULT_NUM_PARENTS = 2

        /**
         * The default probability that a chromosome will undergo crossover. By default, the rate is 0.5.
         */
        const val DEFAULT_CHROMOSOME_RATE = 0.5

        /**
         * The default probability that a gene within a chromosome will undergo crossover. By default, the rate is 0.5.
         */
        const val DEFAULT_GENE_RATE = 0.5

        /**
         * The default exclusivity policy for gene selection across multiple crossover operations. By default, parents
         * are selected non-exclusively.
         */
        val DEFAULT_EXCLUSIVITY = Exclusivity.NON_EXCLUSIVE
    }
}
