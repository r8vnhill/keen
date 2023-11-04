/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.intRange
import io.kotest.property.arbitrary.list

/**
 * Generates an arbitrary [IntChromosome] instance.
 *
 * This function creates an arbitrary [IntChromosome] populated with genes
 * derived from the [intGene] function within a range of 0 to 100.
 *
 * @receiver The companion object of the [Arb] class, which provides factory methods
 *           for creating arbitrary instances of various types.
 *
 * @return An arbitrary instance of [IntChromosome] with genes in the specified range.
 */
fun Arb.Companion.intChromosome() = arbitrary {
    IntChromosome(list(intGene(), 0..5).bind())
}

/**
 * Generates a factory that produces [IntChromosome] instances.
 *
 * @param size An [Arb] that determines the size of the [IntChromosome].
 * @param ranges An [Arb] that defines the range of valid integer values for the genes.
 *
 * @return A factory for creating [IntChromosome] instances.
 */
fun Arb.Companion.intChromosomeFactory(
    size: Arb<Int> = int(0..10),
    ranges: Arb<MutableList<ClosedRange<Int>>> = mutableList(intRange(Int.MIN_VALUE..Int.MAX_VALUE))
) = arbitrary {
    IntChromosome.Factory().apply {
        this.size = size.bind()
        this.ranges = ranges.bind()
    }
}
