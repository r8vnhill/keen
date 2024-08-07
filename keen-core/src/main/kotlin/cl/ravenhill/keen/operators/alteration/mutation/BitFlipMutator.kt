/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.ExperimentalJakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator.Companion.DEFAULT_INDIVIDUAL_RATE


/**
 * A mutator for genetic algorithms that performs bit-flipping mutation on Boolean genes.
 *
 * `BitFlipMutator` is specifically designed for genetic algorithms where genes are represented as Boolean values.
 * It flips the value of a gene (true to false or false to true) based on specified probabilities. This mutator
 * introduces variations into the population, which is essential for exploring the solution space and avoiding
 * premature convergence.
 *
 * ## Constraints:
 * - All rates ([individualRate], [chromosomeRate], and [geneRate]) must be within the range [0.0, 1.0].
 *
 * ## Mutation Process:
 * 1. **Individual Mutation Decision**: Determines if an individual is to be mutated based on [individualRate].
 * 2. **Chromosome Selection**: For each individual selected, chromosomes are chosen for mutation based on
 *   [chromosomeRate].
 * 3. **Gene Flipping**: Within the selected chromosomes, each gene is potentially flipped based on [geneRate].
 *
 * ## Usage:
 * This mutator is suited for evolutionary algorithms with binary encoding, often used in optimization problems where
 * solutions can be represented as a series of binary decisions.
 *
 * ### Example:
 * ```kotlin
 * val bitFlipMutator = BitFlipMutator<BooleanGene>(
 *     individualRate = 0.1,    // 10% chance to mutate an individual
 *     chromosomeRate = 0.5,    // 50% chance to mutate a chromosome
 *     geneRate = 0.5           // 50% chance to flip a gene
 * )
 * ```
 * In this example, `BitFlipMutator` is configured with specified mutation probabilities. It can be used in
 * an evolutionary algorithm to mutate individuals, introducing diversity into the population.
 *
 * ## Difference with `RandomMutator`:
 * This mutator is similar to [RandomMutator], but it is specifically designed for Boolean genes, when a gene's value
 * is selected for flipping, it is always flipped to the opposite value. In contrast, [RandomMutator] can be used with
 * any type of gene, and the new value is randomly selected from the gene's domain, meaning that the new value can be
 * the same as the original one.
 *
 * @param G The type of gene, specifically a Boolean gene in this context.
 * @param individualRate The probability of an individual being mutated. This is the first level of selection for
 *   mutation. Defaults to [DEFAULT_INDIVIDUAL_RATE].
 * @param chromosomeRate The probability of choosing a chromosome within an individual for mutation.
 * @param geneRate The probability of flipping a gene's value in a chosen chromosome.
 */
@OptIn(ExperimentalJakt::class)
class BitFlipMutator<G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    override val geneRate: Double = DEFAULT_GENE_RATE
) : GeneMutator<Boolean, G> where G : Gene<Boolean, G> {

    init {
        // Validation constraints for mutation probabilities
        constraints {
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The gene rate ($geneRate) must be in 0.0..1.0"(::MutatorConfigException) {
                geneRate must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Mutates a chromosome by potentially flipping the value of its genes.
     *
     * This method operates on a single chromosome and decides whether to mutate it based on the [chromosomeRate].
     * If the chromosome is selected for mutation, each gene within the chromosome is evaluated for mutation
     * through the [mutateGene] method.
     *
     * ## Mutation Process:
     * 1. **Mutation Decision**: Determines whether the chromosome will undergo mutation based on [chromosomeRate].
     * 2. **Gene Mutation**: If the chromosome is selected, each gene within the chromosome is passed to [mutateGene],
     *    which decides whether to flip the gene's value.
     * 3. **Chromosome Duplication**: A new chromosome is created with the potentially mutated genes. This ensures that
     *    the original chromosome remains unaltered, adhering to the principles of immutability.
     *
     * ## Usage:
     * This method is internally invoked by the `BitFlipMutator` during the mutation phase of a genetic algorithm.
     * It is not typically called directly in user code.
     *
     * @param chromosome The chromosome to potentially mutate. It contains a sequence of Boolean genes.
     * @return A new chromosome instance with potentially mutated genes, or the original chromosome if no mutation
     *   occurs.
     */
    override fun mutateChromosome(chromosome: Chromosome<Boolean, G>) =
        chromosome.duplicateWithGenes(chromosome.genes.map {
            if (Domain.random.nextDouble() < geneRate) {
                mutateGene(it)
            } else {
                it
            }
        })

    /**
     * Mutates a gene by flipping its Boolean value.
     *
     * This method decides whether to mutate a given gene based on the `geneRate`. If the gene is selected for mutation,
     * its value is flipped (i.e., `true` becomes `false` and vice versa). The method ensures immutability by returning
     * a new gene instance with the modified value, leaving the original gene unaltered.
     *
     * ## Mutation Process:
     * 1. **Mutation Decision**: Determines whether the gene will undergo mutation based on `geneRate`.
     * 2. **Value Flipping**: If the gene is selected for mutation, its Boolean value is flipped.
     * 3. **Gene Duplication**: Creates a new gene instance with the flipped value.
     *
     * ## Usage:
     * This method is an integral part of the `BitFlipMutator`'s functionality, being invoked during the mutation phase
     * of an evolutionary algorithm. It is responsible for introducing variation in the population at the gene level.
     *
     * @param gene The gene to potentially mutate. It is a Boolean gene, representing binary genetic information.
     * @return A new gene instance with the flipped value if mutation occurs, or the original gene if no mutation
     *   happens.
     */
    override fun mutateGene(gene: G): G = gene.duplicateWithValue(!gene.value)

    /**
     * Companion object for default mutation rate constants.
     *
     * This companion object defines default rate constants used in mutators in evolutionary computation. These
     * constants represent default probabilities for mutation at different levels of genetic structure: individual,
     * chromosome, and gene. They are used as default values when specific rates are not provided during mutator
     * configuration.
     *
     * @property DEFAULT_INDIVIDUAL_RATE The default mutation rate for individuals. This constant is set at 0.5,
     *   representing a 50% probability that an individual will undergo mutation.
     * @property DEFAULT_CHROMOSOME_RATE The default mutation rate for chromosomes. Set at 0.5, it indicates a 50%
     *   chance that a chromosome within an individual will be mutated.
     * @property DEFAULT_GENE_RATE The default rate for gene-level mutations. Also set at 0.5, it denotes a 50%
     *   likelihood of mutating a specific gene within a chromosome.
     */
    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 0.5
        const val DEFAULT_CHROMOSOME_RATE = 0.5
        const val DEFAULT_GENE_RATE = 0.5
    }
}
