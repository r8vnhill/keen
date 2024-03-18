/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.ExperimentalJakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.indices
import cl.ravenhill.keen.utils.swap


/**
 * Represents a mutator that performs swap mutations on chromosomes. This class is generic and can be used with any type
 * of gene.
 *
 * Swap mutations are a form of genetic variation that simulate natural evolutionary processes. They are particularly
 * effective in optimization and search problems where the solution can be represented as a sequence or combination of
 * genes.
 *
 * ## Constraints
 * - The [individualRate], [chromosomeRate], and [swapRate] must be within the range [0.0, 1.0].
 *
 * ## Usage:
 * This mutator is used in evolutionary algorithms to introduce variability in the population by randomly swapping genes
 * within chromosomes. It is particularly useful in problems like the Traveling Salesman Problem (TSP), genetic
 * programming tasks, or any domain where the ordering or combination of elements is crucial.
 *
 * ### Scenario 1: Optimizing Routes in TSP
 * In the TSP, swapping city positions (genes) in a route (chromosome) can lead to discovering shorter paths.
 * ```
 * val swapMutator = SwapMutator<City, RouteGene>(individualRate = 0.2, chromosomeRate = 0.5, swapRate = 0.5)
 * ```
 *
 * ### Scenario 2: Genetic Programming
 * In genetic programming, swapping nodes in an expression tree represented by a list of genes can lead to new,
 * potentially more effective expressions or algorithms.
 * ```
 * val swapMutator = SwapMutator<ExpressionNode, ExpressionGene>(
 *     individualRate = 0.3, chromosomeRate = 0.6, swapRate = 0.4
 * )
 * ```
 *
 * @param T the type of value that the genes represent.
 * @param G the gene type, must extend Gene<T, G>.
 * @param individualRate the mutation rate at the level of individuals.
 * @param chromosomeRate the mutation rate at the level of chromosomes.
 * @param swapRate the rate at which genes are swapped within a chromosome.
 * @constructor Creates a new instance of SwapMutator with specified mutation rates.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws DoubleConstraintException if any of the mutation rates is not within the range [0.0, 1.0].
 */
class SwapMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val swapRate: Double = DEFAULT_SWAP_RATE,
) : Mutator<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "The swap rate ($swapRate) must be in 0.0..1.0"(::MutatorConfigException) {
                swapRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigException) {
                individualRate must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Performs a swap mutation on the given chromosome. This mutation process involves randomly selecting gene indices
     * and swapping their positions, introducing genetic variation.
     *
     * ## Process:
     * 1. Converts the chromosome's genes into a mutable list to facilitate swapping.
     * 2. Generates a set of indices based on the [swapRate] and the total number of genes in the chromosome.
     * 3. For each index in the generated set, a random index within the gene list is chosen, and the two are swapped.
     * 4. Creates a new chromosome instance with the mutated gene list, preserving the original chromosome's structure.
     *
     * ## Usage:
     * This function is called internally by the SwapMutator during the mutation phase of an evolutionary algorithm. It
     * ensures that each chromosome undergoes mutation according to the defined swap mutation logic, thus contributing
     * to the diversity and exploration of the solution space.
     *
     * ## Example:
     * ```
     * // Assuming 'chromosome' is an instance of Chromosome<T, G>
     * val mutatedChromosome = swapMutator.mutateChromosome(chromosome)
     * ```
     *
     * @param chromosome the chromosome to mutate. It is a generic type with gene type G and value type T.
     * @return a new instance of Chromosome<T, G> with genes swapped based on the mutation logic.
     * @receiver the current instance of the mutator.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): ChromosomeMutationResult<T, G> {
        var mutations = 0
        val genes = chromosome.toMutableList()
        val indices = Domain.random.indices(swapRate, genes.size)
        indices.forEach {
            Domain.random.nextInt(genes.size).apply {
                mutations++
                genes.swap(it, this)
            }
        }
        return ChromosomeMutationResult(chromosome.duplicateWithGenes(genes), mutations)
    }

    /**
     * Companion object containing the default values for the mutation rates.
     *
     * @property DEFAULT_INDIVIDUAL_RATE the default mutation rate at the level of individuals.
     * @property DEFAULT_CHROMOSOME_RATE the default mutation rate at the level of chromosomes.
     * @property DEFAULT_SWAP_RATE the default rate at which genes are swapped within a chromosome.
     */
    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 0.5
        const val DEFAULT_CHROMOSOME_RATE = 0.5
        const val DEFAULT_SWAP_RATE = 0.5
    }
}
