package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.Reduceable


class ProgramChromosome<I> private constructor(genes: List<ProgramGene<I>>) {
    class Factory<T>(primitives: List<Reduceable<Double, Double>>, filter: (Reduceable<*, *>) -> Boolean) :
            Chromosome.Factory<T> {
        override fun make(): Chromosome<T> {
            TODO("Not yet implemented")
        }
    }
}