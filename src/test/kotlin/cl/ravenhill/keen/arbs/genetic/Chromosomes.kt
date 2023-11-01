/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic

import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.NothingChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*

/**
 * Provides an arbitrary generator for creating random [Chromosome] instances of various types.
 *
 * This extension function facilitates the generation of random [Chromosome] objects
 * by making a choice among different chromosome types, including boolean, character, double, and integer chromosomes.
 *
 * @receiver Arb.Companion The companion object of the arbitrary type, enabling this to be an extension function.
 *
 * @return An [Arb] instance that produces random [Chromosome]s, with the specific type being one of the choices provided.
 */
fun Arb.Companion.chromosome() = choice(
    boolChromosome(),
    charChromosome(),
    doubleChromosome(),
    intChromosome(),
    nothingChromosome()
)

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

fun Arb.Companion.nothingChromosome(length: Arb<Int> = int(0..10)) = arbitrary {
    NothingChromosome(List(length.bind()) { NothingGene })
}