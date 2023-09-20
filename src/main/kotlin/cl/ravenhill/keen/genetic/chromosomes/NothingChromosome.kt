/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.NothingGene


/**
 * Represents a chromosome composed exclusively of `NothingGene` elements.
 *
 * The `NothingChromosome` class extends from [AbstractChromosome] and is specifically
 * tailored to handle genes that lack genetic information, namely `NothingGene`.
 * This chromosome serves as a container or placeholder for `NothingGene` genes, particularly useful
 * in contexts where the structure of a chromosome is needed without any associated genetic data.
 *
 * Similar to its gene counterpart (`NothingGene`), any attempt to manipulate or interpret
 * the genetic data of a `NothingChromosome` should be approached with caution, keeping in mind
 * that the genes it contains are devoid of real genetic information.
 *
 * @property genes A list of `NothingGene` elements that form the chromosome.
 */
class NothingChromosome(genes: List<NothingGene>) :
        AbstractChromosome<Nothing, NothingGene>(genes) {

    /**
     * Returns a new `NothingChromosome` instance with the provided genes.
     *
     * @param genes A list of `NothingGene` elements that will compose the new chromosome.
     * @return A new instance of `NothingChromosome` with the specified genes.
     */
    override fun withGenes(genes: List<NothingGene>) = NothingChromosome(genes)
}