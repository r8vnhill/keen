/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeInRange
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import kotlin.math.min


/**
 * A [MultiPointCrossover] with only one cut point.
 *
 * Given a list of two chromosomes, this crossover will randomly select a single crossover point,
 * and swap the genes of the two chromosomes up to that point, creating two new chromosomes.
 * This means that all genes to the left of the crossover point will be swapped between the
 * chromosomes, while all genes to the right will remain in their original positions.
 *
 * # Pseudo-code
 * ```
 * fun singlePointCrossover(P1, P2):
 *    i = randomIndex(0, min(P1.size, P2.size))
 *    O1 = P1[0..i] + P2[i+1..P2.size]
 *    O2 = P2[0..i] + P1[i+1..P1.size]
 *    return O1, O2
 * ```
 *
 * # Example
 *
 * Suppose we have ``P1 = [1, 2, 3, 4, 5]`` and ``P2 = [6, 7, 8, 9, 10]``.
 *
 * If the randomly generated index ``i = 2``, then the offspring would be:
 * ```
 * O1 = [1, 2, 8, 9, 10]
 * O2 = [6, 7, 3, 4, 5]
 * ```
 *
 * @param DNA The type of the values stored in the genes.
 * @param probability The probability of performing crossover on each individual of the population.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class SinglePointCrossover<DNA, G : Gene<DNA, G>>(val probability: Double) :
    MultiPointCrossover<DNA, G>(1) {

    /**
     * Performs a single point crossover between the given chromosomes.
     * Returns a list with two new chromosomes resulting from the crossover.
     *
     * @param chromosomes The list of [Chromosome]s to be crossed over.
     *
     * @return A list with two new [Chromosome]s resulting from the crossover.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        constraints {
            "The number of chromosomes to be crossed over must be 2." {
                chromosomes must HaveSize(2)
            }
        }
        val first = chromosomes[0].genes
        val second = chromosomes[1].genes
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
        constraints {
            "The index must be in the range [0, $hi)." { index must BeInRange(0..<hi) }
        }
        val newFirst = mates.first.slice(0..<index) + mates.second.slice(index..<hi)
        val newSecond = mates.second.slice(0..<index) + mates.first.slice(index..<hi)
        return newFirst to newSecond
    }

    /// Documentation inherited from [Any].
    override fun toString() = "SinglePointCrossover(probability=$probability)"
}
