package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.genetics.genes.Gene

class ValidateGeneMutatorRates<T, G>(
    override val individualRate: Double,
    override val chromosomeRate: Double,
    override val geneRate: Double
) : GeneMutator<T, G> where G : Gene<T, G>
