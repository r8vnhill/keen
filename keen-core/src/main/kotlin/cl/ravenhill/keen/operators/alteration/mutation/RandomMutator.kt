/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.MutatorConfigurationException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A mutator that randomly applies mutations to chromosomes and genes in an evolutionary algorithm.
 *
 * `RandomMutator` introduces genetic diversity into a population by randomly altering genes and chromosomes based on
 * specified probabilities.
 *
 * ## Constraints:
 * - Both [chromosomeRate] and [geneRate] must be within the range [0.0, 1.0]. These values represent the
 *   likelihood of a chromosome or a gene being selected for mutation.
 *
 * ## Mutation Process:
 * The mutation process operates at two levels:
 * 1. **Chromosome Level**: Determines whether each chromosome in the genotype will be considered for mutation.
 * 2. **Gene Level**: For chromosomes selected for mutation, determines which specific genes will be mutated.
 *
 * A chromosome is only mutated if a random value falls under the [chromosomeRate]. Similarly, within a mutated
 * chromosome, each gene is mutated based on the [geneRate].
 *
 * ## Usage:
 * This mutator can be used in evolutionary algorithms where random mutations are required to explore the search space
 * and maintain genetic diversity.
 *
 * ### Example:
 * ```kotlin
 * val mutator = RandomMutator<MyDataType, MyGene>(
 *     individualRate = 0.1,     // 10% overall mutation probability
 *     chromosomeRate = 0.5,  // 50% chance for each chromosome to be mutated
 *     geneRate = 0.5         // 50% chance for each gene within a chromosome to be mutated
 * )
 * ```
 * In this example, `RandomMutator` is configured with a 10% chance to initiate a mutation process, and within that
 * process, there's a 50% chance for each chromosome and each gene to be mutated.
 *
 * @param T The type of data encapsulated by the genes within the chromosomes.
 * @param G The type of gene in the chromosomes, conforming to the [Gene] interface.
 * @property individualRate The overall probability of a mutation occurring during the mutation process.
 * @property chromosomeRate The probability of a chromosome undergoing mutation.
 * @property geneRate The probability of an individual gene within a chromosome being mutated.
 */
class RandomMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    override val geneRate: Double = DEFAULT_GENE_RATE
) : GeneMutator<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigurationException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigurationException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The gene rate ($geneRate) must be in 0.0..1.0"(::MutatorConfigurationException) {
                geneRate must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Mutates a chromosome by potentially mutating each gene within it based on the specified gene mutation
     * probability.
     *
     * This method creates a new chromosome by duplicating the original chromosome and applying mutations to its genes
     * based on the [geneRate] probability. Each gene in the chromosome is mutated if a random value falls within the
     * [geneRate].
     *
     * ## Mutation Logic:
     * - For each gene in the chromosome, if a random value is within the [geneRate], the gene is mutated by calling
     *   the [mutateGene] method. Otherwise, the gene remains unaltered.
     *
     * ## Usage:
     * This method is typically invoked as part of a larger mutation process within an evolutionary algorithm, where
     * chromosomes of individuals in a population are subject to mutation to introduce genetic diversity.
     *
     * @param chromosome The chromosome to potentially mutate.
     * @return A new chromosome with potentially mutated genes.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>) = chromosome.duplicateWithGenes(chromosome.map {
        if (Domain.random.nextDouble() <= geneRate) {
            mutateGene(it)
        } else {
            it
        }
    })

    /**
     * Mutates a gene.
     *
     * This method directly calls the `mutate` method on the provided gene, creating a new mutated gene instance.
     *
     * ## Mutation Logic:
     * - The method does not consider any mutation probabilities and always mutates the provided gene.
     * - A new gene instance is created by invoking the gene's `mutate` method.
     *
     * ## Usage:
     * This method is a key component in genetic algorithms where mutation is used to introduce variability and
     * explore new genetic combinations. It's invoked as part of the mutation process for individuals in a population.
     *
     * @param gene The gene to be mutated.
     * @return A new mutated gene instance.
     */
    override fun mutateGene(gene: G) = gene.mutate()

    companion object {
        /**
         * The default probability of a mutation occurring during the mutation process for an individual.
         *
         * This value represents the default likelihood that any given individual will be selected for mutation.
         */
        const val DEFAULT_INDIVIDUAL_RATE = 0.5

        /**
         * The default probability of a chromosome undergoing mutation.
         *
         * This value represents the default likelihood that any given chromosome within an individual will be selected
         * for mutation.
         */
        const val DEFAULT_CHROMOSOME_RATE = 0.5

        /**
         * The default probability of an individual gene within a chromosome being mutated.
         *
         * This value represents the default likelihood that any given gene within a chromosome will be selected for
         * mutation.
         */
        const val DEFAULT_GENE_RATE = 0.5
    }
}
