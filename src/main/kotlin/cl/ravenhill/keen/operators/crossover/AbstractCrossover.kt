/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.MutablePopulation
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.operators.AbstractAlterer
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.operators.GeneticOperationResult
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.get
import cl.ravenhill.keen.util.indices
import cl.ravenhill.keen.util.math.neq
import cl.ravenhill.keen.util.subsets


/**
 * An abstract class for performing crossover operations on individuals within a population.
 * Subclasses must implement the [crossoverChromosomes] method for actual crossover functionality.
 *
 * @param probability The probability that a crossover operation will be performed on a given pair of individuals
 * @param numOut The number of new individuals produced by each crossover operation
 * @param numIn The number of individuals required as input for each crossover operation
 * @param exclusivity If true, individuals cannot be selected more than once for a given crossover operation
 * @param chromosomeRate The probability that a given chromosome within an individual will be selected for recombination
 */
abstract class AbstractCrossover<DNA>(
    probability: Double,
    private val numOut: Int,
    private val numIn: Int = 2,
    private val exclusivity: Boolean = false,
    protected val chromosomeRate: Double = 1.0
) : AbstractAlterer<DNA>(probability), Crossover<DNA> {

    init {
        enforce {
            numIn should BeAtLeast(2) {
                "There should be at least 2 inputs to perform a crossover operation"
            }
        }
    }

    // Documentation inherited from Alterer interface
    override fun invoke(
        population: Population<DNA>,
        generation: Int
    ): GeneticOperationResult<DNA> {
        val pop = population.toMutableList()
        // check if probability is non-zero and there are at least 2 individuals in the population
        return if (probability neq 0.0 && pop.size >= 2) {
            // select a subset of individuals to recombine using the provided probability and other parameters
            val indices = Core.random.indices(probability, pop.size)
            if (indices.size < numIn) return AltererResult(pop)
            val parents = Core.random.subsets(indices, exclusivity, numIn)
            // recombine the selected parents and count the number of individuals that were recombined
            val count = parents.sumOf { crossover(pop, it) }
            // return the resulting population and count
            AltererResult(pop, count)
        } else {
            // if probability is zero or there are less than 2 individuals in the population, return
            // the original population
            AltererResult(pop)
        }
    }

    override fun crossover(population: MutablePopulation<DNA>, indices: List<Int>): Int {
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
        val recombined = chromosomes.map { crossoverChromosomes(it) }
        // return the number of newly recombined individuals
        return recombined.size * numOut
    }

    protected abstract fun crossoverChromosomes(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>>
}
