package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.requirements.IntRequirement.*
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
 * @param chromosomeRate the proportion of chromosomes to recombine (default: 1.0)
 * @param DNA the type of the genetic material
 */
abstract class AbstractRecombinatorAlterer<DNA>(
    probability: Double,
    protected val numOut: Int,
    private val numIn: Int = 2,
    private val exclusivity: Boolean = false,
    private val chromosomeRate: Double = 1.0
) : AbstractAlterer<DNA>(probability) {

    init {
        enforce { numOut should BeAtLeast(2) { "The order must be at least 2" } }
    }

    override fun invoke(
        population: Population<DNA>,
        generation: Int
    ): AltererResult<DNA> {
        val pop = population.toMutableList()
        return if (probability neq 0.0 && pop.size >= 2) {
            val indices = Core.random.indices(probability, pop.size)
            val parents = Core.random.subsets(indices, exclusivity, numOut)
            val count = parents.sumOf { recombine(pop, it, generation) }
            AltererResult(pop, count)
        } else {
            AltererResult(pop)
        }
    }

    /**
     * Recombines the individuals at the given indices.
     *
     * @param population the population to recombine
     * @param indices the indices of the individuals to recombine
     * @param generation the current generation
     * @return the number of individuals that were recombined
     */
    private fun recombine(
        population: MutableList<Phenotype<DNA>>,
        indices: List<Int>,
        generation: Int
    ): Int {
        enforce { indices.size should BeEqualTo(numOut) }
        val individuals = population[indices]
        enforce { individuals.map { it.genotype.size }.distinct().size should BeEqualTo(1) }
        val genotypes = individuals.map { it.genotype }
        val chIndices = Core.random.indices(chromosomeRate, genotypes[0].size)
        val chromosomes = genotypes.map { it.chromosomes[chIndices] }
        val recombined = chromosomes.map { recombineChromosomes(it) }
        return numOut * recombined.size
    }

    abstract fun recombineChromosomes(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>>
}
