/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.constraints.BePermutation
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Defines an interface for performing permutation crossover in genetic algorithms. This crossover strategy is
 * specifically designed for chromosomes that represent permutations.
 *
 * In genetic algorithms, a permutation crossover is a method of combining two or more parent chromosomes to produce new
 * offspring. This type of crossover ensures that the offspring chromosomes are also valid permutations.
 *
 * ## Crossover Process:
 * 1. Validates that each chromosome in the input list is a permutation chromosome.
 * 2. Determines whether a crossover will occur based on a predefined probability ([chromosomeRate]).
 * 3. If crossover is to take place, performs permutation-specific crossover logic via the [permuteChromosomes] method.
 * 4. Generates offspring chromosomes by duplicating the base chromosome's structure with the new gene sequences.
 *
 * ## Usage:
 * This interface can be implemented to define specific permutation crossover behaviors in genetic algorithms,
 * particularly in problems where the solutions are permutations, such as scheduling or routing problems.
 *
 * ### Example Implementation:
 * ```
 * class MyPermutationCrossover<T, G> : PermutationCrossover<T, G> where G : Gene<T, G> {
 *     override fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>> {
 *         // Implement specific permutation crossover logic here
 *     }
 * }
 * ```
 *
 * @param T the type of value that the genes represent.
 * @param G the gene type, must extend Gene<T, G>.
 */
interface PermutationCrossover<T, G> : Crossover<T, G> where G : Gene<T, G> {

    /**
     * Performs a permutation crossover operation on a list of chromosomes. This function ensures that the resulting
     * chromosomes are valid permutations and maintains the integrity of the genetic information.
     *
     * ## Crossover Logic:
     * 1. Validates each chromosome in the provided list to ensure it represents a valid permutation. This is crucial
     *    as the crossover operation is defined specifically for permutation chromosomes.
     * 2. Determines whether a crossover should occur based on a probability ([chromosomeRate]). If the random value
     *    exceeds this rate, the function returns the original list of chromosomes without any crossover.
     * 3. Performs the permutation crossover by calling the `permuteChromosomes` method, which should be implemented to
     *    define the specific logic of permutation crossover.
     * 4. Creates new chromosomes by duplicating the base chromosome's structure with the permuted gene sequences.
     *
     * ## Constraints:
     * - The function enforces that all chromosomes in the input list are permutation chromosomes. This constraint is
     *   crucial for the crossover process to be meaningful and valid in the context of genetic algorithms focusing on
     *   permutation solutions.
     *
     * ## Usage:
     * This function is typically used in gene-centric evolutionary algorithms where solutions are represented as
     * permutations. It is particularly effective in problems like the Traveling Salesman Problem, scheduling, or any
     * other scenario where the order of elements is critical.
     *
     * ### Example Usage:
     * ```
     * // Assuming a list of permutation chromosomes and an instance of a PermutationCrossover implementation
     * val offspringChromosomes = permutationCrossover.crossoverChromosomes(parentChromosomes)
     * ```
     *
     * @param chromosomes A list of chromosomes to undergo crossover. These chromosomes should represent permutations.
     * @return A list of new Chromosome<T, G> instances resulting from the crossover operation.
     * @throws CompositeException if any chromosome in the list does not satisfy the permutation constraint.
     */
    @Throws(CompositeException::class)
    override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>> {
        constraints {
            for (chromosome in chromosomes) {
                "A Permutation Crossover can only be applied to permutation chromosomes" {
                    chromosome must BePermutation
                }
            }
        }
        if (Domain.random.nextDouble() > chromosomeRate) {
            return chromosomes
        }
        val crossed = permuteChromosomes(chromosomes)
        val base = chromosomes[0]
        return crossed.map { base.duplicateWithGenes(it) }
    }

    /**
     * Defines the specific logic for permuting the genes within a list of chromosomes. This function is central to the
     * permutation crossover process, determining how the genes are rearranged to create new offspring chromosomes.
     *
     * ## Process:
     * 1. Takes a list of chromosomes as input.
     * 2. Applies a permutation logic to the genes within these chromosomes. The specific permutation algorithm is
     *   defined in the implementation of this function.
     * 3. Returns a list of gene lists, where each list represents the permuted genes of a chromosome.
     *
     * ## Usage:
     * This function is called by the [crossoverChromosomes] method in the `PermutationCrossover` interface. It's
     * responsible for the actual rearrangement of genes that constitutes the crossover in genetic algorithms,
     * especially in problems where the solutions are represented as gene permutations (e.g., routing or sequencing
     * problems).
     *
     * Implementing this function requires defining the specific way genes are permuted during the crossover operation.
     * The implementation can vary based on the requirements of the specific problem being solved by the genetic
     * algorithm.
     *
     * ### Example Implementation:
     * ```
     * override fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>> {
     *     // Define the permutation logic for the chromosomes' genes
     * }
     * ```
     *
     * @param chromosomes A list of chromosomes to be permuted. Each chromosome is a collection of genes of type G.
     * @return A list of lists, where each inner list is a permuted sequence of genes representing a new chromosome.
     */
    fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>>
}
