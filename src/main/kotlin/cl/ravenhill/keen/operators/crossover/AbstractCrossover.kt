/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.core.Genotype
import cl.ravenhill.keen.core.KeenCore
import cl.ravenhill.keen.core.chromosomes.Chromosome
import cl.ravenhill.keen.operators.Alterer
import kotlin.random.asKotlinRandom


/**
 * Abstract class for crossover operators.
 *
 * @param DNA   The type of the DNA of the Genotype
 * @property probability    The probability of crossover
 *
 * @constructor Creates a new crossover operator
 */
abstract class AbstractCrossover<DNA>(override val probability: Double) : Alterer<DNA> {

    override fun invoke(population: List<Genotype<DNA>>): List<Genotype<DNA>> {
        return population.map {
            val mate = population.random(KeenCore.generator.asKotlinRandom())
            crossover(it to mate)
        }
    }

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
        return mates.first.copy(offspring)
    }

    /**
     * Performs a crossover between two chromosomes and returns the new chromosome.
     *
     * @param mates The pair of chromosomes to crossover
     */
    protected abstract fun crossover(mates: Pair<Chromosome<DNA>, Chromosome<DNA>>): Chromosome<DNA>
}