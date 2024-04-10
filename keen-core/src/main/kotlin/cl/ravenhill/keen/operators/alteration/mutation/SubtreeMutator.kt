/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.trees.Tree


@ExperimentalKeen
class SubtreeMutator<V, T, G>(
    override val individualRate: Double,
    override val chromosomeRate: Double,
    override val geneRate: Double,
) : GeneMutator<T, G> where T : Tree<V, T>, G : Gene<T, G> {

    override fun mutateGene(gene: G): G {
        return if (Domain.random.nextDouble() < geneRate) {
            gene.mutate()
        } else {
            gene
        }
    }

    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        TODO("Not yet implemented")
    }
}