package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.util.math.eq


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
 * @version 1.3.0
 */
class InversionMutator<DNA>(probability: Double) : Mutator<DNA>(probability) {
    override fun mutateChromosome(
        chromosome: Chromosome<DNA>,
    ) = if (probability eq 0.0 || chromosome.size < 2) {
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
        MutatorResult(chromosome.duplicate(genes), 1)
    }

    /**
     * Inverts the order of the genes in the given [genes] list, between the given [start] and [end] indexes.
     */
    private fun invert(genes: MutableList<Gene<DNA>>, start: Int, end: Int) {
        var (i, j) = if (start < end) start to end else end to start
        while (i < j) {
            val tmp = genes[i]
            genes[i] = genes[j]
            genes[j] = tmp
            i++
            j--
        }
    }
}
