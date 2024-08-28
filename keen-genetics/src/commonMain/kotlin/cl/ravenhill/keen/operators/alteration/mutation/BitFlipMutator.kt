package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.genetics.genes.BooleanGene

class BitFlipMutator(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    override val geneRate: Double = DEFAULT_GENE_RATE
) : GeneMutator<Boolean, BooleanGene> by ValidateGeneMutatorRates(individualRate, chromosomeRate, geneRate) {

    companion object {

        const val DEFAULT_INDIVIDUAL_RATE = 0.5

        const val DEFAULT_CHROMOSOME_RATE = 0.5

        const val DEFAULT_GENE_RATE = 0.5
    }
}
