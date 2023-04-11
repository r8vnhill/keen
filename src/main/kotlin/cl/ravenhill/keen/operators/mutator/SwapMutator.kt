package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.int
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
 * @param probability The probability of mutating a genotype
 *
 * @constructor Creates a new [SwapMutator] with the given [probability]
 */
class SwapMutator<DNA, G: Gene<DNA, G>>(probability: Double = 0.2) : Mutator<DNA, G>(probability) {
    override fun mutateChromosome(
        chromosome: Chromosome<DNA, G>,
    ) = if (probability neq 0.0 && chromosome.size > 1) {
        val genes = chromosome.genes.toMutableList()
        val indices = Dice.random.indices(probability, genes.size)
        val mutations = indices
            .map { genes.swap(it, Dice.int(genes.size)) }
            .count()
        MutatorResult(chromosome.withGenes(genes), mutations)
    } else {
        MutatorResult(chromosome, 0)
    }
}

