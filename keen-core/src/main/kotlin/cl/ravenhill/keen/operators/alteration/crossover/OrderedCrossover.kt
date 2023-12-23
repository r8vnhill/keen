/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtMost
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.indices


/**
 * The `OrderedCrossover` class implements a specific type of crossover mechanism for genetic algorithms, particularly
 * suited for problems where chromosomes represent permutations and the order of genes is crucial. This class provides
 * a method to combine genes from parent chromosomes into offspring while preserving the relative order of genes,
 * essential in problems like the Traveling Salesman Problem (TSP) or scheduling tasks.
 *
 * ## Theoretical Background:
 * In permutation-based genetic algorithms, maintaining the order of genes is often critical to ensuring valid
 * solutions. The ordered crossover technique addresses this by selecting a subset of genes from one parent and filling
 * the rest of the chromosome with genes from the other parent in the order they appear. This approach ensures that the
 * offspring inherits characteristics from both parents while maintaining a valid permutation.
 *
 * ## Crossover Logic:
 * 1. Selects a contiguous segment of genes (crossover region) from one parent.
 * 2. Inserts this segment into the corresponding position in the other parent, ensuring no gene duplications.
 * 3. Repeats the process in reverse to create a second offspring.
 *
 * ### Example:
 * Let's say we have two parent chromosomes: ``P1 = [1, 2, 3, 4, 5, 6, 7, 8, 9]`` and
 * ``P2 = [5, 7, 4, 9, 1, 3, 6, 2, 8]``.
 *
 * 1. Selects a crossover region from ``P1``: ``[3, 4, 5, 6]``.
 * 2. Inserts the crossover region into ``P2``: ``[_, _, 3, 4, 5, 6, _, _, _]``.
 * 3. Fills the remaining positions with genes from ``P2``: ``[7, 9, 1, 2, 8]``.
 * 4. Offspring 1: ``[7, 9, 3, 4, 5, 6, 1, 2, 8]``.
 *
 * ## Constraints:
 * - [chromosomeRate] defines the likelihood of crossover occurring and must be within `[0.0, 1.0]`.
 * - The crossover region is randomly determined and must be valid within the size of the parent chromosomes.
 *
 * ## Practical Usage:
 * Ordered crossover is valuable in optimization problems where the solution is a sequence or route, and the relative
 * position of elements is significant. By maintaining the sequence order, it preserves specific properties or
 * constraints inherent in the problem being solved.
 *
 * ### Example - TSP:
 * In the TSP, cities (genes) must appear only once in a route (chromosome). Ordered crossover allows combining parts
 * of two parent routes while ensuring each city appears exactly once in the offspring route.
 *
 * ```
 * val orderedCrossover = OrderedCrossover<Double, MyGene<Double>>(chromosomeRate = 0.5, exclusivity = true)
 * // This crossover can then be used in a genetic algorithm for the TSP.
 * ```
 *
 * @param T the type of value that the genes represent.
 * @param G the gene type, must extend Gene<T, G>.
 * @param chromosomeRate the probability of crossover between two chromosomes.
 * @param exclusivity a flag indicating whether the crossover is exclusive.
 * @constructor Creates a new instance of OrderedCrossover with the specified crossover rate and exclusivity.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws DoubleConstraintException if `chromosomeRate` is not within the specified range.
 */
class OrderedCrossover<T, G>(
    override val chromosomeRate: Double,
    override val exclusivity: Boolean = false
) : PermutationCrossover<T, G> where G : Gene<T, G> {
    override val numOffspring = 2
    override val numParents = 2

    init {
        constraints {
            "The chromosome crossover probability should be in 0..1" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Implements the permutation logic for chromosomes during the crossover process. This method is responsible for
     * creating new offspring chromosomes by exchanging gene segments between parent chromosomes.
     *
     * ## Crossover Logic:
     * 1. Determines the size of the chromosomes and selects a random contiguous segment (defined by start and end
     *   indices) for crossover.
     * 2. Extracts genes from the provided parent chromosomes.
     * 3. Performs the exchange of the selected gene segments between the two parent chromosomes to create two new
     *   offspring chromosomes.
     *
     * ## Process:
     * - Randomly selects start and end indices within the chromosome's length.
     * - For each pair of parent chromosomes, it exchanges the genes within the selected indices range.
     * - The genes outside this range remain in their original order, ensuring the offspring chromosomes are valid
     *   permutations.
     *
     * ## Usage:
     * This method is central to the ordered crossover mechanism in genetic algorithms, especially when solving problems
     * where the solution is a sequence or route, and maintaining the order of elements is essential.
     *
     * ### Example:
     * ```
     * // Assuming an instance of a class that implements PermutationCrossover and has a list of parent chromosomes
     * val offspringGeneLists = permutationCrossover.permuteChromosomes(parentChromosomes)
     * // The result is a list of gene lists, each representing an offspring chromosome
     * ```
     *
     * @param chromosomes A list of parent chromosomes to be permuted.
     * @return A list of gene lists, where each list represents an offspring chromosome resulting from the crossover.
     */
    override fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>> {
        val size = chromosomes.first().size
        val (start, end) = Domain.random.indices(2, size).sorted()
        val (genes1, genes2) = chromosomes.map { it.genes }
        return listOf(
            exchangeCrossingRegions(genes1 to genes2, start..end),
            exchangeCrossingRegions(genes2 to genes1, start..end)
        )
    }

    /**
     * Facilitates the exchange of gene segments between two parent chromosomes within specified indices to create an
     * offspring chromosome. This function is essential in ordered crossover operations where maintaining the sequence
     * order of genes is critical.
     *
     * ## Process:
     * 1. Validates the specified indices for the crossover region to ensure they are within the bounds of the parent
     *   chromosomes.
     * 2. Extracts a sublist of genes from the first parent within the defined crossover region.
     * 3. Filters genes from the second parent to exclude those present in the extracted sublist.
     * 4. Constructs the offspring chromosome by combining the filtered genes from the second parent with the extracted
     *   sublist from the first parent, preserving the order of genes.
     *
     * ## Constraints:
     * - The start of the crossover region must be non-negative.
     * - The end of the crossover region must not exceed the size of the parent chromosomes.
     * - Ensures the validity of the crossover region to avoid negative indices and out-of-bounds errors.
     *
     * ## Usage:
     * This function is typically used in genetic algorithms where preserving the order of genes is crucial, such as in
     * routing or sequencing problems. It ensures that offspring chromosomes are valid permutations and maintain
     * characteristics from both parents.
     *
     * ### Example:
     * ```
     * val parents = Pair(listOf(gene1, gene2, gene3), listOf(gene4, gene5, gene6))
     * val offspringGenes = exchangeCrossingRegions(parents, 1..2)
     * // Results in a new list of genes for an offspring chromosome
     * ```
     *
     * @param parents A pair of lists representing the genes of the two parent chromosomes.
     * @param indices The range of indices defining the crossover region.
     * @return A list of genes representing an offspring chromosome.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws IntConstraintException if the indices are not valid.
     */
    fun exchangeCrossingRegions(parents: Pair<List<G>, List<G>>, indices: ClosedRange<Int>): List<G> {
        constraints {
            "The start of the crossover region must be non-negative" { indices.start mustNot BeNegative }
            "The end of the crossover region must be less than the size of the parents" {
                indices.endInclusive must BeAtMost(parents.first.size - 1)
            }
        }
        if (indices.start == indices.endInclusive) return parents.second
        // Takes a sublist of genes from the first parent to be inserted into the second parent.
        val sublist = parents.first.subList(indices.start, indices.endInclusive + 1)
        // Creates a new list to hold the genes from the second parent that are not in the sublist.
        val uniqueGenes = parents.second.filter { it !in sublist }
        // Creates the new offspring list by combining genes from the second parent and the sublist of the first parent.
        return uniqueGenes.take(indices.start) + sublist + uniqueGenes.drop(indices.start)
    }
}
