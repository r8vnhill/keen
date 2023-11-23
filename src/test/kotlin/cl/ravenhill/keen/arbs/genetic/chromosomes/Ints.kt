/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.datatypes.orderedPair
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next

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

fun Arb.Companion.intChromosome(size: Arb<Int>) = arbitrary {
    size.bind().let {
        IntChromosome(list(intGene(), it..it + 1).bind())
    }
}

/**
 * Generates an arbitrary factory for creating instances of [IntChromosome].
 *
 * This function provides a way to generate [IntChromosome.Factory] instances with varied configurations,
 * allowing for flexibility in testing and simulation scenarios that involve integer chromosomes.
 * It utilizes Kotest's property-based testing framework for generating random sizes and range pairs for
 * the chromosomes.
 *
 * The factory produced by this function is capable of creating [IntChromosome] instances with a specified
 * size and a set of ranges. These ranges define the permissible values for the genes within the chromosome.
 *
 * ## Usage Scenario:
 * In a genetic algorithm simulation, the `intGenotypeFactory` function utilizes `intChromosomeFactory`
 * to create a [Genotype.Factory] with multiple chromosomes, each having different configurations.
 *
 * ### Example:
 * ```kotlin
 * fun Arb.Companion.intGenotypeFactory() = arbitrary {
 *     Genotype.Factory<Int, IntGene>().apply {
 *         repeat(5) {
 *             chromosomes += intChromosomeFactory().bind()
 *         }
 *     }
 * }
 * ```
 * In this example, `intGenotypeFactory` calls `intChromosomeFactory` five times to create a genotype
 * with diverse chromosome configurations, each potentially having different sizes and gene value ranges.
 *
 * @param sizeRange An [Arb] of [Pair]<[Int], [MutableList]<[Pair]<[Int], [Int]>>> that generates
 *                  arbitrary sizes and corresponding range pairs for the chromosomes. Each pair represents
 *                  the size of a chromosome and its value ranges.
 *
 * @return An [Arb] that produces instances of [IntChromosome.Factory], each capable of creating
 *         [IntChromosome] instances with specified sizes and gene value ranges.
 * @see compose
 */
fun Arb.Companion.intChromosomeFactory(
    sizeRange: Arb<Pair<Int, MutableList<Pair<Int, Int>>>> = int(0..10).map {
        it to mutableList(
            Arb.orderedPair(
                int(),
                strict = true
            ), it..<it + 1
        ).next()
    }
) = arbitrary {
    val (size, ranges) = sizeRange.bind()
    IntChromosome.Factory().apply {
        this.size = size
        this.ranges = ranges.let { ranges ->
            ranges.map { (min, max) ->
                min..max
            }
        }.toMutableList()
    }
}
