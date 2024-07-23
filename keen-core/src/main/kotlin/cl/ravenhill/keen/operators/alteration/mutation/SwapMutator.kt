/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.keen.Domain
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
 * genes (e.g., the Traveling Salesman Problem).
 *
 * ## Constraints
 * - The [individualRate], [chromosomeRate], and [swapRate] must be within the range [0.0, 1.0].
 *
 * ## Usage:
 * This mutator is used in evolutionary algorithms to introduce variability in the population by randomly swapping genes
 * within chromosomes. It is particularly useful in problems like the Traveling Salesman Problem (TSP), genetic
 * programming tasks, or any domain where the ordering or combination of elements is crucial.
 *
 * ### Example: Optimizing Routes in TSP
 * In the TSP, swapping city positions (genes) in a route (chromosome) can lead to discovering shorter paths.
 * ```
 * val swapMutator = SwapMutator<City, RouteGene>(individualRate = 0.2, chromosomeRate = 0.5, swapRate = 0.5)
 * ```
 *
 * ## Functionality:
 * Suppose we have a chromosome with the following genes: [A, B, C, D, E].
 * - A swap mutation with a rate of 0.5 would randomly select (approximately) half of the genes for swapping. Let's say
 *   the selected genes are [B, D].
 * - The selected genes are then swapped with other randomly selected genes. For example, [B, D] could be swapped with
 *   [C, E], resulting in the chromosome [A, C, B, E, D].
 *
 * @param T the type of value that the genes represent.
 * @param G the gene type, must extend [Gene].
 * @property individualRate the mutation rate at the level of individuals.
 * @property chromosomeRate the mutation rate at the level of chromosomes.
 * @property swapRate the rate at which genes are swapped within a chromosome.
 * @constructor Creates a new instance of SwapMutator with specified mutation rates.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws DoubleConstraintException if any of the mutation rates is not within the range [0.0, 1.0].
 * @see DEFAULT_INDIVIDUAL_RATE
 * @see DEFAULT_CHROMOSOME_RATE
 * @see DEFAULT_SWAP_RATE
 */
class SwapMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val swapRate: Double = DEFAULT_SWAP_RATE
) : Mutator<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "The swap rate [$swapRate] must be in 0.0..1.0" {
                swapRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate [$chromosomeRate] must be in 0.0..1.0" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The individual rate [$individualRate] must be in 0.0..1.0" {
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
     * @param chromosome the chromosome to mutate. It is a generic type with gene type G and value type T.
     * @return a new instance of `Chromosome<T, G>` with genes swapped based on the mutation logic.
     * @receiver the current instance of the mutator.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        val genes = chromosome.toMutableList()
        val indices = Domain.random.indices(swapRate, genes.size)
        indices.forEach {
            Domain.random.nextInt(genes.size).apply { genes.swap(it, this) }
        }
        return chromosome.duplicateWithGenes(genes)
    }

    companion object {
        /**
         * The default probability of a mutation occurring at the individual level during the mutation process.
         *
         * This value represents the likelihood that any given individual will be selected for mutation.
         */
        const val DEFAULT_INDIVIDUAL_RATE = 0.5

        /**
         * The default probability of a chromosome undergoing mutation.
         *
         * This value represents the likelihood that any given chromosome within an individual will be selected for
         * mutation.
         */
        const val DEFAULT_CHROMOSOME_RATE = 0.5

        /**
         * The default probability of genes within a chromosome being swapped.
         *
         * This value represents the likelihood that any given gene within a chromosome will be selected for a swap
         * mutation.
         */
        const val DEFAULT_SWAP_RATE = 0.5
    }
}
