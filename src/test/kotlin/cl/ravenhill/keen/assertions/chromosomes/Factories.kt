/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.chromosomes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.mutableList
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.Filterable
import cl.ravenhill.keen.util.MutableFilterCollection
import cl.ravenhill.keen.util.MutableRangedCollection
import cl.ravenhill.keen.util.Ranged
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import kotlin.random.Random

/**
 * Validates that the factory correctly retains and reproduces the provided ranges.
 *
 * This function verifies two main aspects:
 * 1. After assigning ranges to the factory, the factory's range count should match the input range count.
 * 2. The actual ranges within the factory should be exactly the same as the input ranges.
 *
 * @param T The gene's type parameter which should be comparable.
 * @param G The type of gene, which has to be both filterable and ranged.
 * @param F The type of factory for creating chromosomes.
 *
 * @param arb An arbitrary generator producing closed ranges of type [T].
 * @param factoryBuilder A lambda function responsible for producing instances of [F].
 *
 * @throws AssertionError If the assigned ranges in the factory do not match the input ranges in both count and content.
 */
suspend fun <T, G, F> `factory should retain assigned ranges`(
    arb: Arb<ClosedRange<T>>,
    factoryBuilder: () -> F,
) where
      T : Comparable<T>,
      G : Gene<T, G>, G : Filterable<T>, G : Ranged<T>,
      F : Chromosome.Factory<T, G>, F : MutableRangedCollection<T> {
    with(Arb) {
        checkAll(list(arb)) { ranges ->
            val factory = factoryBuilder()
            ranges.forEach { factory.ranges += it }
            factory.ranges shouldHaveSize ranges.size
            factory.ranges shouldBe ranges
        }
    }
}

/**
 * Tests whether the factory correctly retains the assigned filters.
 *
 * Given a certain number of filters, this function checks whether the factory
 * correctly stores these filters and whether each filter behaves as expected.
 *
 * @param T The type of the gene's value.
 * @param G The gene type that uses the filter.
 * @param F The type of the chromosome factory that supports mutable filter collection.
 *
 * @param arb An [Arb] instance that generates values of type [T].
 * @param filter A predicate function that tests if a given value of type [T] satisfies certain conditions.
 * @param factoryBuilder A lambda that returns an instance of a chromosome factory.
 *
 * @throws AssertionError if the factory does not retain the filters as expected.
 */
suspend fun <T, G, F> `factory should retain assigned filters`(
    arb: Arb<T>,
    filter: (T) -> Boolean,
    factoryBuilder: () -> F,
) where
      T : Comparable<T>,
      G : Gene<T, G>, G : Filterable<T>,
      F : Chromosome.Factory<T, G>, F : MutableFilterCollection<T> {
    with(Arb) {
        checkAll(int(1..100), arb) { size, x ->
            val factory = factoryBuilder()
            repeat(size) { factory.filters += filter }
            factory.filters shouldHaveSize size
            filter(x).shouldBeTrue()
        }
    }
}

/**
 * Verifies that the factory correctly assigns and reproduces the provided ranges.
 *
 * This function checks:
 * 1. When ranges are assigned to the factory, the size of the factory's ranges matches the size of the provided list.
 * 2. The factory's assigned ranges should be identical to the provided list of ranges.
 *
 * @param T The type parameter of the gene which should be comparable.
 * @param G The gene type that must be filterable and ranged.
 * @param F The factory type for creating chromosomes.
 *
 * @param arb An arbitrary generator producing closed ranges of type [T].
 * @param factoryBuilder A lambda function to produce instances of [F].
 *
 * @throws AssertionError If the factory's ranges do not match the provided list of ranges in terms of both size and content.
 */
suspend fun <T, G, F> `validate factory range assignment`(
    arb: Arb<ClosedRange<T>>,
    factoryBuilder: () -> F,
) where
      T : Comparable<T>,
      G : Gene<T, G>, G : Filterable<T>, G : Ranged<T>,
      F : Chromosome.Factory<T, G>, F : MutableRangedCollection<T> {
    with(Arb) {
        checkAll(mutableList(arb)) { ranges ->
            val factory = factoryBuilder()
            factory.ranges = ranges
            factory.ranges shouldHaveSize ranges.size
            factory.ranges shouldBe ranges
        }
    }
}

/**
 * Validates genes of a chromosome by applying a specified validation function on each gene.
 *
 * This function creates a factory for chromosomes using the provided `factoryBuilder` function,
 * then constructs chromosomes of various sizes within the provided size range (default is 1 to 100),
 * and finally applies the `validate` function on each gene of the chromosome.
 *
 * @param sizeRange The range of chromosome sizes for which to run validations, default is from 1 to 100.
 * @param factoryBuilder A lambda function that returns an instance of `Chromosome.Factory<T, G>`.
 * @param validate A lambda function that contains the validation logic to be applied on each gene.
 *
 * @param T The type of the gene's value.
 * @param G The type of the gene. This gene type should be compatible with `Gene<T, G>`.
 */
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
        it.range.start shouldBe range.start
        it.range.endInclusive shouldBe range.endInclusive
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

/**
 * Validates that every gene produced by the provided factory meets the conditions of a specified filter.
 *
 * This function uses property-based testing to ensure that for a range of randomly generated items of type [T]
 * and varying chromosome sizes, all genes produced by the factory are consistent with the behavior of the
 * specified filter. Specifically, it ensures that if a randomly generated item satisfies the filter, then
 * the filter of each gene should also return `true` for that item, and vice versa.
 *
 * The function makes use of the [Arb] class to generate random values and uses the `checkAll` method to
 * test the property over a combination of sizes and random values.
 *
 * @param T The type of items to be tested against the filter.
 * @param G The type of gene which has filtering capability.
 * @param F The type of factory that produces chromosomes having genes of type [G].
 *
 * @param arb An [Arb] instance used to generate random values of type [T].
 * @param filter A filter function that takes an item of type [T] and returns a `Boolean`
 *               indicating whether the item satisfies a certain condition.
 * @param factoryBuilder A lambda function that returns an instance of [F].
 *
 * @throws AssertionError If any of the property checks fail.
 */
suspend fun <T, G, F> `validate all genes against single filter`(
    arb: Arb<T>,
    filter: (T) -> Boolean,
    factoryBuilder: () -> F,
) where
      T : Comparable<T>,
      G : Gene<T, G>, G : Filterable<T>,
      F : Chromosome.Factory<T, G>, F : MutableFilterCollection<T> {
    with(Arb) {
        checkAll(int(1..100), arb) { size, x ->
            val factory = factoryBuilder()
            factory.filters += filter
            factory.size = size
            factory.make().genes.forEach {
                if (filter(x)) {
                    it.filter(x) shouldBe true
                } else {
                    it.filter(x) shouldBe false
                }
            }
        }
    }
}

/**
 * Validates genes against the specified ranges and ensures they match
 * the expected output from the provided gene factory.
 *
 * @param arb An arb instance that generates a sequence of closed ranges of type `T`.
 * @param geneFactory A function that, given a random seed, a list of ranges, and an index,
 *                    produces a gene of type `G`.
 * @param factoryBuilder A lambda that builds a chromosome factory of type `F`.
 *
 * @param T Represents the comparable type within the closed range.
 * @param G Represents the type of the gene. The gene should be filterable and ranged.
 * @param F Represents the type of the chromosome factory which should support a mutable
 *          collection of ranges.
 */
suspend fun <T, G, F> `validate genes with specified range and factory`(
    arb: Arb<ClosedRange<T>>,
    geneFactory: (Random, List<ClosedRange<T>>, Int) -> G,
    factoryBuilder: () -> F,
) where
      T : Comparable<T>,
      G : Gene<T, G>, G : Filterable<T>, G : Ranged<T>,
      F : Chromosome.Factory<T, G>, F : MutableRangedCollection<T> {
    with(Arb) {
        checkAll(mutableList(arb), long()) { ranges, seed ->
            Core.random = Random(seed)
            val rng = Random(seed)
            val factory = factoryBuilder()
            factory.ranges = ranges
            factory.size = ranges.size
            factory.make().genes.forEachIndexed { index, gene ->
                gene.range shouldBe ranges[index]
                gene shouldBe geneFactory(rng, ranges, index)
            }
        }
    }
}
