package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.get
import cl.ravenhill.keen.util.indices
import cl.ravenhill.keen.util.math.neq
import cl.ravenhill.keen.util.subsets


/**
 * An abstract class for recombining the genes of individuals in a population.
 * This class extends the [AbstractAlterer] class and provides a framework for implementing
 * different recombination strategies.
 *
 * @param probability the probability of applying the recombination operation to an individual in
 *      the population
 * @param numOut the number of individuals to create through recombination
 * @param numIn the number of parents to use for recombination (default: 2)
 * @param exclusivity whether a parent can be used more than once (default: false)
 * @param chromosomeRate the probability of applying the recombination operation to a chromosome in
 *      the individual (default: 1.0)
 * @param DNA the type of the genetic material
 */
abstract class AbstractRecombinatorAlterer<DNA>(
    probability: Double,
    private val numOut: Int,
    private val numIn: Int = 2,
    private val exclusivity: Boolean = false,
    protected val chromosomeRate: Double = 1.0
) : AbstractAlterer<DNA>(probability) {

    init {
        enforce {
            numIn should BeAtLeast(2) {
                "There should be at least 2 inputs to perform a recombination"
            }
        }
    }

    // Documentation inherited from Alterer interface
    override fun invoke(
        population: Population<DNA>,
        generation: Int
    ): AltererResult<DNA> {
        val pop = population.toMutableList()
        // check if probability is non-zero and there are at least 2 individuals in the population
        return if (probability neq 0.0 && pop.size >= 2) {
            // select a subset of individuals to recombine using the provided probability and other parameters
            val indices = Core.random.indices(probability, pop.size)
            if (indices.size < numIn) return AltererResult(pop)
            val parents = Core.random.subsets(indices, exclusivity, numIn)
            // recombine the selected parents and count the number of individuals that were recombined
            val count = parents.sumOf { recombine(pop, it) }
            // return the resulting population and count
            AltererResult(pop, count)
        } else {
            // if probability is zero or there are less than 2 individuals in the population, return
            // the original population
            AltererResult(pop)
        }
    }

    /**
     * Recombines the individuals at the given indices.
     *
     * @param population the population to recombine
     * @param indices the indices of the individuals to recombine
     * @return the number of individuals that were recombined
     */
    private fun recombine(
        population: MutableList<Phenotype<DNA>>, // the population to be recombined
        indices: List<Int> // indices of individuals to be recombined
    ): Int {
        enforce { indices.size should BeEqualTo(numIn) }
        // get the individuals at the specified indices
        val individuals = population[indices]
        // enforce that all individuals have the same genotype length
        enforce { individuals.map { it.genotype.size }.distinct().size should BeEqualTo(1) }
        // extract the genotypes of the individuals
        val genotypes = individuals.map { it.genotype }
        // randomly select indices of chromosomes to recombine
        val chIndices = Core.random.indices(chromosomeRate, genotypes[0].size)
        // Associate the chromosomes at the selected indices
        val chromosomes = chIndices.map { i -> genotypes.map { it[i] } }
        // recombine the chromosomes to create new individuals
        val recombined = chromosomes.map { crossover(it) }
        // return the number of newly recombined individuals
        return recombined.size * numOut
    }

    /**
     * Recombines the given list of chromosomes to create a new list of chromosomes.
     * The specific recombination strategy is implemented in subclasses of this abstract class.
     *
     * @param chromosomes the list of chromosomes to recombine
     * @return the resulting list of recombined chromosomes
     */
    protected abstract fun crossover(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>>
}
