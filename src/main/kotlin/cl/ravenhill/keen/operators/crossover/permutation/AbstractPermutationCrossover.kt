package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.requirements.IntRequirement.*
import cl.ravenhill.keen.util.validatePredicate


/**
 * A _Permutation Crossover_ operator is a crossover operator that works with a list of
 * genes that represent a permutation of a set of elements (without duplication).
 *
 * @param DNA the type of the DNA.
 * @param probability the probability of crossover.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
abstract class AbstractPermutationCrossover<DNA>(probability: Double) :
    AbstractCrossover<DNA>(probability) {
    override fun crossover(
        genes1: MutableList<Gene<DNA>>,
        genes2: MutableList<Gene<DNA>>
    ): Int {
        validatePredicate({ genes1.distinct().size == genes1.size }) { "A permutation crossover can't have duplicated genes: $genes1" }
        validatePredicate({ genes2.distinct().size == genes2.size }) { "A permutation crossover can't have duplicated genes: $genes2" }
        val size = minOf(genes1.size, genes2.size)
        doCrossover(genes1, genes2, size)
        return 1
    }

    protected abstract fun doCrossover(
        genes1: MutableList<Gene<DNA>>,
        genes2: MutableList<Gene<DNA>>,
        size: Int
    ): Int

    // Documentation is inherited from AbstractRecombinatorAlterer
    override fun crossover(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        enforce {
            for (chromosome in chromosomes) {
                chromosome.genes.distinct().size should BeEqualTo(chromosome.genes.size) {
                    "A permutation crossover can't have duplicated genes: ${chromosome.genes}"
                }
            }
        }
        crossover(chromosomes)
        TODO("Not yet implemented")
    }
}