/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.*
import kotlin.math.min


/**
 * Performs a crossover between two genotypes using a random single-point crossover.
 *
 * @param DNA The type of the gene's value.
 * @property probability The probability of crossover.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class SinglePointCrossover<DNA>(probability: Double) : MultiPointCrossover<DNA>(probability, 1) {

    override fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int {
        val index = Core.random.nextInt(min(genes1.size, genes2.size))
        val crossed = crossoverAt(index, genes1 to genes2)
        genes1.clear()
        genes1.addAll(crossed.first)
        genes2.clear()
        genes2.addAll(crossed.second)
        return 2
    }

    override fun crossover(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        enforce { chromosomes.size should BeEqualTo(2) }
        val first = chromosomes[0].genes.toMutableList()
        val second = chromosomes[1].genes.toMutableList()
        val index = Core.random.nextInt(min(first.size, second.size))
        val crossed = crossoverAt(index, first to second)
        return listOf(
            chromosomes[0].duplicate(genes = crossed.first),
            chromosomes[1].duplicate(genes = crossed.second)
        )
    }

    /**
     * Performs a crossover between two lists of genes at the given index.
     */
    internal fun crossoverAt(
        index: Int,
        mates: Pair<List<Gene<DNA>>, List<Gene<DNA>>>
    ): Pair<List<Gene<DNA>>, List<Gene<DNA>>> {
        val hi = min(mates.first.size, mates.second.size)
        enforce { index should BeInRange(0..hi) }
        val newFirst = mates.first.slice(0 until index) + mates.second.slice(index until hi)
        val newSecond = mates.second.slice(0 until index) + mates.first.slice(index until hi)
        return newFirst to newSecond
    }

    override fun toString() = "SinglePointCrossover { probability: $probability }"
}
