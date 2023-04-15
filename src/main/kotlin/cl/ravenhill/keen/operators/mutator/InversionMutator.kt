package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.util.eq


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
 *
 * @constructor Creates a new [InversionMutator] with the given [probability]
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.3.0
 * @version 2.0.0
 */
class InversionMutator<DNA, G: Gene<DNA, G>>(probability: Double) : Mutator<DNA, G>(probability) {
    override fun mutateChromosome(
        chromosome: Chromosome<DNA, G>,
    ) = if (probability eq 0.0 || chromosome.size < 2) {
        // If the mutation probability is 0 or the chromosome is too small, return the original
        // chromosome
        MutatorResult(chromosome, 0)
    } else {
        var start = 0
        var end = chromosome.size - 1
        for (i in 0 until chromosome.size) {
            if (Core.Dice.probability() < probability) {
                start = i
                break
            }
        }
        for (i in start until chromosome.size) {
            if (Core.Dice.probability() > probability) {
                end = i
                break
            }
        }
        val genes = chromosome.genes.toMutableList()
        invert(genes, start, end)
        MutatorResult(chromosome.withGenes(genes), 1)
    }

    private fun getRandomIndices(size: Int, probability: Double): Pair<Int, Int> {
        var start = 0
        var end = size - 1
        for (i in 0 until size) {
            if (Core.Dice.probability() < probability) {
                start = i
                break
            }
        }
        for (i in start until size) {
            if (Core.Dice.probability() > probability) {
                end = i
                break
            }
        }
        return start to end
    }

    /**
     * Inverts the order of the genes in the given `genes` list, between the given `start` and `end`
     * indexes.
     */
    private fun invert(genes: MutableList<G>, start: Int, end: Int) {
        // Iterate over half the range of genes to swap their positions.
        for (i in start until (start + (end - start + 1) / 2)) {
            // Calculate the corresponding index to swap with.
            val j = end - (i - start)
            // Swap the positions of the genes at indices i and j.
            val tmp = genes[i]
            genes[i] = genes[j]
            genes[j] = tmp
        }
    }
}
