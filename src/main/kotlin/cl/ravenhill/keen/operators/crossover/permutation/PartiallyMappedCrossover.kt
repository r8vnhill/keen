package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.util.Subset
import cl.ravenhill.keen.util.indexOf
import cl.ravenhill.keen.util.validatePredicate

/**
 * Alias for [PartiallyMappedCrossover].
 */
typealias PMX<DNA> = PartiallyMappedCrossover<DNA>

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
class PartiallyMappedCrossover<DNA>(probability: Double) : AbstractCrossover<DNA>(probability) {
    override fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int {
        validatePredicate({ genes1.distinct().size == genes1.size }) { "Partially mapped crossover can't have duplicated genes: $genes1" }
        validatePredicate({ genes2.distinct().size == genes2.size }) { "Partially mapped crossover can't have duplicated genes: $genes2" }
        val size = minOf(genes1.size, genes2.size)
        // Select two random indexes
        val (lo, hi) = Subset.next(size, 2)
        // Create the crossing region
        val crossSection1 = genes1.subList(lo, hi)
        val crossSection2 = genes2.subList(lo, hi)
        // The offspring are created
        for (i in 0 until size) {
            if (i in lo until hi) continue
            val gene1 = genes1[i]
            val gene2 = genes2[i]
            // If the gene is already in the crossing region, we don't need to do anything
            if (gene1 in crossSection1 || gene2 in crossSection2) continue
            // If the gene is not in the crossing region, we need to find the gene that is in the
            // crossing region and replace it with the gene that is not in the crossing region
            val gene1Index = genes1.indexOf(gene1)
            val gene2Index = genes2.indexOf(gene2)
            val gene1InCrossingRegion = genes1[gene2Index]
            val gene2InCrossingRegion = genes2[gene1Index]
            genes1[gene1Index] = gene1InCrossingRegion
            genes2[gene2Index] = gene2InCrossingRegion
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