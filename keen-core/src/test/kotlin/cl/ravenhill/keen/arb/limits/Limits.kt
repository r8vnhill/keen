/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.limits

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.MaxGenerations
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.positiveInt

fun <T, G> arbMaxGenerations(generations: Arb<Int> = Arb.positiveInt()) where G : Gene<T, G> = arbitrary {
    MaxGenerations<T, G>(generations.bind())
}
