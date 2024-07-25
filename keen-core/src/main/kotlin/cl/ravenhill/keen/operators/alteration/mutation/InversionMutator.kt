/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.MutatorConfigurationException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A mutator that performs inversion mutations on chromosomes. This class is generic and can be used with any type
 * of gene.
 *
 * Inversion mutations are a form of genetic variation that reverse the order of genes within a chromosome. They are
 * particularly useful in optimization and search problems where the order of elements is significant.
 *
 * ## Constraints:
 * - The [individualRate], [chromosomeRate], and [inversionBoundaryProbability] must be within the range [0.0, 1.0].
 *
 * ## Usage:
 * This mutator is used in evolutionary algorithms to introduce variability in the population by inverting gene
 * sequences within chromosomes. It is particularly useful in problems like the Traveling Salesman Problem (TSP),
 * genetic programming tasks, or any domain where the order of elements is crucial.
 *
 * ### Example:
 * ```
 * val inversionMutator = InversionMutator<City, RouteGene>(
 *     individualRate = 0.2,
 *     chromosomeRate = 0.5,
 *     inversionBoundaryProbability = 0.5
 * )
 * inversionMutator.mutateChromosome(chromosome)
 * ```
 *
 * ## Functionality:
 * The inversion mutation process involves selecting a sub-sequence of genes within a chromosome and reversing their
 * order. The boundaries of this sub-sequence are determined based on the [inversionBoundaryProbability].
 *
 * @param T The type of value that the genes represent.
 * @param G The gene type, must extend [Gene].
 * @property individualRate The mutation rate at the level of individuals.
 * @property chromosomeRate The mutation rate at the level of chromosomes.
 * @property inversionBoundaryProbability The probability of determining the inversion boundaries within a chromosome.
 * @constructor Creates a new instance of InversionMutator with specified mutation rates.
 * @throws CompositeException containing all the constraint violations.
 * @throws MutatorConfigurationException if any of the mutation rates is not within the range [0.0, 1.0] and
 *  [Jakt.shortCircuit] is enabled.
 * @see DEFAULT_INDIVIDUAL_RATE
 * @see DEFAULT_CHROMOSOME_RATE
 * @see DEFAULT_INVERSION_BOUNDARY_PROBABILITY
 */
class InversionMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val inversionBoundaryProbability: Double = DEFAULT_INVERSION_BOUNDARY_PROBABILITY
) : Mutator<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigurationException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigurationException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "Inversion boundary probability ($inversionBoundaryProbability) must be in 0.0..1.0"(
                ::MutatorConfigurationException
            ) {
                inversionBoundaryProbability must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Performs an inversion mutation on the given chromosome.
     *
     * This mutation process involves selecting a sub-sequence of genes within the chromosome and reversing their order.
     * The boundaries of the sub-sequence are determined based on the [inversionBoundaryProbability].
     *
     * ## Process:
     * 1. Retrieves the list of genes from the chromosome.
     * 2. Initializes the start index to 0 and the end index to the last gene in the chromosome.
     * 3. Iterates over the genes to determine the start index for the inversion based on the
     *  [inversionBoundaryProbability].
     * 4. Iterates from the start index to determine the end index for the inversion based on the
     *  [inversionBoundaryProbability].
     * 5. Reverses the order of genes between the start and end indices.
     * 6. Creates a new chromosome instance with the inverted gene sequence.
     *
     * ## Usage:
     * This method is called internally by the `InversionMutator` during the mutation phase of an evolutionary
     * algorithm. It ensures that each chromosome undergoes mutation according to the defined inversion mutation logic,
     * thus contributing to the diversity and exploration of the solution space.
     *
     * @param chromosome The chromosome to mutate. It is a generic type with gene type `G` and value type `T`.
     * @return A new instance of `Chromosome<T, G>` with genes inverted based on the mutation logic.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        val genes = chromosome.genes
        var start = 0
        var end = chromosome.size - 1
        for (i in chromosome.indices) {
            if (Domain.random.nextDouble() < inversionBoundaryProbability) {
                start = i
                break
            }
        }
        for (i in start..<chromosome.size) {
            if (Domain.random.nextDouble() > inversionBoundaryProbability) {
                end = i
                break
            }
        }
        return chromosome.duplicateWithGenes(
            invert(genes, start, end)
        )
    }

    /**
     * Inverts the order of genes between the specified start and end indices.
     *
     * This helper method reverses the order of genes in the provided list between the given start and end indices.
     *
     * @param genes The list of genes to invert.
     * @param start The start index of the inversion.
     * @param end The end index of the inversion.
     * @return A list of genes with the specified sub-sequence inverted.
     */
    private fun invert(genes: List<G>, start: Int, end: Int): List<G> {
        val invertedGenes = genes.toMutableList()

        // Iterate over half the range of genes to swap their positions.
        for (i in start..<(start + (end - start + 1) / 2)) {
            // Calculate the corresponding index to swap with.
            val j = end - (i - start)
            // Swap the positions of the genes at indices i and j.
            val tmp = invertedGenes[i]
            invertedGenes[i] = invertedGenes[j]
            invertedGenes[j] = tmp
        }

        return invertedGenes
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
         * The default probability of determining the inversion boundaries within a chromosome.
         *
         * This value represents the likelihood that any given boundary within a chromosome will be selected for
         * inversion.
         */
        const val DEFAULT_INVERSION_BOUNDARY_PROBABILITY = 0.5
    }
}
