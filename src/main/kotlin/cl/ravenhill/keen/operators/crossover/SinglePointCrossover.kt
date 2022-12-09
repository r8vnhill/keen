/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.swap
import cl.ravenhill.keen.util.validateRange
import kotlin.math.min


/**
 * Performs a crossover between two genotypes using a random single-point crossover.
 *
 * @param DNA The type of the gene's value.
 * @property probability The probability of crossover.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class SinglePointCrossover<DNA>(probability: Double) : MultiPointCrossover<DNA>(probability, 1) {

    override fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int {
        val index = Core.rng.nextInt(min(genes1.size, genes2.size))
        crossoverAt(index, genes1 to genes2)
        return 2
    }

    private fun crossoverAt(index: Int, mates: Pair<MutableList<Gene<DNA>>, MutableList<Gene<DNA>>>) {
        val hi = min(mates.first.size, mates.second.size)
        index.validateRange(0 until hi)
        mates.first.swap(index, hi, mates.second, index)
    }

    override fun toString() = "SinglePointCrossover { probability: $probability }"
}
