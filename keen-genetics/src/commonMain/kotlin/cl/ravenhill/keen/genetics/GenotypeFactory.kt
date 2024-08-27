package cl.ravenhill.keen.genetics

import cl.ravenhill.keen.genetics.chromosomes.ChromosomeFactory
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.RepresentationFactory
import kotlin.random.Random

class GenotypeFactory<T, G> : RepresentationFactory<T, G, Genotype<T, G>> where G : Gene<T, G> {

    val chromosomes: MutableList<ChromosomeFactory<T, G>> = mutableListOf()

    override fun invoke(size: Int, random: Random): Result<Genotype<T, G>> = TODO()
}
