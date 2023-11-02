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
import cl.ravenhill.keen.util.neq

/**
 * The inversion mutator is a simple mutation operator, which inverts the order of a given
 * sub-sequence of the genotype.
 * The sub-sequence is determined by the mutation [probability].
 *
 * __References:__
 * - [Analyzing the Performance of Mutation Operators to Solve the Travelling Salesman Problem](https://arxiv.org/ftp/arxiv/papers/1203/1203.3099.pdf)
 *
 * @param DNA The type of the DNA
 * @param probability The probability of mutating a genotype
 * @param chromosomeRate The probability of mutating a chromosome
 * @param inversionBoundaryProbability The probability of inverting a sub-sequence
 *
 * @constructor Creates a new [InversionMutator] with the given [probability]
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.3.0
 * @version 2.0.0
 */
class InversionMutator<DNA, G : Gene<DNA, G>>(
    override val probability: Double = 0.5,
    override val chromosomeRate: Double = 0.5,
    val inversionBoundaryProbability: Double = 0.5,
) : ChromosomeMutator<DNA, G> {

    /* Documentation inherited from [Mutator] */
    override fun mutateChromosome(
        chromosome: Chromosome<DNA, G>,
    ) =
        if (chromosome.size > 1 &&
            inversionBoundaryProbability neq 0.0 &&
            Core.random.nextDouble() < chromosomeRate
        ) {
            val genes = chromosome.genes.toMutableList()
            var start = 0
            var end = chromosome.size - 1
            for (i in 0 until chromosome.size) {
                if (Core.random.nextDouble() < inversionBoundaryProbability) {
                    start = i
                    break
                }
            }
            for (i in start until chromosome.size) {
                if (Core.random.nextDouble() > inversionBoundaryProbability) {
                    end = i
                    break
                }
            }
            val inverted = invert(genes, start, end)
            MutatorResult(chromosome.withGenes(inverted), 1)
        } else {
            MutatorResult(chromosome, 0)
        }

    /**
     * Inverts the order of the genes in the given `genes` list, between the given `start` and `end`
     * indexes.
     */
    private fun invert(genes: List<G>, start: Int, end: Int): List<G> {
        val invertedGenes = genes.toMutableList()

        // Iterate over half the range of genes to swap their positions.
        for (i in start until (start + (end - start + 1) / 2)) {
            // Calculate the corresponding index to swap with.
            val j = end - (i - start)
            // Swap the positions of the genes at indices i and j.
            val tmp = invertedGenes[i]
            invertedGenes[i] = invertedGenes[j]
            invertedGenes[j] = tmp
        }

        return invertedGenes
    }
}
