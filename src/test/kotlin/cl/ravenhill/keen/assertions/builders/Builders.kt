/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions.builders

import cl.ravenhill.keen.builders.ChromosomeScope
import cl.ravenhill.keen.builders.GenotypeScope
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll


/**
 * Tests the capability to add a chromosome factory to a `GenotypeScope`.
 *
 * This function tests the ability to add multiple chromosomes to a `GenotypeScope`
 * and then verifies that the total number of chromosomes added matches the expected size.
 * It accepts a chromosome builder lambda that describes how to construct the chromosome factory.
 *
 * Usage of the lambda allows for flexibility in specifying various types of chromosomes during testing.
 *
 * The test iterates for a random size between 0 and 100, adding the chromosomes to the `GenotypeScope`
 * and subsequently checks if the size of the chromosomes matches the intended count.
 *
 * @param T The type representing the data of the chromosome.
 * @param G The generic type representing the gene associated with the chromosome.
 *
 * @param chromosomeBuilder A lambda that extends the functionality of `ChromosomeScope<T>`
 *                          to produce a `Chromosome.Factory<T, G>`. This lambda defines
 *                          how the chromosome factory is built.
 *
 * @throws AssertionError if any of the validation checks fail.
 */
suspend fun <T, G> `test adding chromosome factory to GenotypeScope`(
    chromosomeBuilder: ChromosomeScope<T>.() -> Chromosome.Factory<T, G>,
) where G : Gene<T, G> {
    checkAll(Arb.int(0..100)) { size ->
        with(GenotypeScope<T, G>()) {
            repeat(size) {
                chromosome {
                    chromosomeBuilder()
                }
            }
            chromosomes.size shouldBe size
        }
    }
}