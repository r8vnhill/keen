/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.operators.crossover.combination

import cl.ravenhill.keen.genetic.genes.numerical.NumberGene


/**
 * A [CombineCrossover] that performs mean crossover on [NumberGene]s in the chromosome.
 * Given a list of chromosomes, this crossover will take the average of the genes in each chromosome
 * with a given rate.
 *
 * @param DNA The type of the number values stored in the genes.
 * @param probability The probability of performing crossover on each individual of the population.
 * @param chromosomeRate The rate of chromosomes that will undergo crossover.
 * @param geneRate The rate of genes that will undergo crossover within each chromosome.
 *
 * @constructor Creates a [AverageCrossover] instance with the given crossover rates and probability.
 *
 * @see NumberGene
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class AverageCrossover<DNA : Number, G : NumberGene<DNA, G>>(
    probability: Double,
    chromosomeRate: Double = 1.0,
    geneRate: Double = 1.0
) : CombineCrossover<DNA, G>(
    { genes: List<G> ->
        genes[0].average(genes.drop(1))
    },
    probability,
    chromosomeRate,
    geneRate
) {
    override fun toString() =
        "MeanCrossover(probability=$probability, chromosomeRate=$chromosomeRate, geneRate=$geneRate)"
}