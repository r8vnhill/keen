/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.Filterable
import cl.ravenhill.keen.util.MutableRangedCollection
import cl.ravenhill.keen.util.Ranged
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll

private suspend fun <T, G> validateGenes(
    sizeRange: Arb<Int> = Arb.int(1..100),
    factoryBuilder: () -> Chromosome.Factory<T, G>,
    validate: (G) -> Unit,
) where G : Gene<T, G> {
    checkAll(sizeRange) { size ->
        val factory = factoryBuilder()
        factory.size = size
        factory.make().genes.forEach { gene ->
            validate(gene)
        }
    }
}

/**
 * Validates that each gene in a chromosome passes a specified filter.
 *
 * This function uses property-based testing to validate that every gene produced by
 * the given chromosome factory passes the specified filter. The genes within the chromosome
 * are expected to adhere to the filtering condition provided by the Arb (Arbitrary) generator.
 *
 * The function samples various sizes and values to ensure that the genes within the chromosome
 * respect the filtering condition for the given value.
 *
 * @param T Represents the type of gene value.
 * @param G Represents the gene type, which is filterable (i.e., it can be checked against a filtering condition).
 *
 * @param arb The Arb (Arbitrary) generator for producing values of type T. These values will be used
 *            to test the filtering condition of the genes.
 * @param factoryBuilder A lambda function returning an instance of the chromosome factory. This
 *                       factory will be used to produce chromosomes for validation.
 *
 * @throws AssertionError if any gene does not pass the specified filter.
 */
suspend fun <T, G> `each gene should pass the specified filter`(
    arb: Arb<T>,
    factoryBuilder: () -> Chromosome.Factory<T, G>,
) where T : Comparable<T>, G : Gene<T, G>, G : Filterable<T> {
    validateGenes(factoryBuilder = factoryBuilder) { gene ->
        gene.filter(arb.next()) shouldBe true
    }
}

/**
 * Verifies that each gene within a `CharChromosome` possesses the specified range.
 *
 * @param range The expected range for each gene within the chromosome.
 * @param factoryBuilder A function that returns a [Chromosome.Factory] for creating the chromosome.
 */
suspend fun <T, G> `each gene should have the specified range`(
    range: ClosedRange<T>,
    factoryBuilder: () -> Chromosome.Factory<T, G>,
) where T : Comparable<T>, G : Gene<T, G>, G : Ranged<T> {
    validateGenes(factoryBuilder = factoryBuilder) {
        it.range shouldBe range
    }
}

/**
 * Validates that all genes in a chromosome match a specified single range.
 *
 * Given a range generator and a chromosome factory, this function sets a single range for the
 * chromosome's genes and validates that all the genes adhere to this specified range.
 *
 * The function uses property-based testing to sample various ranges and sizes, ensuring that the
 * genes within the chromosome respect the boundaries of the provided range.
 *
 * @param T Represents the type of gene value, which must be comparable.
 * @param G Represents the gene type, which is ranged (i.e., it has a start and end value).
 * @param F Represents the chromosome factory type, which is capable of producing chromosomes
 *          with genes of type G and can have its range collection modified.
 *
 * @param generator The Arb (Arbitrary) generator for producing a closed range of type T.
 * @param factoryBuilder A lambda function returning an instance of the chromosome factory. This
 *                       factory will be used to produce chromosomes for validation.
 *
 * @throws AssertionError if any gene does not match the specified range.
 */
suspend fun <T, G, F> `validate all genes against single range`(
    generator: Arb<ClosedRange<T>>,
    factoryBuilder: () -> F,
) where T : Comparable<T>, G : Gene<T, G>, G : Ranged<T>, F : Chromosome.Factory<T, G>, F : MutableRangedCollection<T> {
    with(Arb) {
        checkAll(generator, int(1..100)) { range, size ->
            val factory = factoryBuilder()
            factory.ranges += range
            factory.size = size
            factory.make().genes.forEach {
                it.range.start shouldBe range.start
                it.range.endInclusive shouldBe range.endInclusive
            }
        }
    }
}
