/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.AbstractRecombinatorAlterer

/**
 * An abstract class for implementing crossover operations between two individuals.
 *
 * This class extends the [AbstractRecombinatorAlterer] class and provides a framework for
 * implementing different crossover strategies.
 *
 * @param DNA   The type of the DNA of the Genotype
 * @property probability    The probability of crossover
 * @property chromosomeRate The rate of crossover between chromosomes
 *
 * @constructor Creates a new crossover operator
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 */
abstract class AbstractCrossover<DNA>(
    probability: Double,
    numOut: Int = 2,
    numIn: Int = 2,
    exclusivity: Boolean = false,
    chromosomeRate: Double = 1.0,
) : AbstractRecombinatorAlterer<DNA>(probability, numOut, numIn, exclusivity, chromosomeRate) {

    /**
     * Performs the crossover operation.
     * @return Int
     */
    abstract fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int
}
