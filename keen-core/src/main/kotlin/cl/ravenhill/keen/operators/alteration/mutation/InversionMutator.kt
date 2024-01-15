/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.ExperimentalJakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.IntConstraint
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.swap
import cl.ravenhill.jakt.constraints.ints.BeInRange as IntBeInRange

/**
 * Represents an inversion mutator in genetic algorithms. This mutator selectively inverts sections of chromosomes
 * based on specified probabilities.
 *
 * The inversion mutation is a genetic operator that reverses the order of a segment of genes within a chromosome. It
 * plays a critical role in evolutionary algorithms by introducing diversity in the population, thereby aiding in the
 * exploration of the solution space. The `InversionMutator` class is generic, allowing it to work with any gene type.
 *
 * ## How it works:
 * The mutator selects a chromosome for mutation based on the [chromosomeRate]. If selected, it then determines the
 * start and end indices of the segment to be inverted within the chromosome. These indices are chosen based on the
 * [inversionBoundaryProbability], which defines the likelihood of each gene being the boundary of the inversion
 * segment. The segment between these indices is then inverted, creating a new genetic sequence within the chromosome.
 *
 * ## Constraints:
 * - The [individualRate], [chromosomeRate], and [inversionBoundaryProbability] must all be within the range
 *   `[0.0, 1.0]`.
 *
 * ## Usage:
 * This mutator can be particularly useful in problems where the relative ordering of genes is significant, such as in
 * routing or sequencing problems. It introduces significant structural changes, thereby helping to avoid local optima.
 *
 * ### Example:
 * ```
 * val inversionMutator = InversionMutator<MyGeneType, MyChromosomeType>(0.1, 0.5, 0.3)
 * // Use this mutator in the genetic algorithm to mutate chromosomes
 * ```
 *
 * @param T the type of value that the genes represent.
 * @param G the gene type, must extend Gene<T, G>.
 * @param individualRate the mutation rate at the level of individuals.
 * @param chromosomeRate the mutation rate at the level of chromosomes.
 * @param inversionBoundaryProbability the probability of a gene being the boundary for inversion.
 * @constructor Creates a new instance of InversionMutator with the specified rates.
 * @throws CompositeException containing all the exceptions thrown by the constraints.
 * @throws DoubleConstraintException if any of the rates are not within the specified range.
 */
@OptIn(ExperimentalJakt::class)
data class InversionMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val inversionBoundaryProbability: Double = DEFAULT_INVERSION_BOUNDARY_PROBABILITY,
) : Mutator<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The inversion boundary probability ($inversionBoundaryProbability) must be in 0.0..1.0"(
                ::MutatorConfigException
            ) {
                inversionBoundaryProbability must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Performs mutation on a given chromosome by potentially inverting a segment of its genes. The inversion is
     * contingent upon specified mutation rates and probabilities.
     *
     * ## Process:
     * 1. Determines whether to mutate the chromosome based on the [chromosomeRate]. If the randomly generated number
     *   exceeds this rate, the original chromosome is returned without mutation.
     * 2. Identifies the start and end indices for the inversion segment. These indices are selected based on the
     *   [inversionBoundaryProbability], influencing how likely a gene is to be the start or end of the inversion
     *   segment.
     * 3. Inverts the gene sequence within these indices, creating a new genetic configuration.
     * 4. Constructs a new chromosome with the modified gene sequence, leaving the original chromosome unchanged.
     *
     * ## Usage:
     * This function is a core component of the `InversionMutator` behavior in genetic algorithms, providing a
     * mechanism for introducing significant genetic diversity. It is particularly effective in scenarios where the
     * solution space benefits from large structural changes to escape local optima.
     *
     * ## Example:
     * ```
     * // Assuming 'chromosome' is an instance of Chromosome<T, G>
     * val mutatedChromosome = inversionMutator.mutateChromosome(chromosome)
     * ```
     *
     * @param chromosome The chromosome to potentially mutate. It is a generic type with gene type G and value type T.
     * @return A new instance of `Chromosome<T, G>` with a potentially inverted gene sequence.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): ChromosomeMutationResult<T, G> {
        val genes = chromosome.genes
        val (start, end) = getInversionBoundary(chromosome.indices)
        return ChromosomeMutationResult(
            subject = chromosome.duplicateWithGenes(invert(genes, start, end)),
            mutations = end - start + 1 // Number of genes subject to inversion
        )
    }

    internal fun getInversionBoundary(indices: IntRange): Pair<Int, Int> {
        var start = 0
        var end = 0
        for (i in indices) {
            if (Domain.random.nextDouble() < inversionBoundaryProbability) {
                start = i
                break
            }
        }
        for (i in start..<indices.last) {
            if (Domain.random.nextDouble() > inversionBoundaryProbability) {
                end = i
                break
            }
        }
        return start to end
    }

    /**
     * Inverts a specified segment of genes within a chromosome.
     *
     * ## Overview
     * This function takes a list of genes and inverts the order of genes within a specified segment. The segment is
     * defined by the `start` and `end` indices. This inversion is a crucial operation in genetic algorithms for
     * introducing variability and helping the algorithm to explore different genetic configurations.
     *
     * ## Process
     * - The function iterates over half of the specified segment range.
     * - For each position in this range, it calculates the corresponding index from the opposite end of the segment.
     * - It then swaps the genes at these two indices.
     * - This process effectively reverses the order of genes within the specified segment.
     *
     * @param genes The list of genes to be inverted.
     * @param start The starting index of the segment to invert.
     * @param end The ending index of the segment to invert.
     * @return A list of genes with the specified segment inverted.
     */
    internal fun invert(genes: List<G>, start: Int, end: Int): List<G> {
        constraints {
            "The start index ($start) must be in 0..${genes.size - 1}"(::MutatorConfigException) {
                start must IntBeInRange(genes.indices)
            }
            "The end index ($end) must be in 0..${genes.size - 1}"(::MutatorConfigException) {
                end must IntBeInRange(genes.indices)
            }
        }
        return genes.subList(0, start) + genes.subList(start, end + 1).reversed() + genes.subList(end + 1, genes.size)
    }

    /**
     * Companion object for the [InversionMutator] class.
     *
     * ## Overview
     * This companion object defines default values for the mutation rates and inversion boundary probability used in
     * the ``InversionMutator`` class. These defaults provide a baseline for typical usage scenarios and can be
     * overridden when creating instances of ``InversionMutator``.
     *
     * ## Usage:
     * These constants are used as default parameters for the ``InversionMutator`` class. They are particularly useful
     * when specific mutation rates and probabilities are not provided during the instantiation of an
     * ``InversionMutator`` object.
     *
     * ## Significance
     * Providing default rates and probabilities is essential for simplifying the usage of the ``InversionMutator``
     * class. It allows users to quickly instantiate an ``InversionMutator`` with sensible defaults, making the class
     * more accessible, especially for those new to evolutionary algorithms or when quick setup is desired.
     * Additionally, having standardized default rates ensures consistency across different uses and applications of the
     * ``InversionMutator``.
     *
     * @property DEFAULT_INDIVIDUAL_RATE the default mutation rate at the individual level. This rate determines how
     *   frequently individual entities in the population are considered for mutation.
     * @property DEFAULT_CHROMOSOME_RATE the default mutation rate at the chromosome level. This rate influences the
     *   likelihood of a chromosome being selected for inversion mutation.
     * @property DEFAULT_INVERSION_BOUNDARY_PROBABILITY the default probability for determining the boundaries of an
     *   inversion segment within a chromosome. This rate affects how likely each gene is to be the start or end of the
     *   inversion segment.
     */
    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 0.5
        const val DEFAULT_CHROMOSOME_RATE = 0.5
        const val DEFAULT_INVERSION_BOUNDARY_PROBABILITY = 0.5
    }
}
