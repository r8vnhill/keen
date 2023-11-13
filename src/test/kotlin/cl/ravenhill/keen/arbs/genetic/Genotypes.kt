/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic

import cl.ravenhill.keen.arbs.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arbs.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.NothingChromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import cl.ravenhill.keen.arbs.datatypes.list
import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.genetic.chromosomes.intChromosomeFactory
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.property.arbitrary.int

/**
 * Generates a genotype.
 *
 * This method is used to generate a genotype by choosing between an integer genotype and
 * a nothing genotype.
 *
 * ## Examples
 * ### Example 1: Using the generator on another generator
 * ```kotlin
 *  fun Arb.Companion.myIndividual() = arbitrary {
 *      Individual(genotype(), double())
 *  }
 * ```
 *
 * ### Example 2: Using the generator on property driven tests
 * ```kotlin
 *  checkAll(genotype(), genotype()) { gt1, gt2 ->
 *      assumme {
 *          gt1 shouldNotBe gt2
 *      }
 *      gt1.chromosomes shouldNotBe gt2.chromosomes
 *  }
 * ```
 *
 *
 * @return The generated genotype.
 */
fun Arb.Companion.genotype() = choice(intGenotype(), nothingGenotype())

/**
 * Provides an arbitrary generator for a [Genotype] using the [Arb] companion object.
 * The created genotype consists of a single chromosome that is randomly generated using [intChromosome].
 *
 * @return An arbitrary generator for [Genotype].
 * @see Genotype
 */
fun Arb.Companion.intGenotype(size: Arb<Int> = int(0..5)) = arbitrary {
    Genotype(list(intChromosome(), size).bind())
}

/**
 * Returns an arbitrary [Genotype] with a list of [NothingChromosome]s.
 *
 * @return an [Arb] genotype with a single nothing chromosome
 */
fun Arb.Companion.nothingGenotype(size: Int? = null) = arbitrary {
    if (size != null) {
        Genotype(List(size) { nothingChromosome().bind() })
    } else {
        Genotype(list(nothingChromosome()).bind())
    }
}

/**
 * Provides an arbitrary generator for creating instances of `Genotype.Factory<Int, IntGene>`.
 * This function is part of Kotest's property-based testing framework and is used to generate
 * random `Genotype.Factory` instances with `Int` as the DNA type and `IntGene` as the gene type.
 *
 * The generated `Genotype.Factory` can be used in tests to create `Genotype` instances with
 * integer genes. It is particularly useful in scenarios where you need to test functions or
 * algorithms that operate on genotypes with integer values.
 *
 * ## Usage Example:
 * This example demonstrates how to use the generator to create a random `Genotype.Factory<Int, IntGene>`:
 * ```kotlin
 * val factoryArb = Arb.intGenotypeFactory()
 * val factory = factoryArb.bind() // Binding the arbitrary to get an instance
 * val genotype = factory.make() // Creating a genotype using the factory
 * ```
 *
 * @return An [Arb] of [Genotype.Factory] with [Int] as the DNA type and [IntGene] as the gene type.
 * @see Genotype.Factory
 * @see IntGene
 */
fun Arb.Companion.intGenotypeFactory() = arbitrary {
    Genotype.Factory<Int, IntGene>().apply {
        repeat(5) {
            chromosomes += intChromosomeFactory().bind()
        }
    }
}
