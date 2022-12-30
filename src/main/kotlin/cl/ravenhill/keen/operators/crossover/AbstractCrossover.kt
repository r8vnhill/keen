/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.AbstractRecombinatorAlterer
import kotlin.math.min


/**
 * Abstract class for crossover operators.
 *
 * @param DNA   The type of the DNA of the Genotype
 * @property probability    The probability of crossover
 *
 * @constructor Creates a new crossover operator
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.3.0
 */
abstract class AbstractCrossover<DNA>(probability: Double) :
        AbstractRecombinatorAlterer<DNA>(probability, 2) {

    override fun recombine(
        population: MutableList<Phenotype<DNA>>,
        individuals: IntArray,
        generation: Int
    ): Int {
        // Get the parents
        val phenotype1 = population[individuals[0]]
        val phenotype2 = population[individuals[1]]
        // Get the parents' genotype
        val genotype1 = phenotype1.genotype
        val genotype2 = phenotype2.genotype
        // Selects the index of the crossover point
        val chromosomeIndex = Core.random.nextInt(min(genotype1.size, genotype2.size))
        // Get the parents' chromosomes
        val chromosomes1 = genotype1.chromosomes.toMutableList()
        val chromosomes2 = genotype2.chromosomes.toMutableList()
        val genes1 = chromosomes1[chromosomeIndex].genes.toMutableList()
        val genes2 = chromosomes2[chromosomeIndex].genes.toMutableList()
        crossover(genes1, genes2)
        chromosomes1[chromosomeIndex] = chromosomes1[chromosomeIndex].duplicate(genes1)
        chromosomes2[chromosomeIndex] = chromosomes2[chromosomeIndex].duplicate(genes2)
        population[individuals[0]] = Phenotype(genotype1.duplicate(chromosomes1), generation)
        population[individuals[1]] = Phenotype(genotype1.duplicate(chromosomes2), generation)
        return order
    }

    /**
     * Performs the crossover operation.
     * @return Int
     */
    abstract fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int
}