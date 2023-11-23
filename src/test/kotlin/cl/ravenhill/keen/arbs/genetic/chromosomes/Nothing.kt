/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.genetic.chromosomes.NothingChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int


/**
 * Generates a `NothingChromosome` using the provided length or a random length within the specified range.
 *
 * @param length The length of the `NothingChromosome`. It defaults to a random value within the range of 0 to 10.
 * @return A new instance of `NothingChromosome`.
 */
fun Arb.Companion.nothingChromosome(length: Arb<Int> = int(0..10)) = arbitrary {
    NothingChromosome(List(length.bind()) { NothingGene })
}


/**
 * Generates a factory that produces [NothingChromosome] instances.
 *
 * @param size An [Arb] that determines the size of the [NothingChromosome].
 *
 * @return A factory for creating [NothingChromosome] instances.
 */
fun Arb.Companion.nothingChromosomeFactory(
    size: Arb<Int> = int(0..10)
) = arbitrary {
    NothingChromosome.Factory().apply {
        this.size = size.bind()
    }
}
