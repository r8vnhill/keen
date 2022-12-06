package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.util.indexes
import cl.ravenhill.keen.util.math.swap


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
 * @param probability The probability of mutating a genotype
 *
 * @constructor Creates a new [SwapMutator] with the given [probability]
 */
class SwapMutator<DNA>(probability: Double = 0.2) : Mutator<DNA>(probability) {
    override fun mutateChromosome(
        chromosome: Chromosome<DNA>,
        prob: Double
    ) = if (chromosome.size > 1) {
        val genes = chromosome.genes.toMutableList()
        val mutations = Core.rng.indexes(prob, genes.size)
            .map { genes.swap(it, Core.rng.nextInt(genes.size)) }
            .count()
        MutatorResult(chromosome.duplicate(genes), mutations)
    } else {
        MutatorResult(chromosome, 0)
    }
}

