package cl.ravenhill.keen.genetics

import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.RepresentationFactory

class GenotypeFactory<T, G> : RepresentationFactory<T, G, Genotype<T, G>> where G : Gene<T, G> {
    override fun make(): Genotype<T, G> {
        TODO("Not yet implemented")
    }
}
