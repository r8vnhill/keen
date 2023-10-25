/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic

import cl.ravenhill.keen.genetic.Genotype
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.list


fun Arb.Companion.genotype() = choice(intGenotype())

/**
 * Provides an arbitrary generator for a [Genotype] using the [Arb] companion object.
 * The created genotype consists of a single chromosome that is randomly generated using [intChromosome].
 *
 * @return An arbitrary generator for [Genotype].
 * @see Genotype
 */
fun Arb.Companion.intGenotype() = arbitrary {
    Genotype(list(intChromosome()).bind())
}
