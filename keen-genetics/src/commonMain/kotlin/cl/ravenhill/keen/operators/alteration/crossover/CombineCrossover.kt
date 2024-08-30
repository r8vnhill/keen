/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.utils.Exclusivity

/**
 * A crossover operator that combines genes from multiple parents to produce offspring in an evolutionary algorithm.
 *
 * The `CombineCrossover` class is a key genetic operator used in evolutionary algorithms to perform crossover
 * operations. It works by selecting genes from a list of parent individuals and combining them to create new
 * offspring. This mechanism introduces genetic diversity and aids in exploring new solutions within the evolutionary
 * process, which is essential for avoiding local optima and enhancing the algorithm's performance.
 *
 * ## Constraints:
 * The `CombineCrossover` class enforces several constraints to ensure valid and effective crossover operations:
 * - **Gene Rate**: The `geneRate` must be between 0 and 1, representing the probability that a specific gene within a
 *   chromosome will be subject to crossover.
 * - **Chromosome Rate**: The `chromosomeRate` must also be between 0 and 1, representing the probability that an entire
 *   chromosome will be subject to crossover.
 * - **Number of Parents**: The `numParents` must be at least 2, ensuring that there are enough parents to perform the
 *   crossover operation.
 *
 * ## Usage:
 * The `CombineCrossover` class is intended for use in evolutionary algorithms where crossover operations play a crucial
 * role in the genetic evolution process. This class is highly configurable, allowing users to adjust the rates and
 * specify a custom `combiner` function to fit the needs of different evolutionary strategies.
 *
 * ### Example: Using `CombineCrossover`
 * ```kotlin
 * val crossover = CombineCrossover<Int, MyGene>(
 *     combiner = { genes -> genes.random(Domain.random) }, // Example of uniform crossover
 *     chromosomeRate = 0.8,
 *     geneRate = 0.5,
 *     numParents = 3,
 *     exclusivity = Exclusivity.NON_EXCLUSIVE
 * )
 * ```
 *
 * In this example, the `CombineCrossover` operator performs a uniform crossover where genes are randomly selected from
 * the parents. The `chromosomeRate` and `geneRate` are set to control the likelihood of crossover at both the
 * chromosome and gene levels.
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene, which must extend [Gene].
 * @param combiner The function that combines a list of genes from the parent individuals into a single gene for the
 *   offspring.
 * @param chromosomeRate The probability that a chromosome will be subject to crossover.
 * @param geneRate The probability that a specific gene within a chromosome will be subject to crossover.
 * @param numParents The number of parent individuals involved in the crossover.
 * @param exclusivity The exclusivity policy for selecting parents during the crossover.
 * @property numOffspring The number of offspring produced by the crossover operation.
 * @throws CompositeException if the gene rate is not between 0 and 1, the chromosome rate is not between 0 and 1, or
 *   the number of parents is less than 2.
 */
open class CombineCrossover<T, G>(
    val combiner: (List<G>) -> G,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val geneRate: Double = DEFAULT_GENE_RATE,
    override val numParents: Int = DEFAULT_NUM_PARENTS,
    override val exclusivity: Exclusivity = defaultExclusivity
) : Crossover<T, G> where G : Gene<T, G> {

    init {
        constrained {
            "The gene rate must be between 0 and 1" {
                geneRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate must be between 0 and 1" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "Number of parents must be greater or equal to 2" {
                numParents must BeAtLeast(2)
            }
        }.onLeft { throw it }
    }

    /**
     * The number of offspring produced by the crossover operation.
     */
    override val numOffspring: Int = 1

    /**
     * Performs the crossover operation on a list of chromosomes.
     *
     * The `crossoverChromosomes` function takes a list of parent chromosomes and produces a list of offspring
     * chromosomes by combining the genes of the parents. This function leverages the `combine` method to generate new
     * genes for the offspring, ensuring that the offspring inherit genetic material from the parent chromosomes
     * according to the configured crossover rates.
     *
     * ## Important Note:
     * While this function is public to allow for fine-tuning and experimentation in new algorithmic approaches, the
     * recommended way to use the crossover operation is through the [invoke] operator function. Direct use of
     * `crossoverChromosomes` should generally be reserved for advanced scenarios where more granular control over the
     * crossover process is necessary.
     *
     * @param chromosomes A list of parent chromosomes to be crossed over.
     * @return A list containing a single offspring chromosome generated by combining the genes of the parent
     *   chromosomes.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>> =
        listOf(chromosomes.first().duplicateWithGenes(combine(chromosomes)))

    /**
     * Combines the genes from the provided chromosomes according to the configured `geneRate` and the `combiner`
     * function.
     *
     * The `combine` function generates a new list of genes by combining the genes from a list of parent chromosomes.
     * This function applies the `geneRate` to determine whether each gene should be recombined or directly inherited
     * from the first parent chromosome. The `combiner` function is used to define how the genes from the parent
     * chromosomes are merged to create the genes of the offspring.
     *
     * ## Important Note:
     * While this function is public to allow for fine-tuning and experimentation in new algorithmic approaches, the
     * recommended way to use the crossover operation is through the [invoke] operator function. Direct use of
     * `combine` should generally be reserved for advanced scenarios where more granular control over the crossover
     * process is necessary.
     *
     * @param chromosomes A list of chromosomes to be combined.
     * @return A list of genes resulting from the combination.
     * @throws CompositeException if the number of chromosomes does not match the expected number of parents or if the
     *   chromosomes do not have the same length.
     */
    fun combine(chromosomes: List<Chromosome<T, G>>): List<G> {
        constrained {
            "Number of inputs (${chromosomes.size}) must equal the number of parents ($numParents)" {
                chromosomes must HaveSize(numParents)
            }
            "All chromosomes must have the same length" {
                chromosomes.map { it.size }.toSet() must HaveSize(1)
            }
        }
        // Combining logic for genes
        return List(chromosomes[0].size) { i ->
            if (Domain.random.nextDouble() < geneRate) {
                combiner(chromosomes.map { it[i] })
            } else {
                chromosomes[0][i]
            }
        }
    }

    companion object {

        /**
         * The default rate at which chromosomes are combined during the crossover process. Set to 0.5 by default.
         */
        const val DEFAULT_CHROMOSOME_RATE = 0.5


        /**
         * Default rate for gene crossover operations in the `CombineCrossover` class. Set to 0.5 by default.
         */
        const val DEFAULT_GENE_RATE = 0.5

        /**
         * The default number of parent chromosomes to be used in the crossover operation. Set to 2 by default.
         */
        const val DEFAULT_NUM_PARENTS = 2

        /**
         * Default exclusivity setting for crossover operations in the `CombineCrossover` class. Set to
         * [Exclusivity.NON_EXCLUSIVE] by default.
         */
        val defaultExclusivity = Exclusivity.NON_EXCLUSIVE
    }
}
