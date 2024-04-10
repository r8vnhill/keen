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
import cl.ravenhill.keen.utils.eq

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
 *     probability = 0.1,     // 10% overall mutation probability
 *     chromosomeRate = 0.5,  // 50% chance for each chromosome to be mutated
 *     geneRate = 0.5         // 50% chance for each gene within a chromosome to be mutated
 * )
 * ```
 * In this example, `RandomMutator` is configured with a 10% chance to initiate a mutation process, and within that
 * process, there's a 50% chance for each chromosome and each gene to be mutated.
 *
 * @param T The type of data encapsulated by the genes within the chromosomes.
 * @param G The type of gene in the chromosomes, conforming to the [Gene] interface.
 * @param individualRate The overall probability of a mutation occurring during the mutation process.
 * @param chromosomeRate The probability of a chromosome undergoing mutation.
 * @param geneRate The probability of an individual gene within a chromosome being mutated.
 */
@OptIn(ExperimentalJakt::class)
class RandomMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    override val geneRate: Double = DEFAULT_GENE_RATE
) : GeneMutator<T, G> where G : Gene<T, G> {

    init {
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
     * Mutates a chromosome based on the specified mutation probability.
     *
     * This method determines whether a given chromosome undergoes mutation. The mutation process is controlled by the
     * [chromosomeRate] probability. If the chromosome is selected for mutation (based on the [chromosomeRate]), each
     * gene within the chromosome is passed to the [mutateGene] method for potential mutation.
     *
     * ## Mutation Logic:
     * - If [chromosomeRate] is 0.0, no mutation occurs, and the original chromosome is returned.
     * - If a random value exceeds [chromosomeRate], the chromosome remains unaltered.
     * - If a random value is within the [chromosomeRate], the chromosome undergoes mutation. This involves creating a
     *   new chromosome with each gene potentially mutated by [mutateGene].
     *
     * ## Usage:
     * This method is typically invoked as part of a larger mutation process within an evolutionary algorithm, where
     * chromosomes of individuals in a population are subject to mutation to introduce genetic diversity.
     *
     * @param chromosome The chromosome to potentially mutate.
     * @return The original chromosome if no mutation occurs, or a new chromosome with mutated genes if mutation occurs.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>) = when {
        chromosomeRate eq 0.0 -> chromosome
        Domain.random.nextDouble() > chromosomeRate -> chromosome
        else -> chromosome.duplicateWithGenes(chromosome.map { mutateGene(it) })
    }

    /**
     * Mutates a gene based on the specified gene mutation probability.
     *
     * This method determines whether a given gene undergoes mutation. The mutation process is governed by the
     * [geneRate] probability. If the gene is selected for mutation (as determined by the [geneRate]), a new mutated
     * gene is created using the gene's own `mutate` method.
     *
     * ## Mutation Logic:
     * - If [geneRate] is 0.0, no mutation occurs, and the original gene is returned.
     * - If a random value exceeds [geneRate], the gene remains unaltered.
     * - If a random value falls within [geneRate], the gene undergoes mutation. This involves creating a new gene
     *   instance with the mutation applied, ensuring that the original gene's state is not altered.
     *
     * ## Usage:
     * This method is a key component in genetic algorithms where mutation is used to introduce variability and
     * explore new genetic combinations. It's invoked as part of the mutation process for individuals in a population.
     *
     * @param gene The gene to potentially mutate.
     * @return The original gene if no mutation occurs, or a new mutated gene instance if mutation occurs.
     */
    override fun mutateGene(gene: G) = when {
        geneRate eq 0.0 -> gene
        Domain.random.nextDouble() > geneRate -> gene
        else -> gene.mutate()
    }

    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 0.5
        const val DEFAULT_CHROMOSOME_RATE = 0.5
        const val DEFAULT_GENE_RATE = 0.5
    }
}
