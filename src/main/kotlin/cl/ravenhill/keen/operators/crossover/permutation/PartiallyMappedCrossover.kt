package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.subset

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
class PartiallyMappedCrossover<DNA>(probability: Double) :
        AbstractPermutationCrossover<DNA>(probability) {

//    override fun doCrossover(
//        genes1: MutableList<Gene<DNA>>,
//        genes2: MutableList<Gene<DNA>>,
//        size: Int
//    ): Int {
//        // Select two random indexes
//        val (lo, hi) = Core.random.subset(pick = 2, from = size)
//        // Create the crossing region
//        val crossSection1 = genes1.subList(lo, hi)
//        val crossSection2 = genes2.subList(lo, hi)
//        // The offspring are created
//        for (i in 0 until size) {
//            if (i in lo until hi) continue
//            val gene1 = genes1[i]
//            val gene2 = genes2[i]
//            // If the gene is already in the crossing region, we don't need to do anything
//            if (gene1 in crossSection1 || gene2 in crossSection2) continue
//            // If the gene is not in the crossing region, we need to find the gene that is in the
//            // crossing region and replace it with the gene that is not in the crossing region
//            val gene1Index = genes1.indexOf(gene1)
//            val gene2Index = genes2.indexOf(gene2)
//            val gene1InCrossingRegion = genes1[gene2Index]
//            val gene2InCrossingRegion = genes2[gene1Index]
//            genes1[gene1Index] = gene1InCrossingRegion
//            genes2[gene2Index] = gene2InCrossingRegion
//        }
//        return 1
//    }

    override fun crossover(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        TODO("Not yet implemented")
    }

    override fun doCrossover(chromosomes: List<Chromosome<DNA>>): List<List<Gene<DNA>>> {
        TODO("Not yet implemented")
    }
}