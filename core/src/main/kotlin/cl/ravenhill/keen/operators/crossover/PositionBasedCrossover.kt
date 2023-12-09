/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.indices


/**
 * The `PositionBasedCrossover` (PBX) class implements a specialized crossover mechanism for permutation-based genetic
 * algorithms, focusing on preserving the absolute positions of specific genes from the parent chromosomes in the
 * offspring. This approach is particularly relevant in problems where the position of each element is crucial to
 * finding an optimal solution.
 *
 * ## Theoretical Background:
 * In many permutation problems, certain elements have an optimal or near-optimal position that should be preserved
 * during the crossover phase to maintain solution quality. PBX addresses this by selecting specific gene positions
 * from one parent and ensuring these genes retain their positions in the offspring. The remaining genes are filled
 * in from the other parent, respecting the unique nature of each gene in the permutation.
 *
 * ## Crossover Logic:
 * 1. Validates the presence of exactly two parent chromosomes for the crossover.
 * 2. Selects a set of positions randomly from the first parent chromosome.
 * 3. Constructs offspring chromosomes by combining genes from both parents, prioritizing the preservation of gene
 *    positions as per the selected set from the first parent.
 * 4. Ensures that the offspring are valid permutations without any duplicate genes.
 * 5. Applies the same logic with roles reversed to create a second offspring.
 *
 * ## Constraints:
 * - PBX requires exactly two parent chromosomes to perform the crossover effectively.
 * - The `chromosomeRate` parameter controls the likelihood of the crossover occurring and should be set within the
 *   range `[0.0, 1.0]`.
 *
 * ## Practical Usage:
 * PBX is effective in sequencing and routing problems, such as vehicle routing or job scheduling, where the position
 * of each gene (e.g., a city in a route or a job in a schedule) significantly impacts the solution's quality. By
 * preserving the position of key genes, PBX can maintain high-quality traits in offspring solutions, enhancing the
 * exploration capability of the genetic algorithm.
 *
 * ### Example - Job Scheduling:
 * ```
 * val pbxCrossover = PositionBasedCrossover<Job, JobGene>(chromosomeRate = 0.5, exclusivity = true)
 * // This crossover can be used in a genetic algorithm to solve a job scheduling problem, ensuring optimal or
 * // near-optimal positioning of jobs in the schedule.
 * ```
 *
 * @param T the type of value that the genes represent.
 * @param G the gene type, must extend Gene<T, G>.
 * @param chromosomeRate the probability of crossover between two chromosomes.
 * @param exclusivity a flag indicating whether the crossover is exclusive.
 * @property numOffspring the number of offspring produced by the crossover. Always 2.
 * @property numParents the number of parents required by the crossover. Always 2.
 * @constructor Creates a new instance of PositionBasedCrossover with the specified crossover rate and exclusivity.
 */
class PositionBasedCrossover<T, G>(
    override val chromosomeRate: Double,
    override val exclusivity: Boolean,
) : PermutationCrossover<T, G> where G : Gene<T, G> {

    override val numOffspring = 2

    override val numParents = 2

    /**
     * Executes the permutation crossover operation using the Position Based Crossover (PBX) method for two chromosomes.
     * This method is integral to the PBX crossover, ensuring the correct exchange and mapping of genes based on their
     * positions in the parent chromosomes to create valid offspring permutations.
     *
     * ## Process:
     * 1. Validates that exactly two chromosomes are provided for the PBX crossover.
     * 2. Separates the genes of the two parent chromosomes.
     * 3. Performs the PBX permutation logic on each set of parent genes to create two sets of offspring genes.
     * 4. The permutation logic involves selecting specific positions from one parent and filling the rest from the
     *   other parent, maintaining the uniqueness and order of genes.
     * 5. Returns the resulting pair of offspring gene lists.
     *
     * ## Constraints:
     * - The method requires exactly two chromosomes for the PBX crossover to be effectively performed, aligning with
     *   the typical structure of crossover operations in genetic algorithms.
     *
     * ## Usage:
     * This method is called during the crossover phase in genetic algorithms that use PBX. It's particularly useful for
     * problems where maintaining the absolute positions of certain genes is crucial, such as in complex routing or
     * sequencing problems.
     *
     * ### Example:
     * ```
     * // Assuming an instance of PositionBasedCrossover and a list of two parent chromosomes
     * val offspringChromosomes = positionBasedCrossover.permuteChromosomes(parentChromosomes)
     * // The result is a list containing two new offspring chromosome gene lists
     * ```
     *
     * @param chromosomes A list containing exactly two parent chromosomes to undergo PBX crossover.
     * @return A list containing two lists of genes, each representing an offspring chromosome.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws CollectionConstraintException if the number of chromosomes is not exactly 2.
     */
    override fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>> {
        constraints {
            "PBX requires two parents" {
                chromosomes must HaveSize(2)
            }
        }
        val genes = chromosomes[0].toMutableList() to chromosomes[1].toMutableList()
        val permuted =
            permuteGenes(genes, chromosomes[0].size) to permuteGenes(genes.second to genes.first, chromosomes[0].size)
        return listOf(permuted.first, permuted.second)
    }

    /**
     * Performs gene permutation for a single offspring chromosome as part of the Position Based Crossover (PBX)
     * process. This method selectively combines genes from two parent chromosomes based on specified positions,
     * ensuring the offspring chromosome is a valid permutation.
     *
     * ## Process:
     * 1. Determines a set of positions to be retained from the first parent chromosome based on a probability
     *   calculation.
     * 2. Creates an offspring chromosome by iterating through each gene in the first parent chromosome.
     * 3. If the current gene's position is in the selected set, it is directly copied to the offspring.
     * 4. If not, the method finds the first gene from the second parent chromosome that is not already in the offspring
     *    and adds it to ensure uniqueness and maintain the permutation property.
     * 5. Returns the resulting list of genes representing a single offspring chromosome.
     *
     * ## Usage:
     * This method is used internally by the `permuteChromosomes` method of the `PositionBasedCrossover` class during
     * the crossover operation. It is essential for preserving specific gene positions from the parents while ensuring
     * the offspring's genetic diversity.
     *
     * ### Example:
     * ```
     * // Assuming a pair of parent chromosomes represented as lists of genes
     * val parentGenes = (parentChromosome1.toMutableList(), parentChromosome2.toMutableList())
     * val offspringGenes = permuteGenes(parentGenes, parentChromosome1.size)
     * // offspringGenes now contains a list of genes forming a new offspring chromosome
     * ```
     *
     * @param genes A pair of lists representing the genes of the two parent chromosomes.
     * @param size The size of the parent chromosomes, used to calculate the probability of a gene's position being
     *   selected.
     * @return A mutable list of genes representing a single offspring chromosome.
     */
    private fun permuteGenes(genes: Pair<MutableList<G>, MutableList<G>>, size: Int): MutableList<G> {
        val positions = Domain.random.indices(1 / size.toDouble(), Domain.random.nextInt(size))
        val offspring = mutableListOf<G>()
        genes.first.forEachIndexed { index, gene ->
            if (index in positions) {
                offspring += gene
            } else {
                genes.second.firstOrNull { it !in offspring }?.let {
                    offspring += it
                }
            }
        }
        return offspring
    }
}
