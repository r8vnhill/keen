/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
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
 */
abstract class AbstractCrossover<DNA>(probability: Double) :
        AbstractRecombinatorAlterer<DNA>(probability, 2) {

    /**
     * Performs the crossover operation
     *
     * @param mates The pair of Genotypes to crossover
     * @return  The new Genotype
     */
    open fun crossover(mates: Pair<Genotype<DNA>, Genotype<DNA>>): Genotype<DNA> {
        val offspring = mutableListOf<Chromosome<DNA>>()
        for (i in mates.first.chromosomes.indices) {
            crossover(mates.first.chromosomes[i] to mates.second.chromosomes[i]).let {
                offspring.add(it)
            }
        }
        return mates.first.duplicate(offspring)
    }

    /**
     * Performs a crossover between two chromosomes and returns the new chromosome.
     *
     * @param mates The pair of chromosomes to crossover
     */
    protected abstract fun crossover(mates: Pair<Chromosome<DNA>, Chromosome<DNA>>): Chromosome<DNA>

    override fun recombine(
        population: MutableList<Phenotype<DNA>>,
        individuals: IntArray,
        generation: Int
    ): Int {
        val phenotype1 = population[individuals[0]]
        val phenotype2 = population[individuals[1]]
        val genotype1 = phenotype1.genotype
        val genotype2 = phenotype2.genotype
        val chromosomeIndex = Core.rng.nextInt(min(genotype1.size, genotype2.size))
        val chromosomes1 = genotype1.chromosomes.toMutableList()
        val chromosomes2 = genotype2.chromosomes.toMutableList()
        val genes1 = chromosomes1[chromosomeIndex].genes.toMutableList()
        val genesa = chromosomes1[chromosomeIndex].genes.toMutableList()
        val genes2 = chromosomes2[chromosomeIndex].genes.toMutableList()
        crossover(genes1, genes2)
        chromosomes1[chromosomeIndex] = chromosomes1[chromosomeIndex].duplicate(genes1)
        chromosomes2[chromosomeIndex] = chromosomes2[chromosomeIndex].duplicate(genes2)
        population[individuals[0]] = Phenotype(genotype1.duplicate(chromosomes1), generation)
        population[individuals[1]] = Phenotype(genotype1.duplicate(chromosomes2), generation)
        return order
    }

    abstract fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int
}