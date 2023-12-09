/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.trees.Tree


/**
 * Represents a point mutation operator used in genetic algorithms, specifically for chromosomes with tree-structured
 * genes. The `PointMutator` class is part of the experimental features in the Keen library and allows for the mutation
 * of individual genes within a chromosome based on specified rates.
 *
 * ## Functionality:
 * - Applies point mutations to genes in a chromosome based on a defined gene mutation rate ([geneRate]).
 * - A point mutation involves randomly selecting a node from the gene's tree (with the same arity as the tree's root)
 *   and replacing the tree's root with the selected node.
 * - The mutation rates for genes ([geneRate]), individuals ([individualRate]), and chromosomes ([chromosomeRate])
 *   control the likelihood of mutations occurring at different levels.
 *
 * ## Constraints:
 * - The mutation rates must be between 0.0 and 1.0.
 *
 * ## Usage:
 * This class is used in genetic programming and evolutionary algorithms to introduce genetic variations and
 * diversity. Point mutations are a key mechanism for exploring the genetic landscape and avoiding premature convergence
 * to local optima.
 *
 * ### Example:
 * ```
 * val mutator = PointMutator<MyValueType, MyTreeType, MyGeneType>(
 *     geneRate = 0.1,
 *     individualRate = 0.2,
 *     chromosomeRate = 0.3
 * )
 * val mutatedChromosome = mutator.mutateChromosome(originalChromosome)
 * // mutatedChromosome may contain mutated genes, based on the specified mutation rates
 * ```
 *
 * @param V The type of value held by the tree nodes in the genes.
 * @param T The tree type, extending the `Tree` interface.
 * @param G The gene type, encapsulating a tree structure within the chromosome.
 * @param geneRate The probability of a gene undergoing mutation.
 * @param individualRate The probability of an individual (set of chromosomes) undergoing mutation.
 * @param chromosomeRate The probability of a chromosome undergoing mutation.
 * @return A potentially mutated chromosome, with modifications based on the mutation rates.
 */
@ExperimentalKeen
class PointMutator<V, T, G>(
    override val individualRate: Double,
    override val chromosomeRate: Double,
    override val geneRate: Double,
) : GeneMutator<T, G> where T : Tree<V, T>, G : Gene<T, G> {

    init {
        constraints {
            "Individual mutation rate must be between 0.0 and 1.0" { individualRate must BeInRange(0.0..1.0) }
            "Chromosome mutation rate must be between 0.0 and 1.0" { chromosomeRate must BeInRange(0.0..1.0) }
            "Gene mutation rate must be between 0.0 and 1.0" { geneRate must BeInRange(0.0..1.0) }
        }
    }

    /**
     * Mutates a given gene based on the specified gene mutation rate. This function is a fundamental part of the
     * `PointMutator` class, implementing the gene-level mutation logic in the context of tree-structured genetic
     * programming.
     *
     * ## Functionality:
     * - Determines whether to mutate the gene based on the gene mutation rate ([geneRate]).
     * - If mutation occurs, a new gene is created by duplicating the original gene and replacing its tree value.
     * - The new tree value is selected randomly from the nodes of the original gene's tree that have the same arity
     *   as the tree's root.
     * - If no mutation occurs (based on the [geneRate]), the original gene is returned unchanged.
     *
     * ## Usage:
     * This function is utilized in genetic algorithms to introduce small, random changes in the genes of a chromosome.
     * Point mutations are a key mechanism for maintaining genetic diversity and aiding in the exploration of the
     * solution space.
     *
     * ### Example:
     * ```
     * val mutator = PointMutator<MyValueType, MyTreeType, MyGeneType>(geneRate = 0.1, ...)
     * val originalGene: MyGeneType = ...
     * val mutatedGene = mutator.mutateGene(originalGene)
     * // mutatedGene is either a mutated version of originalGene or the originalGene itself, depending on the mutation
     * // rate
     * ```
     *
     * @param gene The gene to potentially mutate.
     * @return The mutated gene if mutation occurs; otherwise, the original gene.
     */
    override fun mutateGene(gene: G) = if (Domain.random.nextDouble() <= geneRate) {
        gene.duplicateWithValue(gene.value.nodes.filter { it.arity == gene.value.arity }
            .random(Domain.random))
    } else {
        gene
    }

    /**
     * Applies mutation to each gene in a given chromosome, potentially producing a new chromosome with one or more
     * mutated genes. This function is an integral part of the `PointMutator` class and provides the mechanism for
     * chromosome-level mutation in genetic algorithms.
     *
     * ## Functionality:
     * - Iterates through each gene in the provided chromosome.
     * - Applies the [mutateGene] method to each gene, which may result in mutation based on the gene mutation rate
     *   ([geneRate]).
     * - Creates a new chromosome by duplicating the original chromosome with the potentially mutated genes.
     *
     * ## Usage:
     * This function is used in genetic programming to introduce mutations at the chromosome level. It is particularly
     * useful in scenarios where genetic diversity is crucial for the exploration and exploitation of the solution
     * space. The function ensures that mutations are applied in a controlled manner, adhering to the specified
     * mutation rates.
     *
     * ### Example:
     * ```
     * val mutator = PointMutator<MyValueType, MyTreeType, MyGeneType>(geneRate = 0.1, ...)
     * val originalChromosome: Chromosome<MyTreeType, MyGeneType> = ...
     * val mutatedChromosome = mutator.mutateChromosome(originalChromosome)
     * // mutatedChromosome may contain zero or more mutated genes compared to the original chromosome
     * ```
     *
     * @param chromosome The chromosome to be potentially mutated.
     * @return A new chromosome with the same structure as the original, but potentially containing mutated genes.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        val genes = chromosome.map { mutateGene(it) }
        return chromosome.duplicateWithGenes(genes)
    }
}
