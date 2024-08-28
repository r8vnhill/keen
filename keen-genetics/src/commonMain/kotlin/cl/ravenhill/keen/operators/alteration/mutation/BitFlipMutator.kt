package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.genetics.genes.BooleanGene

class BitFlipMutator(
    override val individualRate: Double = TODO(),
    override val chromosomeRate: Double = TODO(),
    override val geneRate: Double = TODO()
) : GeneMutator<Boolean, BooleanGene> by ValidateGeneMutatorRates(individualRate, chromosomeRate, geneRate)
