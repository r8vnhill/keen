/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.requirements.IntRequirement.BeInRange
import kotlin.math.min


/**
 * A [MultiPointCrossover] with only one cut point.
 *
 * Given a list of two chromosomes, this crossover will select a random crossover point and swap the
 * genes of the two chromosomes up to that point, creating two new chromosomes.
 *
 * @param DNA The type of the values stored in the genes.
 * @param probability The probability of performing crossover on each individual of the population.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class SinglePointCrossover<DNA, G : Gene<DNA, G>>(probability: Double) :
        MultiPointCrossover<DNA, G>(probability, 1) {

    /**
     * Performs a single point crossover between the given chromosomes.
     * Returns a list with two new chromosomes resulting from the crossover.
     *
     * @param chromosomes The list of [Chromosome]s to be crossed over.
     *
     * @return A list with two new [Chromosome]s resulting from the crossover.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        enforce {
            "The number of chromosomes to be crossed over must be 2." {
                chromosomes.size should BeEqualTo(2)
            }
        }
        val first = chromosomes[0].genes.toMutableList()
        val second = chromosomes[1].genes.toMutableList()
        val index = Core.random.nextInt(min(first.size, second.size))
        val crossed = crossoverAt(index, first to second)
        return listOf(
            chromosomes[0].withGenes(genes = crossed.first),
            chromosomes[0].withGenes(genes = crossed.second)
        )
    }

    /**
     * Performs a crossover between two lists of genes at the given index.
     *
     * @param index The index where the crossover will be performed.
     * @param mates A [Pair] with the lists of genes that will be crossed over.
     *
     * @return A [Pair] with the crossed over lists of genes.
     */
    internal fun crossoverAt(
        index: Int,
        mates: Pair<List<G>, List<G>>
    ): Pair<List<G>, List<G>> {
        val hi = min(mates.first.size, mates.second.size)
        enforce {
            "The index must be in the range [0, $hi)." { index should BeInRange(0 until hi) }
        }
        val newFirst = mates.first.slice(0 until index) + mates.second.slice(index until hi)
        val newSecond = mates.second.slice(0 until index) + mates.first.slice(index until hi)
        return newFirst to newSecond
    }

    override fun toString() = "SinglePointCrossover { probability: $probability }"
}
