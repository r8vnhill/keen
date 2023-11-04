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
fun Arb.Companion.intGenotype() = arbitrary {
    Genotype(list(intChromosome(), int(0..5)).bind())
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
