/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.genetic.chromosomes.NothingChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int

/**
 * Generates an arbitrary [NothingChromosome.Factory] for property-based testing.
 *
 * This function creates instances of [NothingChromosome.Factory], which can be used to generate
 * [NothingChromosome] objects with a specified size. The size of the chromosome is determined
 * by the provided `size` parameter. This function is particularly useful for testing genetic
 * algorithms where chromosomes of type [NothingChromosome] are required, but their specific
 * genetic content is irrelevant or placeholder.
 *
 * ## Example Usage:
 * ```
 * // Generating a factory for NothingChromosome with a chromosome size between 5 and 10
 * val chromosomeFactory = Arb.nothingChromosomeFactory(Arb.int(5..10)).bind()
 * // Creating a NothingChromosome using the factory
 * val chromosome = chromosomeFactory.make()
 * // The resulting chromosome will have a size between 5 and 10
 * ```
 * @param size An [Arb]<[Int]> that specifies the possible sizes of the chromosomes. The default range
 *   is set from 0 to 10. This parameter controls the number of genes in the generated [NothingChromosome].
 *
 * @return An [Arb]<[NothingChromosome.Factory]> that generates instances of chromosome factories capable
 *         of producing [NothingChromosome] objects with sizes determined by the specified `size` parameter.
 */
fun Arb.Companion.nothingChromosomeFactory(size: Arb<Int> = int(0..10)) = arbitrary {
    NothingChromosome.Factory().apply {
        this.size = size.bind()
    }
}
