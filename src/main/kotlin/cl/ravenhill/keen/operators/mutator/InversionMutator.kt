package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.util.subset


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
    ) = if (chromosome.size < 2) {
        MutatorResult(chromosome, 0)
    } else {
        val (start, end) = Core.random.subset(pick = 2, from = chromosome.size)
        val genes = chromosome.genes.toMutableList()
        var i = start
        var j = end - 1
        while (i < j) {
            val tmp = genes[i]
            genes[i] = genes[j]
            genes[j] = tmp
            i++
            j--
        }
        MutatorResult(chromosome.duplicate(genes), 1)
    }
}
