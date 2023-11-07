/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator.strategies

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.DoubleConstraint.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.mutator.ChromosomeMutator
import cl.ravenhill.keen.operators.mutator.GeneMutator
import cl.ravenhill.keen.operators.mutator.MutatorResult
import cl.ravenhill.keen.util.trees.Tree

/**
 * Represents a point mutation operation on a genetic structure. Point mutations involve changing
 * the value of a gene to another value within its domain. In the context of genetic programming,
 * this could
 * mean swapping a node in the expression tree with another node of the same arity.
 *
 * @param V The type of the value held by the nodes in the tree structure of the DNA.
 * @param DNA The type of the DNA, which extends a tree structure.
 * @param G The type of the gene, which extends the Gene class.
 * @property probability The overall probability of a mutation occurring.
 * @property chromosomeRate The probability of a chromosome being selected for mutation.
 * @property geneRate The probability of a gene within a selected chromosome being mutated.
 *
 * @constructor Creates a new [PointMutator] with the given [probability], [chromosomeRate] and [geneRate].
 * @throws CompositeException if the mutation probabilities are not in the range 0.0 to 1.0.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class PointMutator<V, DNA, G>(
    override val probability: Double,
    override val chromosomeRate: Double = 0.5,
    override val geneRate: Double = 0.5
) : ChromosomeMutator<DNA, G>, GeneMutator<DNA, G> where DNA : Tree<V, DNA>, G : Gene<DNA, G> {

    init {
        constraints {
            "Mutation probabilities must be between 0.0 and 1.0" {
                probability must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Mutates the given chromosome by potentially mutating each gene within it.
     *
     * @param chromosome The chromosome to be mutated.
     * @return A [MutatorResult] containing the mutated chromosome and the total number of mutations.
     */
    override fun mutateChromosome(
        chromosome: Chromosome<DNA, G>
    ): MutatorResult<DNA, G, Chromosome<DNA, G>> {
        val genes = chromosome.genes.map { mutateGene(it) }
        return MutatorResult(
            chromosome.withGenes(genes.map { it.mutated }),
            genes.sumOf { it.mutations }
        )
    }

    /**
     * Mutates the given gene with a certain probability. Mutation involves replacing the gene's DNA
     * with another node of the same arity from the available nodes.
     *
     * @param gene The gene to be mutated.
     * @return A [MutatorResult] containing the mutated gene (or the original gene if no mutation occurred).
     */
    override fun mutateGene(gene: G) =
        if (Core.random.nextDouble() <= geneRate) {
            MutatorResult(
                gene.withDna(
                    gene.dna.nodes.filter { it.arity == gene.dna.arity }
                        .random(Core.random)
                )
            )
        } else {
            MutatorResult(gene)
        }
}

