package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.Crossover


/**
 * Interface defining the behavior for performing permutation crossover in genetic algorithms.
 *
 * Permutation crossover is a specific type of crossover operation used in genetic algorithms, particularly
 * suitable for problems where the solution can be represented as a sequence or a permutation. This method
 * is often used in scheduling, routing, or other ordering problems where the order of elements (genes)
 * is crucial to the solution's quality.
 *
 * The crossover operation combines parts of two or more parent chromosomes to produce new offspring. In
 * permutation crossover, this process needs to maintain a valid permutation - typically ensuring no
 * duplicate elements in the offspring and preserving the order-related characteristics of the parents.
 *
 * Implementing this interface requires defining the `performPermutationCrossover` method. This method
 * takes a list of parent chromosomes and produces a list of new chromosomes (offspring) by combining the
 * genetic material of the parents in a way that respects the permutation nature of the problem.
 *
 * ## Examples
 * ### Example 1: Basic Permutation Crossover
 * ```
 * class MyPermutationCrossover : PermutationCrossover<Int, IntGene> {
 *     override fun performPermutationCrossover(chromosomes: List<Chromosome<Int, IntGene>>): List<List<IntGene>> {
 *         // Implementation of crossover logic specific to the problem
 *     }
 * }
 * ```
 *
 * ### Example 2: Using PermutationCrossover in a Genetic Algorithm
 * ```
 * // Assuming a genetic algorithm setup for a routing problem
 * val crossoverOperator = MyPermutationCrossover()
 * //... setup for genetic algorithm
 * ```
 *
 * @param DNA The type of data that represents an individual's genotype.
 * @param G The specific type of [Gene] that encapsulates the [DNA] type data.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface PermutationCrossover<DNA, G> : Crossover<DNA, G> where G : Gene<DNA, G> {

    /**
     * Performs a permutation crossover operation on a given list of parent chromosomes.
     *
     * The method is responsible for combining the genetic material of the parents to create offspring
     * while maintaining a valid permutation. It must ensure that each offspring is a valid solution,
     * typically by avoiding duplicate elements and maintaining a meaningful sequence order.
     *
     * @param chromosomes The list of parent chromosomes to undergo crossover.
     * @return A list containing lists of [G] genes, each representing an offspring chromosome.
     */
    fun performPermutationCrossover(chromosomes: List<Chromosome<DNA, G>>): List<List<G>>

    /**
     * Performs crossover operation on a list of chromosomes.
     *
     * @param chromosomes the list of chromosomes to be crossed over
     * @return the list of resulting chromosomes after crossover operation
     */
    fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>>
}
