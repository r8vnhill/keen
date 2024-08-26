package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.evolution.executors.construction.ConstructorExecutor
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.RepresentationFactory

interface ChromosomeFactory<T, G> : RepresentationFactory<T, G, Chromosome<T, G>> where G : Gene<T, G> {
    var executor: ConstructorExecutor<G>
}
