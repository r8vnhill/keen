/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.crossover.Crossover
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next

fun <T, G> Arb.Companion.baseCrossover(chromosomeRate: Arb<Double> = double(0.0, 1.0)): Arb<Crossover<T, G>> where G : Gene<T, G> = arbitrary {
    object : Crossover<T, G> {
        override val numOffspring: Int by lazy { int(1..10).next() }
        override val numParents: Int by lazy { int(1..10).next() }
        override val chromosomeRate: Double by lazy { chromosomeRate.next() }
        override val exclusivity: Boolean by lazy { boolean().next() }

        @Suppress("RedundantOverride")  // This override is needed to avoid a compilation error
        override fun crossover(parentGenotypes: List<Genotype<T, G>>) = super.crossover(parentGenotypes)

        override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>) = chromosomes.take(numOffspring)
    }
}
