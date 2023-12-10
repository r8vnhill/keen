/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.indices

/**
 * The `PartiallyMappedCrossover` (PMX) class implements a crossover mechanism designed for permutation-based
 * genetic algorithms. PMX is particularly effective in problems where preserving the relative order of genes
 * is crucial. This crossover method is commonly used in routing, scheduling, and sequencing problems where the
 * solution involves finding an optimal arrangement or path.
 *
 * ## Theoretical Background:
 * PMX crossover was developed to address the need for a crossover operator that respects the order of elements
 * in permutation-based problems. It works by partially mapping genes from one parent to another, ensuring that
 * offspring inherit characteristics from both parents while maintaining a valid permutation. The method involves
 * exchanging a segment of genes between two parent chromosomes and then resolving any conflicts outside this
 * segment to ensure each gene appears exactly once.
 *
 *
 * ## Crossover Logic:
 * 1. Randomly selects a segment (defined by two indices) within the parent chromosomes.
 * 2. Swaps these segments between the parents to form intermediate offspring.
 * 3. Resolves gene duplications outside the crossover segment by mapping corresponding genes between the parents.
 * 4. Produces two new offspring chromosomes that are valid permutations and contain traits from both parents.
 *
 * ### Example:
 * - Let's begin with two parent chromosomes: `I1 = [1 2 3 4 5 6 7 8 9]` and `I2 = [5 7 4 9 1 3 6 2 8]`
 * - Selects a crossover region from `I1`: `[3 4 5 6]`
 * - Replace the genes from the crossover region in `I2`: `[_ _ 4 9 1 3 _ _ _]
 * - Resolves gene duplications outside the crossover region by mapping corresponding genes between the parents until
 *   no duplicates remain:
 * - `O1 = [5 2 4 9 1 3 7 8 6]`
 *
 * ## Practical Usage:
 * PMX is beneficial in evolutionary algorithms for problems like the Traveling Salesman Problem (TSP), where the
 * order of cities (genes) in a route (chromosome) is essential. It ensures that offspring routes are valid and
 * explore new potential solutions derived from both parents, enhancing the algorithm's ability to search the
 * solution space effectively.
 *
 * ### Example - TSP:
 * ```
 * val pmxCrossover = PartiallyMappedCrossover<Double, CityGene>(chromosomeRate = 0.5, exclusivity = true)
 * // This crossover can be used in a genetic algorithm for solving TSP, ensuring valid and diverse routes in offspring.
 * ```
 *
 * @param T the type of value that the genes represent.
 * @param G the gene type, must extend Gene<T, G>.
 * @param chromosomeRate the probability of crossover between two chromosomes.
 * @param exclusivity a flag indicating whether the crossover is exclusive.
 * @property numOffspring the number of offspring produced by the crossover. This value is always 2.
 * @property numParents the number of parents required by the crossover. This value is always 2.
 * @constructor Creates a new instance of PartiallyMappedCrossover with the specified crossover rate and exclusivity.
 */
class PartiallyMappedCrossover<T, G>(
    override val chromosomeRate: Double,
    override val exclusivity: Boolean
) : PermutationCrossover<T, G> where G : Gene<T, G> {

    override val numOffspring = 2

    override val numParents = 2

    /**
     * Executes the permutation crossover operation for two chromosomes using the Partially Mapped Crossover (PMX)
     * method.
     * This function is fundamental to the PMX crossover, ensuring the correct exchange and mapping of genes between
     * parent chromosomes to create valid offspring permutations.
     *
     * ## Process:
     * 1. Validates that exactly two chromosomes are provided for the PMX crossover.
     * 2. Randomly selects a crossover region within the chromosomes.
     * 3. Exchanges the genes within this region between the two parent chromosomes to create intermediate offspring.
     * 4. Calls `replaceGenesOutsideCrossingRegions` to adjust the genes outside the crossover region, resolving any
     *    duplications and maintaining the integrity of the permutation.
     * 5. Returns the resulting pair of offspring chromosomes.
     *
     * ## Constraints:
     * - The method requires exactly two chromosomes for the PMX crossover to be correctly performed. This constraint
     *   ensures that the crossover logic can be applied effectively.
     *
     * ## Usage:
     * This method is invoked during the crossover phase in genetic algorithms that use PMX for solving problems where
     * maintaining the relative order of genes is critical, such as in routing or scheduling problems.
     *
     * ### Example:
     * ```
     * // Assuming an instance of PartiallyMappedCrossover and a list of two parent chromosomes
     * val offspringChromosomes = pmxCrossover.permuteChromosomes(parentChromosomes)
     * // The result is a list containing two new offspring chromosome gene lists
     * ```
     *
     * @param chromosomes A list containing exactly two parent chromosomes.
     * @return A list containing two lists of genes, each representing an offspring chromosome.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws CollectionConstraintException if the number of chromosomes is not exactly 2.
     */
    override fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>> {
        constraints {
            "There should be exactly 2 chromosomes to perform a PMX crossover" {
                chromosomes must HaveSize(numParents)
            }
        }
        val genes = chromosomes[0].toMutableList() to chromosomes[1].toMutableList()
        val (lo, hi) = Domain.random.indices(2, genes.first.size)
        val crossRegions = genes.first.subList(lo, hi) to genes.second.subList(lo, hi)
        replaceGenesOutsideCrossingRegions(genes, crossRegions, lo, hi)
        return replaceGenesOutsideCrossingRegions(genes, crossRegions, lo, hi).let { listOf(it.first, it.second) }
    }

    /**
     * Adjusts genes outside the specified crossover region in parent chromosomes for a Partially Mapped Crossover
     * (PMX).
     * This function ensures that the resulting offspring chromosomes maintain a valid permutation of genes.
     *
     * ## Process:
     * 1. Takes a pair of parent chromosomes (genes) and the genes within the crossover region (crossRegions).
     * 2. Iterates over all genes outside the crossover region in both parent chromosomes.
     * 3. For each gene outside the crossover region, if it duplicates a gene inside the crossover region of the
     *    other parent, it is replaced. The replacement gene is selected to maintain a valid permutation.
     * 4. Continues this process until no duplicate genes remain outside the crossover regions.
     * 5. Returns a pair of adjusted parent chromosomes, now representing the new offspring chromosomes.
     *
     * ## Usage:
     * This function is called by the `permuteChromosomes` method in the PMX crossover process. It's crucial for
     * ensuring that each gene appears exactly once in each offspring chromosome, thereby maintaining the integrity
     * and validity of the permutation.
     *
     * ### Example:
     * Given two parent chromosomes and a crossover region, this function adjusts the genes outside the crossover
     * region to avoid duplicates and ensure a valid permutation in the offspring.
     *
     * @param genes A pair of mutable lists representing the genes of two parent chromosomes.
     * @param crossRegions A pair of mutable lists representing the genes within the crossover regions of the parents.
     * @param lo The lower bound of the crossover region.
     * @param hi The upper bound (exclusive) of the crossover region.
     * @return A pair of mutable lists representing the genes of the offspring chromosomes.
     */
    private fun replaceGenesOutsideCrossingRegions(
        genes: Pair<MutableList<G>, MutableList<G>>,
        crossRegions: Pair<MutableList<G>, MutableList<G>>,
        lo: Int,
        hi: Int,
    ): Pair<MutableList<G>, MutableList<G>> {
        val (crossSection1, crossSection2) = crossRegions
        val (genes1, genes2) = genes.first.toMutableList() to genes.second.toMutableList()
        // Iterate over all the genes in the parent genes lists
        for (i in 0..<genes1.size) {
            if (i < lo || i >= hi) {
                while (genes1[i] in crossSection2) {
                    val index = genes.second.indexOf(genes1[i])
                    genes1[i] = crossSection1[index]
                }
                while (genes2[i] in crossSection1) {
                    val index = genes.first.indexOf(genes2[i])
                    genes2[i] = crossSection2[index]
                }
            }
        }
        return genes1 to genes2
    }
}
