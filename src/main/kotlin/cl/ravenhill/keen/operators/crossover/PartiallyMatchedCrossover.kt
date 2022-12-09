package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.indexOf
import cl.ravenhill.keen.util.swap
import kotlin.math.min

typealias PMX<DNA> = PartiallyMatchedCrossover<DNA>

/**
 * The ``PartiallyMatchedCrossover`` (PMX) guarantees that all [Gene]s are found exactly once in
 * each chromosome.
 * The PMX can be applied usefully in the TSP or other permutation problem encodings.
 * Permutation encoding is useful for all problems where the fitness only depends on the ordering of
 * the genes within the chromosome.
 *
 * The PMX is similar to the two-point crossover. A crossing region is chosen
 * by selecting two crossing points.
 * ```
 *     C1 = 012|345|6789
 *     C2 = 987|654|3210
 * ```
 * After performing the crossover we normally got two invalid chromosomes.
 * ```
 *     C1 = 012|654|6789
 *     C2 = 987|345|3210
 * ```
 * Chromosome ``C1`` contains the value 6  twice and misses the value 3.
 * On  the other side chromosome ``C2``  contains the value 3 twice and misses the value 6.
 * We can observe that this crossover is equivalent to the exchange of the values ``3 -> 6``,
 * ``4 -> 5`` and ``5 -> 4``.
 * To repair the two chromosomes we have to apply this exchange outside the crossing region.
 * ```
 *     C1 = 012|654|3789
 *     C2 = 987|345|6210
 * ```
 *
 * @param DNA The type of the gene's value.
 * @property probability The probability of crossover.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class PartiallyMatchedCrossover<DNA>(probability: Double) : AbstractCrossover<DNA>(probability) {
    override fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int {
        val size = min(genes1.size, genes2.size)
        val r1 = Core.rng.nextInt(size)
        val r2 = Core.rng.nextInt(size)
        if (size >= 2) {
            val (start, end) = if (r1 < r2) r1 to r2 else r2 to r1
            genes1.swap(start = start, end = end, other = genes2, otherStart = start)
            repair(genes1 to genes2, start, end)
            repair(genes2 to genes1, start, end)
        }
        return 1
    }

    /**
     * Performs the chromosome reparations.
     *
     * @param genes The genes to repair.
     * @param start The start index.
     * @param end The end index.
     */
    private fun repair(
        genes: Pair<MutableList<Gene<DNA>>, MutableList<Gene<DNA>>>,
        start: Int,
        end: Int
    ) {
        for (i in 0 until start) {
            var index: Int = genes.first.indexOf(genes.first[i], start, end)
            while (index != -1) {
                genes.first[i] = genes.second[index]
                index = genes.first.indexOf(genes.first[i], start, end)
            }
        }
        for (i in end until genes.first.size) {
            var index: Int = genes.first.indexOf(genes.first[i], start, end)
            while (index != -1) {
                genes.first[i] = genes.second[index]
                index = genes.first.indexOf(genes.first[i], start, end)
            }
        }
    }
}


