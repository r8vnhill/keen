/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.arbs.doubleRange
import cl.ravenhill.keen.arbs.genetic.doubleGene
import cl.ravenhill.keen.arbs.mutableList
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list

/**
 * Generates an arbitrary [DoubleChromosome] instance.
 *
 * This function creates an arbitrary [DoubleChromosome] populated with genes
 * derived from the [doubleGene] function within a range of 0 to 100.
 *
 * @receiver The companion object of the [Arb] class, which provides factory methods
 *           for creating arbitrary instances of various types.
 *
 * @return An arbitrary instance of [DoubleChromosome] with genes in the specified range.
 */
fun Arb.Companion.doubleChromosome() = arbitrary {
    DoubleChromosome(list(doubleGene(), 0..5).bind())
}

/**
 * Generates a factory that produces [DoubleChromosome] instances.
 *
 * @param size An [Arb] that determines the size of the [DoubleChromosome].
 * @param ranges An [Arb] that defines the range of valid double values for the genes.
 *
 * @return A factory for creating [DoubleChromosome] instances.
 */
fun Arb.Companion.doubleChromosomeFactory(
    size: Arb<Int> = int(0..10),
    ranges: Arb<MutableList<ClosedRange<Double>>> = mutableList(doubleRange())
) = arbitrary {
    DoubleChromosome.Factory().apply {
        this.size = size.bind()
        this.ranges = ranges.bind()
    }
}
