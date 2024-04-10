/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.rngPair
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.FilterMutableListContainer
import cl.ravenhill.keen.mixins.Filterable
import cl.ravenhill.keen.mixins.RangeMutableListContainer
import cl.ravenhill.keen.mixins.Ranged
import io.kotest.common.ExperimentalKotest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import kotlin.random.Random


/**
 * Validates genes of a chromosome by applying a specified validation function on each gene.
 *
 * This function creates a factory for chromosomes using the provided `factoryBuilder` function, then constructs
 * chromosomes of various sizes within the provided size range (default is 1 to 100), and finally applies the [validate]
 * function on each gene of the chromosome.
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
) where T : Comparable<T>,
        G : Gene<T, G>, G : Ranged<T>,
        F : Chromosome.Factory<T, G>, F : RangeMutableListContainer<T> {
    checkAll(generator, Arb.int(1..100)) { range, size ->
        val factory = factoryBuilder()
        factory.ranges += range
        factory.size = size
        factory.make().genes.forEach {
            it.range.start shouldBe range.start
            it.range.endInclusive shouldBe range.endInclusive
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
      F : Chromosome.Factory<T, G>, F : FilterMutableListContainer<T> {
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
@OptIn(ExperimentalKotest::class)
suspend fun <T, G, F> `validate genes with specified range and factory`(
    arb: Arb<ClosedRange<T>>,
    geneFactory: (Random, List<ClosedRange<T>>, Int) -> G,
    factoryBuilder: () -> F,
) where
      T : Comparable<T>,
      G : Gene<T, G>, G : Filterable<T>, G : Ranged<T>,
      F : Chromosome.Factory<T, G>, F : RangeMutableListContainer<T> {
    checkAll(
        PropTestConfig(listeners = listOf(ResetDomainListener)),
        Arb.list(arb, 1..50).map { it.toMutableList() },
        Arb.rngPair()
    ) { ranges, (r1, r2) ->
        Domain.random = r1
        val factory = factoryBuilder()
        factory.ranges = ranges
        factory.size = ranges.size
        val chromosome = factory.make()
        val expected = List(ranges.size) { index ->
            geneFactory(r2, ranges, index)
        }
        chromosome shouldHaveSize ranges.size
        chromosome.genes shouldBe expected
    }
}

suspend fun <T, G> `test chromosome gene consistency`(
    arb: Arb<G>,
    factoryBuilder: (List<G>) -> Chromosome<T, G>,
) where G : Gene<T, G> {
    checkAll(Arb.list(arb, 0..10)) { genes ->
        val chromosome = factoryBuilder(genes)
        chromosome.genes shouldBe genes
    }
}

suspend fun <T, G> `test that a gene can be duplicated with a new set of genes`(
    chromosome: Arb<Chromosome<T, G>>,
    gene: Arb<G>,
) where G : Gene<T, G> {
    checkAll(chromosome, Arb.list(gene, 0..10)) { sourceChromosome, newGenes ->
        val duplicatedChromosome = sourceChromosome.duplicateWithGenes(newGenes)
        duplicatedChromosome.genes shouldBe newGenes
    }
}
