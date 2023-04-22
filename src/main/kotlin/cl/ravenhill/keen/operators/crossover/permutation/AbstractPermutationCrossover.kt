package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo


/**
 * A _Permutation Crossover_ operator is a crossover operator that works with a list of
 * genes that represent a permutation of a set of elements (without duplication).
 *
 * @param DNA The type of the elements in the genes.
 * @param probability The probability of performing crossover on each individual of the population.
 * @param numOut The number of individuals produced by the crossover (default: 2).
 * @param numIn The number of individuals required to perform the crossover (default: 2).
 * @param exclusivity whether a parent can be used more than once (default: false)
 * @param chromosomeRate The rate of chromosomes that will undergo crossover (default: 1.0).
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.2.0
 * @version 2.0.0
 */
abstract class AbstractPermutationCrossover<DNA, G : Gene<DNA, G>>(
    probability: Double,
    numOut: Int = 2,
    numIn: Int = 2,
    exclusivity: Boolean = false,
    chromosomeRate: Double = 1.0
) : AbstractCrossover<DNA, G>(probability, numOut, numIn, exclusivity, chromosomeRate) {

    /// Documentation inherited from [AbstractCrossover]
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        enforce {
            for (chromosome in chromosomes) {
                "A permutation crossover can't have duplicated genes: ${chromosome.genes}" {
                    chromosome.genes.distinct().size should BeEqualTo(chromosome.genes.size)
                }
            }
        }
        val crossed = doCrossover(chromosomes)
        return crossed.map { chromosomes[0].withGenes(genes = it) }
    }

    /**
     * Performs permutation crossover on a list of chromosomes.
     */
    protected abstract fun doCrossover(chromosomes: List<Chromosome<DNA, G>>): List<List<G>>
}