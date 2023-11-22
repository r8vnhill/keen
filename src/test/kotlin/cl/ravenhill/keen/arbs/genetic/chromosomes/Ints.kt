/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.datatypes.orderedPair
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map

/**
 * Generates an arbitrary factory for creating instances of [IntChromosome].
 *
 * This function constructs a [IntChromosome.Factory], which can then be used to produce
 * [IntChromosome] objects with specified characteristics. The size and range of values
 * for each gene in the chromosome are determined by the `sizeRange` parameter.
 *
 * ## Functionality:
 * - The `sizeRange` parameter provides a mechanism to specify both the size of the chromosome
 *   (i.e., the number of genes it contains) and the range of integer values each gene can hold.
 * - The size is determined by the first element of the pair, while the second element provides
 *   a list of integer ranges, with each range corresponding to a gene in the chromosome.
 *
 * ## Example Usage:
 * ```
 * // Creating a factory for IntChromosome with a size of 5 and specific gene value ranges
 * val chromosomeFactoryArb = Arb.intChromosomeFactory(
 *     Arb.constant(5 to mutableList(orderedPair(Arb.int(0..10), strict = true), 5..5))
 * )
 * val chromosomeFactory = chromosomeFactoryArb.bind()
 * // Creating an IntChromosome using the factory
 * val chromosome = chromosomeFactory.make()
 * // The resulting chromosome will have 5 genes, each with values in the specified ranges
 * ```
 *
 * This function is particularly useful for testing scenarios that require chromosomes with
 * integer genes and configurable sizes and value ranges.
 *
 * @param sizeRange An [Arb] that generates pairs of chromosome size and a list of integer value ranges.
 *                  Each pair consists of the size of the chromosome and a corresponding list of ranges
 *                  for the genes. The default range for chromosome size is set from 0 to 10.
 * @return An [Arb] that generates instances of [IntChromosome.Factory] for producing chromosomes
 *         with specified sizes and gene value ranges.
 */
fun Arb.Companion.intChromosomeFactory(
    sizeRange: Arb<Pair<Int, Arb<MutableList<Pair<Int, Int>>>>> = int(0..10).map { size ->
        size to mutableList(orderedPair(int(), strict = true), size..size)
    },
) = arbitrary {
    val (size, ranges) = sizeRange.bind()
    IntChromosome.Factory().apply {
        this.size = size
        this.ranges = ranges.bind().map { (min, max) ->
            min..max
        }.toMutableList()
    }
}
