/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.genetics.genes.BooleanGene
import cl.ravenhill.keen.mixins.Validator

/**
 * A gene-level mutator that performs bit-flip mutations on boolean genes in a genetic algorithm.
 *
 * The `BitFlipMutator` class is a specialized implementation of the [GeneMutator] interface, designed to work with
 * [BooleanGene] types. It flips the value of a boolean gene from `true` to `false` or vice versa, introducing genetic
 * variation in the population. The mutation rates for individuals, chromosomes, and genes can be customized, allowing
 * for flexible control over the mutation process.
 *
 * ## Theoretical Framework:
 * In genetic algorithms, mutation is a key operator used to maintain genetic diversity within the population. The
 * bit-flip mutation is a simple yet effective technique when working with binary or boolean representations. It
 * operates by inverting the value of a gene—turning a `true` into a `false` or a `false` into a `true`. This small,
 * random change can help prevent the algorithm from getting stuck in local optima and encourages exploration of the
 * solution space.
 *
 * The bit-flip mutation is particularly relevant in problems where solutions are encoded as binary strings, such as in
 * genetic algorithms applied to combinatorial optimization, Boolean satisfiability (SAT) problems, and certain machine
 * learning models. The mutation rates ([individualRate], [chromosomeRate], and [geneRate]) play a crucial role in
 * balancing the exploration and exploitation trade-off. Higher mutation rates increase diversity but may disrupt
 * convergence, while lower rates may lead to premature convergence.
 *
 * ## Example Usage:
 * ```kotlin
 * val bitFlipMutator = BitFlipMutator(
 *     individualRate = 0.7,
 *     chromosomeRate = 0.6,
 *     geneRate = 0.1
 * )
 *
 * val initialPopulation: List<Individual<Boolean, BooleanGene>> = // initialize your population
 *
 * val newPopulation = bitFlipMutator(initialPopulation)
 * println("Mutated Population: $newPopulation")
 * ```
 *
 * In this example, the `BitFlipMutator` is configured with specific mutation rates for individuals, chromosomes, and
 * genes. It is then applied to an initial population of individuals, resulting in a new population with mutated boolean
 * genes.
 *
 * ## Recommended Usage:
 * The recommended way to use the `BitFlipMutator` is through its `invoke` operator, which applies the mutation process
 * to a population of individuals. The other methods are public to allow for fine-tuning and customization of mutation
 * strategies, but they should be used directly only in specialized contexts or when developing new algorithms.
 *
 * @param individualRate The probability that an individual in the population will be subject to mutation. Default is
 *   [DEFAULT_INDIVIDUAL_RATE].
 * @param chromosomeRate The probability that a chromosome within an individual will be subject to mutation. Default is
 *   [DEFAULT_CHROMOSOME_RATE].
 * @param geneRate The probability that a gene within a chromosome will be subject to mutation. Default is
 *   [DEFAULT_GENE_RATE].
 */
class BitFlipMutator(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    override val geneRate: Double = DEFAULT_GENE_RATE
) : GeneMutator<Boolean, BooleanGene>, Validator by ValidateGeneMutatorRates(individualRate, chromosomeRate, geneRate) {

    /**
     * Performs a bit-flip mutation on a single boolean gene.
     *
     * This method inverts the value of the given boolean gene—changing `true` to `false` or `false` to `true`. The
     * mutation is applied based on the mutation rates configured for the individual, chromosome, and gene levels.
     *
     * @param gene The boolean gene to be mutated.
     * @return A new boolean gene with its value flipped.
     */
    override fun mutateGene(gene: BooleanGene) = gene.duplicateWithValue(!gene.value)

    companion object {

        /**
         * The default probability that an individual will be subject to mutation. Set to 0.5 (50%).
         */
        const val DEFAULT_INDIVIDUAL_RATE = 0.5

        /**
         * The default probability that a chromosome within an individual will be subject to mutation. Set to 0.5 (50%).
         */
        const val DEFAULT_CHROMOSOME_RATE = 0.5

        /**
         * The default probability that a gene within a chromosome will be subject to mutation. Set to 0.5 (50%).
         */
        const val DEFAULT_GENE_RATE = 0.5
    }
}

