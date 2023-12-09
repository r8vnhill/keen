/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.mutation

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Defines the behavior of a gene-level mutator in evolutionary algorithms.
 *
 * The `GeneMutator` interface extends the [Mutator] interface to focus specifically on the mutation of genes within a
 * chromosome. It introduces an additional parameter, `geneRate`, which determines the likelihood of each gene being
 * mutated. Implementations of this interface should define the `mutateGene` method to specify how individual genes are
 * altered during the mutation process.
 *
 * ## Key Concepts:
 * - **Gene-Level Mutation**: While a [Mutator] may operate on chromosomes or individuals, a `GeneMutator` targets
 *   specific genes within a chromosome.
 * - **Mutation Probability**: The `geneRate` property defines the probability of each gene undergoing mutation. This
 *   allows for fine-grained control over the extent of mutation within a population.
 *
 * ## Usage:
 * Implement the `GeneMutator` interface to create custom gene mutation logic, which can be applied as part of the
 * genetic operations in an evolutionary algorithm. The mutation logic can vary widely depending on the nature of the
 * genes and the specific requirements of the problem being solved.
 *
 * ### Example:
 * ```kotlin
 * class MyGeneMutator : GeneMutator<MyDataType, MyGene> {
 *     override val geneRate: Double = 0.1
 *
 *     override fun mutateGene(gene: MyGene): MyGene {
 *         // Implement mutation logic for MyGene
 *     }
 * }
 *
 * // Usage in an evolutionary algorithm
 * val mutator = MyGeneMutator()
 * val mutatedIndividual = mutator(state, outputSize)
 * ```
 * In this example, `MyGeneMutator` implements the `GeneMutator` interface, defining specific mutation logic for
 * `MyGene`. The `mutateGene` method describes how a `MyGene` instance is mutated.
 *
 * @param T The type of data encapsulated by the genes.
 * @param G The type of gene to be mutated, conforming to the [Gene] interface.
 * @property geneRate The probability of mutating each gene in a chromosome. This value should be between 0 and 1,
 *   where 0 means no mutation and 1 means every gene is mutated.
 */
interface GeneMutator<T, G> : Mutator<T, G> where G : Gene<T, G> {
    val geneRate: Double

    /**
     * Executes a mutation operation on a single gene.
     *
     * This method is at the heart of the genetic mutation process in evolutionary algorithms, targeting the micro-level
     * changes at the gene level. It is designed to modify the gene's internal value or structure, thereby introducing
     * genetic variation within the population. The method should encapsulate the logic for altering the gene, which may
     * involve randomization, flipping bits, changing numerical values, or other forms of mutation, depending on the
     * specific implementation and the nature of the gene.
     *
     * The mutation process plays a critical role in the exploration of the genetic search space and helps in preventing
     * premature convergence to local optima by maintaining diversity within the population.
     *
     * ## Key Considerations:
     * - **Immutability**: The method returns a new instance of the gene with the mutation applied, thereby
     *   adhering to the principle of immutability. The original gene instance remains unchanged.
     * - **Controlled Variation**: The extent and nature of the mutation should be carefully balanced to ensure
     *   effective exploration without disrupting the evolutionary progress. Overly aggressive mutations might
     *   lead to loss of valuable genetic information, while too subtle mutations may not provide sufficient
     *   diversity.
     *
     * ## Example Implementation:
     * ```kotlin
     * class DoubleGeneMutator : GeneMutator<Double, DoubleGene> {
     *     override val geneRate: Double = 0.05 // 5% mutation rate
     *
     *     override fun mutateGene(gene: DoubleGene): DoubleGene {
     *         // Simple mutation logic: add or subtract a small value
     *         val mutationAmount = if (Random.nextBoolean()) 1.0 else -1.0
     *         return DoubleGene(gene.value + mutationAmount)
     *     }
     * }
     * ```
     * In this example, `DoubleGeneMutator` performs a simple mutation on `DoubleGene` by randomly adding or
     * subtracting a small value. This demonstrates a basic mutation strategy for a numerical gene type.
     *
     * @param gene The gene to be mutated, representing a single unit of genetic information.
     * @return A new instance of [G], representing the gene post-mutation. This instance should reflect the changes
     *   made during the mutation process.
     */
    fun mutateGene(gene: G): G
}
