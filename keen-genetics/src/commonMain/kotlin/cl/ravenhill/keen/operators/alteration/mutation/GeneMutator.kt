/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene

/**
 * Interface for implementing gene-level mutation in a genetic algorithm.
 *
 * The `GeneMutator` interface extends the [Mutator] interface and provides functionality for mutating individual genes
 * within a chromosome. This interface is designed to be used in genetic algorithms where mutation operations occur
 * at the gene level, allowing for fine-grained control over the genetic diversity introduced during the evolutionary
 * process.
 *
 * ## Usage:
 * The `GeneMutator` interface is intended to be implemented by classes that require gene-level mutations within a
 * genetic algorithm. The interface provides a standardized way to define how individual genes are mutated and how those
 * mutations propagate through the chromosomes of the population.
 *
 * ### Example: Implementing a Custom Gene Mutator
 * ```kotlin
 * class MyGeneMutator<T>(
 *     override val geneRate: Double
 * ) : GeneMutator<T, MyGene> {
 *
 *     override fun mutateGene(gene: MyGene): MyGene {
 *         // Define the logic for mutating a single gene
 *         return gene.mutate()
 *     }
 * }
 * ```
 *
 * In this example, the `MyGeneMutator` class implements the `GeneMutator` interface, providing a custom mutation
 * strategy for genes of type `MyGene`. The `mutateGene` method is overridden to specify how each gene should be
 * mutated.
 *
 * ## Recommended Usage:
 * The recommended way to use the `GeneMutator` is through its [invoke] operator, which applies the mutation to a
 * population of individuals. The [mutateChromosome] and [mutateGene] methods are public to allow fine-tuning and
 * customization of mutation strategies, but they should be used directly only in specialized contexts or when
 * developing new algorithms.
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene that is mutated, which must extend [Gene].
 * @property geneRate The probability that any given gene within a chromosome will be mutated.
 */
interface GeneMutator<T, G> : Mutator<T, G> where G : Gene<T, G> {

    val geneRate: Double

    /**
     * Mutates the genes within a chromosome based on the gene mutation rate.
     *
     * This method iterates over each gene in the chromosome and applies the mutation logic defined by [mutateGene] if
     * the random probability is less than the `geneRate`. The method returns a new chromosome with the potentially
     * mutated genes, preserving the structure and integrity of the original chromosome.
     *
     * @param chromosome The chromosome whose genes are to be mutated.
     * @return A new chromosome with mutated genes.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> =
        chromosome.copyWithGenes(chromosome.genes.map { gene ->
            if (Domain.random.nextDouble() < geneRate) {
                mutateGene(gene)
            } else {
                gene
            }
        })

    /**
     * Defines the mutation logic for an individual gene.
     *
     * Implementations of this interface must provide the specific logic for mutating a gene, determining how the gene's
     * value or state is altered during the mutation process.
     *
     * @param gene The gene to be mutated.
     * @return The mutated gene.
     */
    fun mutateGene(gene: G): G
}
