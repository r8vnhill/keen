/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import cl.ravenhill.keen.genetic.genes.BoolGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list


/**
 * Generates an arbitrary [BoolGene] value, either [BoolGene.True] or [BoolGene.False].
 */
fun Arb.Companion.boolGene() = arbitrary { element(BoolGene.True, BoolGene.False).bind() }

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

