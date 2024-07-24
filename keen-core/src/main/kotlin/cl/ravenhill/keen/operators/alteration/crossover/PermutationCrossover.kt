/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.constraints.BePermutation
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Interface representing a permutation crossover operator for evolutionary algorithms.
 *
 * A permutation crossover operator is used to combine genetic information from parent chromosomes to produce
 * offspring chromosomes. It ensures that the resulting chromosomes are valid permutations, meaning they contain
 * the same elements as the parent chromosomes but in a different order. In a valid permutation, each element must
 * appear exactly once; hence, there are no duplicated elements.
 *
 * ## Constraints:
 * - Each chromosome must be a valid permutation.
 * - All chromosomes must contain the same elements, regardless of order.
 *
 * ## Usage:
 * This interface should be implemented by classes that perform crossover operations on permutations. It is
 * particularly useful in combinatorial optimization problems like the Traveling Salesman Problem (TSP) or any
 * scenario where the solution can be represented as a permutation of elements.
 *
 * ### Example:
 * Implementing a specific permutation crossover:
 * ```
 * class MyPermutationCrossover<T, G : Gene<T, G>> : PermutationCrossover<T, G> {
 *     override fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>> {
 *         // Custom permutation logic
 *     }
 * }
 * ```
 *
 * @param T The type of value that the genes represent.
 * @param G The gene type, must extend [Gene].
 */
interface PermutationCrossover<T, G> : Crossover<T, G> where G : Gene<T, G> {

    /**
     * Performs the crossover operation on the given list of chromosomes.
     *
     * This method ensures that all chromosomes are valid permutations and contain the same elements. If the
     * chromosomes meet these constraints, the crossover operation is performed using the [permuteChromosomes]
     * method.
     *
     * ## Constraints:
     * - Each chromosome must be a valid permutation (no duplicated elements).
     * - All chromosomes must have the same elements in any order.
     *
     * ## Process:
     * 1. Validates that each chromosome is a permutation.
     * 2. Checks that all chromosomes contain the same elements.
     * 3. If a random value exceeds the [chromosomeRate], the original chromosomes are returned.
     * 4. Otherwise, the chromosomes are permuted using [permuteChromosomes] and new chromosomes are created with the
     * permuted genes.
     *
     * @param chromosomes The list of chromosomes to be crossed over.
     * @return A list of new chromosomes created by crossing over the input chromosomes.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws CollectionConstraintException if any chromosome is not a valid permutation
     * @throws ConstraintException if the chromosomes do not contain the same elements
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>> {
        constraints {
            chromosomes.forEachIndexed { index, chromosome ->
                "Chromosome $index is not a permutation" {
                    chromosome must BePermutation
                }
            }
            "All chromosomes must have the same elements in any order" {
                constraint { haveSameElements(chromosomes) }
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
     * Permutes the genes of the given chromosomes.
     *
     * This method should be implemented to define the specific logic for permuting the genes of the input chromosomes.
     * The resulting list of gene lists will be used to create new chromosomes that are valid permutations.
     *
     * @param chromosomes The list of chromosomes to permute.
     * @return A list of gene lists representing the permuted chromosomes.
     */
    fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>): List<List<G>>
}

/**
 * Checks if all provided chromosomes have the same elements.
 *
 * This function compares multiple chromosomes to determine if they all contain the same elements, regardless of the
 * order.
 * It ensures that each list has the same size and that all elements in the first list are present in each subsequent
 * list.
 *
 * @param lists The list of lists to compare.
 * @return `true` if all lists contain the same elements, `false` otherwise.
 */
private fun haveSameElements(lists: List<Chromosome<*, *>>): Boolean {
    val first = lists[0]
    return lists.all { it.size == first.size && it.containsAll(first) }
}
