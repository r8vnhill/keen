/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.orderedPair
import cl.ravenhill.utils.toRange
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
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
    DoubleChromosome(list(doubleGene(), 0..100).bind())
}

/**
 * Creates an arbitrary gene instance containing a double value from the provided arbitrary double source.
 *
 * @param d An arbitrary source of double values.
 * @return An arbitrary gene instance with a double value.
 */
fun Arb.Companion.doubleGene(d: Arb<Double> = double()) = arbitrary {
    DoubleGene(d.bind())
}

fun Arb.Companion.doubleRange() = arbitrary {
    orderedPair(double()).bind().toRange()
}

/**
 * Provides an arbitrary generator for a [Genotype] using the [Arb] companion object.
 * The created genotype consists of a single chromosome that is randomly generated using [intChromosome].
 *
 * @return An arbitrary generator for [Genotype].
 * @see Genotype
 * @see IntChromosome
 */
fun Arb.Companion.intGenotype() = arbitrary {
    Genotype(list(intChromosome()).bind())
}

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
    IntChromosome(list(intGene(), 0..100).bind())
}

/**
 * Creates an arbitrary generator for an `IntGene` using the provided arbitrary generator for
 * integers.
 *
 * @param i The arbitrary generator for integers which will be bound to the `IntGene`.
 * @return An arbitrary generator that produces instances of `IntGene`.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
fun Arb.Companion.intGene(i: Arb<Int> = int()) = arbitrary {
    IntGene(i.bind())
}
