/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.genetic.genes.CharGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.char
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
 * Generates an [Arb] (Arbitrary) of [CharGene] from the given character arbitrary.
 *
 * @param c The arbitrary source of characters to construct the CharGene.
 * @return An arbitrary of CharGene.
 */
fun Arb.Companion.charGene(c: Arb<Char> = char()) = arbitrary {
    CharGene(c.bind())
}

/**
 * Generates an [Arb] (Arbitrary) of [CharRange] between the given bounds `lo` and `hi`.
 *
 * @param lo The lower bound of the character range, defaulting to the smallest possible Char value.
 * @param hi The upper bound of the character range, defaulting to the largest possible Char value.
 * @return An arbitrary of CharRange.
 */
fun Arb.Companion.charRange(lo: Char = Char.MIN_VALUE, hi: Char = Char.MAX_VALUE) =
    arbitrary {
        require(lo < hi)
        lo..hi
    }
