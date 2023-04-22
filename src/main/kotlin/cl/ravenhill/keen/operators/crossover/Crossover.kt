/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.Genotype
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


    fun crossover(inGenotypes: List<Genotype<DNA, G>>): List<Genotype<DNA, G>>
}