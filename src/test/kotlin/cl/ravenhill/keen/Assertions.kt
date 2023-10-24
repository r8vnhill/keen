/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.Ranged
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

/**
 * Verifies that each gene within a `CharChromosome` possesses the specified range.
 *
 * @param range The expected range for each gene within the chromosome.
 * @param factory The factory to use for creating the chromosome.
 */
suspend fun <T, G> `each gene should have the specified range`(
    range: ClosedRange<T>,
    factoryBuilder: () -> Chromosome.Factory<T, G>,
) where T : Comparable<T>, G : Gene<T, G>, G : Ranged<T> {
    with(Arb) {
        checkAll(int(1..100)) { size ->
            val factory = factoryBuilder()
            factory.size = size
            factory.make().genes.forEach {
                it.range.start shouldBe range.start
                it.range.endInclusive shouldBe range.endInclusive
            }
        }
    }
}
