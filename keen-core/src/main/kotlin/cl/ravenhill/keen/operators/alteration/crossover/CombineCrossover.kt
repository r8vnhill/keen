/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A crossover operator that combines genes from multiple parents to create offspring.
 *
 * `CombineCrossover` takes a set of parent chromosomes and combines their genes to produce new chromosomes for the
 * offspring. The combination is done gene by gene, based on a provided combiner function. This operator allows for
 * flexible and customizable crossover strategies, making it suitable for various types of evolutionary algorithms.
 *
 * ## Constraints:
 * - Chromosome and gene rates must be within the range [0.0, 1.0].
 * - The number of parent chromosomes must equal the specified `numParents`.
 * - All parent chromosomes must have the same length.
 *
 * ## Usage:
 * The operator is typically used in the reproduction phase of an evolutionary algorithm to create new
 * individuals by combining the genetic material of parents.
 *
 * ### Example:
 * ```kotlin
 * val combiner: (List<MyGene>) -> MyGene = { genes -> /* combine genes logic */ }
 * val combineCrossover = CombineCrossover(combiner, chromosomeRate = 0.8, geneRate = 0.5)
 *
 * // Apply crossover to produce offspring
 * val offspring = combineCrossover(parentGenotypes)
 * ```
 * In this example, `CombineCrossover` is configured with a custom gene combiner function and specific rates
 * for chromosome and gene selection. The crossover is then applied to a set of parent genotypes to produce
 * offspring genotypes.
 *
 * @param T The type of data encapsulated by the genes.
 * @param G The type of gene, conforming to the [Gene] interface.
 * @param combiner A function that combines a list of genes into a single gene.
 * @param chromosomeRate The probability of a chromosome undergoing crossover, in the range [0.0, 1.0].
 * @param geneRate The probability of an individual gene within a chromosome being recombined, in the range [0.0, 1.0].
 * @param numParents The number of parent chromosomes involved in the crossover.
 * @param exclusivity Flag indicating whether the same individual can be a parent more than once. If `true`,
 *   each parent is unique in the selection.
 */
open class CombineCrossover<T, G>(
    val combiner: (List<G>) -> G,
    override val chromosomeRate: Double = 1.0,
    val geneRate: Double = 1.0,
    override val numParents: Int = 2,
    override val exclusivity: Boolean = false,
) : Crossover<T, G> where G : Gene<T, G> {
    override val numOffspring: Int = 1

    // Initial validation and constraints
    init {
        constraints {
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::CrossoverConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The gene rate ($geneRate) must be in 0.0..1.0"(::CrossoverConfigException) {
                geneRate must BeInRange(0.0..1.0)
            }
            "The number of parents ($numParents) must be greater than 1"(::CrossoverConfigException) {
                numParents must BeAtLeast(2)
            }
        }
    }

    /**
     * Performs a crossover operation on a list of parent chromosomes to produce a single offspring chromosome.
     *
     * This method is a key component of the `CombineCrossover` operation. It takes a list of chromosomes from parent
     * individuals and applies a gene-combining function to produce a new chromosome for the offspring. The crossover
     * is applied gene by gene, using the [combiner] function defined in the [CombineCrossover] class.
     *
     * ## Process:
     * 1. **Combining Genes**: Calls the `combine` function with the list of parent chromosomes to create a new set of
     *   genes.
     * 2. **Chromosome Duplication**: Creates a new chromosome by duplicating the first parent chromosome and
     *   replacing its genes with the newly combined genes.
     *
     * ## Usage:
     * This method is invoked during the crossover phase of the evolutionary process, where genetic material from
     * parents is combined to produce offspring.
     *
     * ### Example:
     * ```kotlin
     * val parentChromosomes = listOf(chromosome1, chromosome2, ...)
     * val offspringChromosome = crossoverChromosomes(parentChromosomes)
     * // offspringChromosome contains genes combined from parent chromosomes
     * ```
     * In this example, `crossoverChromosomes` uses the chromosomes from parent individuals to produce a new
     * chromosome for the offspring. The genes in the offspring chromosome are a combination of genes from the parents.
     *
     * @param chromosomes A list of [Chromosome] instances from parent individuals.
     * @return A list containing a single [Chromosome] for the offspring, created by combining genes from the parent
     *   chromosomes.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>) =
        listOf(chromosomes.first().duplicateWithGenes(combine(chromosomes)))

    /**
     * Combines genes from a list of chromosomes to create a new list of genes, using specified combination rules and
     * probabilities.
     *
     * This function is integral to the `CombineCrossover` operation in evolutionary algorithms, where genes from parent
     * chromosomes are combined to produce offspring. It ensures that all chromosomes have the same length and that the
     * number of chromosomes aligns with the expected number of parents.
     *
     * ## Process:
     * 1. **Validation**:
     *    - Ensures that the number of chromosomes matches the expected number of parents.
     *    - Verifies that all chromosomes have the same length, which is essential for a uniform crossover.
     * 2. **Gene Combination**:
     *    - Iterates over each gene index and uses a `combiner` function to merge genes from each chromosome based on
     *    `geneRate`.
     *    - If `geneRate` conditions are not met, the gene from the first parent chromosome is used as the default.
     *
     * ## Behavior:
     * - **Chromosome Rate is 0**: The function returns a set of genes from the first chromosome, effectively skipping
     * crossover.
     * - **Gene Rate is 1**: Every gene is a result of the `combiner` function, ensuring maximum diversity from
     * available genes.
     *
     * ## Usage:
     * This method is called internally during the crossover process to fuse genes from parental chromosomes into a new
     * set for offspring.
     *
     * ### Example:
     * ```kotlin
     * val parentChromosomes = listOf(chromosome1, chromosome2, ...)
     * val combinedGenes = combine(parentChromosomes)
     * // combinedGenes contains the new set of genes for the offspring
     * ```
     * In this example, `combine` is used to generate a new set of genes from a list of parent chromosomes. The method applies
     * the `combiner` function or inherits directly from the first parent based on the `geneRate`.
     *
     * @param chromosomes A list of parent [Chromosome] instances from which to combine genes.
     * @return A list of [G] representing the combined genes for the offspring's chromosome.
     */
    fun combine(chromosomes: List<Chromosome<T, G>>): List<G> {
        // Validation for the combine operation
        constraints {
            "Number of inputs (${chromosomes.size}) must equal the number of parents ($numParents)"(
                ::CrossoverException
            ) { chromosomes must HaveSize(numParents) }
            "All chromosomes must have the same length"(::CrossoverException) {
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

}
