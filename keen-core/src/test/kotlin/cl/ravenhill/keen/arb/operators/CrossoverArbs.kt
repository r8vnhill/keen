/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.crossover.CombineCrossover
import cl.ravenhill.keen.operators.alteration.crossover.Crossover
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt

/**
 * Generates an arbitrary `Crossover` object for property-based testing in genetic algorithms. This function is an
 * extension of the `Arb` companion object.
 *
 * ## Overview:
 * The function creates a `Crossover` instance with customizable rates, number of parents, and exclusivity settings.
 * These properties are randomly determined based on provided arbitraries. The `Crossover` object defines the basic
 * behavior for crossover operations in genetic algorithms, including generating offspring from parent genotypes.
 *
 * @param chromosomeRate An `Arb<Double>` instance representing the rate at which chromosomes are selected for
 *   crossover. Defaults to a range of 0.0 to 1.0.
 * @param numParents An `Arb<Int>` instance representing the number of parent genotypes required for the crossover.
 *   Defaults to a range of 1 to 10.
 * @param exclusivity An `Arb<Boolean>` instance indicating whether the crossover is exclusive.
 * @return An `Arb<Crossover<T, G>>` instance that generates random `Crossover` objects.
 * @param T The type of the value that the Gene represents.
 * @param G The type of the Gene, constrained to be a subclass of `Gene<T, G>`.
 */
fun <T, G> Arb.Companion.baseCrossover(
    chromosomeRate: Arb<Double> = double(0.0, 1.0),
    numParents: Arb<Int> = int(1..10),
    exclusivity: Arb<Boolean> = boolean(),
): Arb<Crossover<T, G>> where G : Gene<T, G> = arbitrary {
    object : Crossover<T, G> {
        override val numOffspring: Int by lazy { int(1..10).next() }
        override val numParents: Int by lazy { numParents.next() }
        override val chromosomeRate: Double by lazy { chromosomeRate.next() }
        override val exclusivity: Boolean by lazy { exclusivity.next() }

        @Suppress("RedundantOverride")  // This override is needed to avoid a compilation error
        override fun crossover(parentGenotypes: List<Genotype<T, G>>) = super.crossover(parentGenotypes)

        override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>) = chromosomes.take(numOffspring)
    }
}

fun <T, G> Arb.Companion.combineCrossover() where G : Gene<T, G> = arbitrary {
    CombineCrossover<T, G>(
        combiner = { genes -> genes.random() },
        chromosomeRate = probability().bind(),
        geneRate = probability().bind(),
        numParents = positiveInt(10).bind(),
        exclusivity = boolean().bind()
    )
}