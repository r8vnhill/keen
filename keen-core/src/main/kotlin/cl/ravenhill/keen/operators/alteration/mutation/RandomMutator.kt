/*
 * Copyright (c) 2023, Ignacio Slater M.
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

    override fun mutateChromosome(chromosome: Chromosome<T, G>) = chromosome.duplicateWithGenes(
        chromosome.genes.map { gene ->
            if (Domain.random.nextDouble() < geneRate) mutateGene(gene) else gene
        }
    )

    override fun mutateGene(gene: G) = gene.mutate()

    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 0.5
        const val DEFAULT_CHROMOSOME_RATE = 0.5
        const val DEFAULT_GENE_RATE = 0.5
    }
}
