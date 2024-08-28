package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.genetics.genes.Gene

interface GeneMutator<T, G> : Mutator<T, G> where G : Gene<T, G> {
    val geneRate: Double
}
