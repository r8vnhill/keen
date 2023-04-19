/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.MutablePopulation
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.Alterer

/**
 * Represents a type of genetic operator that performs crossover, which is a method of recombining
 * genetic information from two or more individuals to create a new individual.
 *
 * @param DNA The type of data that represents an individual's genotype.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Crossover<DNA, G: Gene<DNA, G>> : Alterer<DNA, G> {

    /**
     * Applies crossover to the individuals in the population specified by the indices and returns
     * the number of offspring produced by the crossover.
     *
     * Crossover is a genetic operator that recombines genetic information from two or more
     * individuals in a population to create a new individual with a combination of traits from
     * the parents.
     * The number of offspring produced by the crossover is returned by this function.
     *
     * @param population The population of individuals to which the crossover will be applied.
     * @param indices A list of indices representing the individuals in the population that will
     *      participate in the crossover.
     * @return The number of offspring produced by the crossover.
     */
    fun crossover(population: MutablePopulation<DNA, G>, indices: List<Int>): List<List<Chromosome<DNA, G>>>
}