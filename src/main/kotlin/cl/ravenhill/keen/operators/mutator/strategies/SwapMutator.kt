/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator.strategies

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.mutator.ChromosomeMutator
import cl.ravenhill.keen.operators.mutator.MutatorResult
import cl.ravenhill.keen.util.indices
import cl.ravenhill.keen.util.neq
import cl.ravenhill.keen.util.swap

/**
 * Implementation of the _Partial Shuffle Mutation_ (PSM) operator.
 *
 * The PSM operator is a mutation operator that randomly selects a number of genes from a chromosome
 * and shuffles them.
 * The number of genes to be shuffled is determined by the mutation [probability].
 *
 * __References:__
 * - [Analyzing the Performance of Mutation Operators to Solve the Travelling Salesman Problem](https://arxiv.org/ftp/arxiv/papers/1203/1203.3099.pdf)
 *
 * @param DNA The type of the DNA
 * @param G The type of the genes
 *
 * @param probability The probability of mutating a genotype
 * @param chromosomeRate The probability of mutating a chromosome
 * @param geneRate The probability of mutating a gene
 *
 * @constructor Creates a new [SwapMutator] with the given [probability]
 */
class SwapMutator<DNA, G : Gene<DNA, G>>(
    override val probability: Double = 0.2,
    override val chromosomeRate: Double = 0.5,
    val geneRate: Double = 0.5
) : ChromosomeMutator<DNA, G> {
    override fun mutateChromosome(
        chromosome: Chromosome<DNA, G>,
    ) = if (chromosomeRate neq 0.0 && chromosome.size > 1) {
        val genes = chromosome.genes.toMutableList()
        val indices = Core.random.indices(geneRate, genes.size)
        val mutations = indices
            .map {
                Core.random.nextInt(genes.size).apply { genes.swap(it, this) }
            }.count()
        MutatorResult(chromosome.withGenes(genes), mutations)
    } else {
        MutatorResult(chromosome, 0)
    }
}
