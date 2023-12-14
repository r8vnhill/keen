/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.NothingGene

data class NothingChromosome(override val genes: List<NothingGene>) : Chromosome<Nothing, NothingGene> {
    override fun duplicateWithGenes(genes: List<NothingGene>) = copy(genes = genes)
}
