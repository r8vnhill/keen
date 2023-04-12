package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.subsets

/**
 * Alias for [PartiallyMappedCrossover].
 */
typealias PMX<DNA, G> = PartiallyMappedCrossover<DNA, G>

/**
 * A genetic operator that performs partially matched crossover (PMX) on a pair of chromosomes that
 * are permutations of the same set of elements.
 * This operator is suitable for solving optimization problems where the order of the elements
 * is important, such as the traveling salesman problem.
 *
 * PMX is a variation of the ordered crossover operator that partially maps one parent's genes to
 * the other parent's genes.
 * The operator selects two random crossover points and swaps the genes that fall within the region
 * between those points.
 * The operator then performs a matching procedure to map the genes that were swapped to the genes
 * in the same position in the other parent's chromosome, ensuring that no duplicate genes are
 * present.
 * The result is two offspring that are permutations of the original set of genes.
 *
 * This implementation of PMX operates on a list of chromosomes, each of which contains a list of
 * genes that encode the permutation.
 * The operator requires exactly two chromosomes in the input list and returns a list of two
 * offspring.
 *
 * # Pseudo-code
 *
 * ```
 * function partiallyMappedCrossover(parent1, parent2):
 *     offspring1 = makeCopy(parent1)
 *     offspring2 = makeCopy(parent2)
 *     // Select two random indexes
 *     lo, hi = randomIndexes(parent1)
 *     // Create the crossing region
 *     crossSection1 = parent1[lo:hi]
 *     crossSection2 = parent2[lo:hi]
 *     // Replace the values outside the crossing region
 *     for i in range(length(parent1)):
 *         if i < lo or i >= hi:
 *             if offspring1[i] in crossSection2:
 *                 index = crossSection2.index(offspring1[i])
 *                 offspring1[i] = crossSection1[index]
 *             if offspring2[i] in crossSection1:
 *                 index = crossSection1.index(offspring2[i])
 *                 offspring2[i] = crossSection2[index]
 *     return offspring1, offspring2
 * ```
 *
 * # Example
 *
 * Parent 1: ``[1 2 3 4 5 6 7 8 9 10]``
 *
 * Parent 2: ``[5 3 1 8 6 2 9 7 4 10]``
 *
 * Let's assume that the crossover is being performed between positions 3 and 7.
 *
 * ## Here are the steps of the algorithm
 *
 * 1. Select the crossover region, which is genes 3 to 7 in both parent chromosomes.
 * 2. Create the offspring by copying the genes outside the crossover region to the corresponding
 *    positions in the offspring chromosomes.
 * 3. For each gene in the crossover region, find the corresponding gene in the other parent
 *    chromosome.
 * 4. If the corresponding gene is also in the crossover region, skip it.
 * 5. If the corresponding gene is not in the crossover region, swap it with the gene in the same
 *    position in the other parent chromosome.
 *
 * ## Here's the resulting offspring
 *
 * Offspring 1: ``[1 2 3 8 6 7 9 4 5 10]``
 *
 * Offspring 2: ``[5 3 1 4 2 6 9 7 8 10]``
 *
 * As you can see, the genes outside the crossover region are copied to the offspring chromosomes as
 * they are, and only the genes in the crossover region are modified by swapping them with the
 * corresponding genes in the other parent chromosome.
 *
 * @param probability the probability that the operator is applied to a pair of chromosomes
 * @param DNA the type of the elements in the permutation
 * @param G the type of the gene that encodes the permutation
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.2.0
 * @version 2.0.0
 */
class PartiallyMappedCrossover<DNA, G : Gene<DNA, G>>(probability: Double) :
        AbstractPermutationCrossover<DNA, G>(probability) {

    /// Documentation inherited from [AbstractPermutationCrossover]
    override fun doCrossover(chromosomes: List<Chromosome<DNA, G>>): List<List<G>> {
        enforce {
            "Partially mapped crossover requires exactly two chromosomes" {
                chromosomes.size should BeEqualTo(2)
            }
        }
        val genes1 = chromosomes[0].genes.toMutableList()
        val genes2 = chromosomes[1].genes.toMutableList()
        val (lo, hi) = selectRandomIndexes(chromosomes[0].size)
        val (crossSection1, crossSection2) = createCrossingRegions(genes1, genes2, lo, hi)
        replaceGenesOutsideCrossingRegions(genes1, genes2, crossSection1, crossSection2, lo, hi)
        return listOf(genes1, genes2)
    }

    private fun selectRandomIndexes(size: Int): Pair<Int, Int> {
        val (lo, hi) = Core.random.subsets(List(size) { it }, true, 2, 1)
            .first()
            .sorted()
        return Pair(lo, hi)
    }

    private fun createCrossingRegions(
        genes1: MutableList<G>,
        genes2: MutableList<G>,
        lo: Int,
        hi: Int
    ): Pair<List<G>, List<G>> {
        val crossSection1 = genes1.subList(lo, hi)
        val crossSection2 = genes2.subList(lo, hi)
        return Pair(crossSection1, crossSection2)
    }

    private fun replaceGenesOutsideCrossingRegions(
        genes1: MutableList<G>,
        genes2: MutableList<G>,
        crossSection1: List<G>,
        crossSection2: List<G>,
        lo: Int,
        hi: Int
    ) {
        for (i in 0 until genes1.size) {
            if (i in lo until hi) continue
            val gene1 = genes1[i]
            val gene2 = genes2[i]
            if (gene1 in crossSection1 || gene2 in crossSection2) continue
            val gene1Index = genes1.indexOf(gene1)
            val gene2Index = genes2.indexOf(gene2)
            val gene1InCrossingRegion = genes1[gene2Index]
            val gene2InCrossingRegion = genes2[gene1Index]
            genes1[gene1Index] = gene1InCrossingRegion
            genes2[gene2Index] = gene2InCrossingRegion
        }
    }
}