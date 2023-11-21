/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.arbs.charRange
import cl.ravenhill.keen.arbs.genetic.charGene
import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list


/**
 * Provides an arbitrary generator for a [CharChromosome] using the [Arb] companion object.
 * The created chromosome will have a size ranging between 0 and 100, and each gene
 * in the chromosome is randomly generated using [charGene] with the entire character range.
 *
 * @return An arbitrary generator for [CharChromosome].
 * @see CharChromosome
 * @see CharGene
 */
fun Arb.Companion.charChromosome() =
    arbitrary { CharChromosome(list(charGene(char()), 0..100).bind()) }

/**
 * Generates a factory that produces [CharChromosome] instances.
 *
 * @param size An [Arb] that determines the size of the [CharChromosome].
 * @param ranges An [Arb] that defines the range of valid characters for the genes.
 *
 * @return A factory for creating [CharChromosome] instances.
 */
fun Arb.Companion.charChromosomeFactory(
    size: Arb<Int> = int(0..10),
    ranges: Arb<MutableList<ClosedRange<Char>>> = mutableList(charRange(' ', '~'))
) = arbitrary {
    CharChromosome.Factory().apply {
        this.size = size.bind()
        this.ranges = ranges.bind()
    }
}
