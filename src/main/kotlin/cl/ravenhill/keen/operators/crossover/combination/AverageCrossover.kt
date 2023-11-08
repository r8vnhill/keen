/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.combination

import cl.ravenhill.keen.genetic.genes.numerical.NumberGene

/**
 * A crossover operator for numeric genes that produces offspring by averaging the values of
 * corresponding genes from multiple parents. This type of crossover is suitable for continuous
 * optimization problems where blending parental genes can lead to a better exploration of the
 * search space.
 *
 * The crossover process involves averaging the values of each gene across all parents, creating a new gene
 * for the offspring. The operation is controlled by two rates: `chromosomeRate` determines the likelihood
 * a chromosome is chosen for crossover, while `geneRate` decides if a gene will be averaged or passed on
 * directly from the first parent.
 *
 * ## Usage
 * ### Example 1: Manually computing the average of 3 chromosomes
 * ```kotlin
 * val chromosome1 = IntChromosome(IntGene(1), IntGene(2), IntGene(3))
 * val chromosome2 = IntChromosome(IntGene(4), IntGene(5), IntGene(6))
 * val chromosome3 = IntChromosome(IntGene(7), IntGene(8), IntGene(9))
 * val crossover = AverageCrossover<Int, IntGene>()
 * val result = crossover.combine(listOf(chromosome1, chromosome2, chromosome3))
 * ```
 * The resulting chromosome will be `[4, 5, 6]`.
 *
 * ### Example 2: Using the crossover operator in a genetic algorithm
 * ```kotlin
 * val engine = engine {
 *   // ...
 *   alterers += AverageCrossover<Int, IntGene>(0.5, 0.7)
 * }
 * ```
 *
 * @param DNA A numeric type that extends from [Number], representing the type of the gene's value.
 * @param G The specific type of [NumberGene] which contains [DNA] type values.
 * @property chromosomeRate The probability with which a chromosome will be selected for crossover.
 * @property geneRate The probability with which an individual gene within a chromosome will undergo crossover.
 *
 * @constructor Creates an [AverageCrossover] instance with the specified chromosome and gene rates.
 * Inherits from [CombineCrossover], using a predefined averaging function as the combiner.
 **
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class AverageCrossover<DNA : Number, G : NumberGene<DNA, G>>(
    chromosomeRate: Double = 1.0,
    geneRate: Double = 1.0
) : CombineCrossover<DNA, G>(
    { genes: List<G> ->
        genes[0].average(genes.drop(1))
    },
    chromosomeRate,
    geneRate
)
