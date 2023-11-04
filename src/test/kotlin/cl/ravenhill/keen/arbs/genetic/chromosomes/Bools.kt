/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.arbs.genetic.boolGene
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list

/**
 * Provides an arbitrary generator for creating `BoolChromosome` instances with random sizes within a specified range.
 *
 * @param sizes An optional range defining the minimum and maximum sizes of the `BoolChromosome`. Defaults to a range of
 *              0 to 100.
 * @return An arbitrary generator for `BoolChromosome`.
 *
 * @see BoolChromosome
 * @see BoolGene
 */
fun Arb.Companion.boolChromosome(sizes: IntRange = 0..100) =
    arbitrary { BoolChromosome(list(boolGene(), sizes).bind()) }

/**
 * Generates a factory that produces [BoolChromosome] instances.
 *
 * @param size An [Arb] that determines the size of the [BoolChromosome].
 * @param trueRate An [Arb] that defines the probability of a gene being true.
 *
 * @return A factory for creating [BoolChromosome] instances.
 */
fun Arb.Companion.boolChromosomeFactory(
    size: Arb<Int> = int(0..10),
    trueRate: Arb<Double> = probability()
) = arbitrary {
    BoolChromosome.Factory().apply {
        this.size = size.bind()
        this.trueRate = trueRate.bind()
    }
}
