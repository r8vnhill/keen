/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.chromosomes.chromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosomeFactory
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

/**
 * Tests the creation of `Genotype` instances to ensure they are correctly initialized with chromosomes.
 *
 * This test suite focuses on verifying that `Genotype` objects are properly constructed with the intended
 * genetic material, represented as a collection of chromosomes. Two primary methods of constructing genotypes
 * are tested: using a list of chromosomes and using varargs of chromosomes. These tests ensure that the
 * genotypes accurately reflect the chromosomes they are initialized with, which is fundamental for the
 * integrity of genetic algorithms.
 *
 * ## Test Cases:
 * - **Genotype Creation with a List of Chromosomes**: Validates that when a genotype is created with a list
 *   of chromosomes, it contains the exact same chromosomes, preserving their order and properties.
 * - **Genotype Creation with Varargs of Chromosomes**: Ensures that when a genotype is constructed using
 *   varargs (variable arguments) of chromosomes, it accurately reflects these chromosomes in the correct order
 *   and with the correct properties.
 *
 * These tests are crucial for confirming that the `Genotype` class behaves as expected when initialized with
 * different sets of chromosomes, thus guaranteeing the reliability of genetic representations in algorithms.
 */
fun `test Genotype creation`() = freeSpec {
    "When creating a Genotype" - {
        "with a list of chromosomes then the genotype should have the same chromosomes" {
            checkAll(Arb.list(Arb.chromosome())) { chromosomes ->
                Genotype(chromosomes).chromosomes shouldBe chromosomes
            }
        }

        "with chromosomes as varargs then the genotype should have the same chromosomes" {
            checkAll(Arb.list(Arb.chromosome())) { chromosomes ->
                Genotype(*chromosomes.toTypedArray()).chromosomes shouldBe chromosomes
            }
        }
    }
}

/**
 * Tests the verification process of `Genotype` instances in a genetic algorithm.
 *
 * This test suite assesses the `verify` method of `Genotype`, ensuring that it correctly evaluates
 * the validity of the genotype based on the verification status of its constituent chromosomes.
 * The verification process is fundamental in genetic algorithms for maintaining the integrity and
 * validity of genetic structures.
 *
 * ## Test Scenarios:
 * - **Verification with an Empty Chromosome List**: Confirms that a genotype with no chromosomes
 *   is automatically considered valid. This scenario is important for handling edge cases where
 *   genotypes may not contain any genetic material.
 *
 * - **Verification with All Valid Chromosomes**: Checks that a genotype is considered valid when
 *   all its chromosomes pass their verification tests. This scenario ensures that the genotype
 *   accurately reflects the validity of its constituent parts.
 *
 * - **Verification with At Least One Invalid Chromosome**: Validates that the genotype is considered
 *   invalid if any of its chromosomes fails the verification test. This test is crucial for ensuring
 *   that the genotype does not falsely represent its genetic integrity.
 *
 * The tests use property-based testing to cover various configurations and states of genotypes, thereby
 * ensuring the robustness and reliability of the genotype's verification mechanism in different contexts.
 *
 * ## Usage Example in Test Suite:
 * ```kotlin
 * class GenotypeTest : FreeSpec({
 *     include(`test Genotype verification`())
 *     //... other tests
 * })
 * ```
 * In this example, the test suite includes the tests defined in this function, which are executed
 * as part of the overall testing of genotype functionalities.
 */
fun `test Genotype verification`() = freeSpec {
    "When verifying a Genotype it" - {
        "should return true if" - {
            "the list of chromosomes is empty" {
                Genotype<Int, DummyGene>().verify().shouldBeTrue()
            }

            "all chromosomes are valid" {
                checkAll(Arb.genotype(isValid = Arb.constant(true))) { genotype ->
                    genotype.verify().shouldBeTrue()
                }
            }
        }

        "should return false if" - {
            "any chromosome is invalid" {
                checkAll(Arb.genotype()) { genotype ->
                    assume {
                        genotype.chromosomes.isNotEmpty()
                        genotype.chromosomes.any { !it.verify() }.shouldBeTrue()
                    }
                    genotype.verify().shouldBeFalse()
                }
            }
        }
    }
}

/**
 * Contains a suite of tests to verify the behavior of the `Genotype` class in different scenarios.
 *
 * These tests cover various aspects of `Genotype` functionality, including creation, verification,
 * flat-mapping, and accessing chromosomes by index. They ensure that `Genotype` behaves as expected
 * under various conditions, which is crucial for the reliability and correctness of genetic algorithms.
 *
 * ## Test Scenarios:
 * - **Size Property**: Verifies that the `size` property of a genotype correctly reflects the number of
 *   chromosomes it contains.
 * - **Verification Logic**: Tests the `verify` method to ensure it accurately reflects the validity of
 *   the chromosomes within the genotype.
 * - **Flat-Mapping**: Checks the behavior of the `flatMap` method, verifying that it correctly
 *   transforms and flattens the genetic material.
 * - **Index Access**: Confirms that chromosomes can be correctly accessed by index and that appropriate
 *   exceptions are thrown for invalid indices.
 */
fun `test Genotype behaviour`() = freeSpec {
    "A Genotype" - {
        "should have a size property that" - {
            "is equal to the number of chromosomes" {
                checkAll(
                    Arb.genotype(Arb.chromosome(size = Arb.int(0..25)))
                ) { genotype ->
                    genotype.size shouldBe genotype.chromosomes.size
                }
            }
        }

        "when accessing a chromosome by index" - {
            "should return the chromosome at the given index" {
                checkAll(Arb.genotype()) { genotype ->
                    genotype.forEachIndexed { index, chromosome ->
                        genotype[index] shouldBe chromosome
                    }
                }
            }

            "should throw an exception if the index is out of bounds" {
                checkAll(Arb.genotype(Arb.intChromosome()).map {
                    it to Arb.int().filter { index -> index !in 0..it.size }.next()
                }) { (genotype, index) ->
                    shouldThrow<CompositeException> {
                        genotype[index]
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The index [$index] must be in the range [0, ${genotype.size})"
                    )
                }
            }
        }
    }
}

fun `test Genotype Factory behaviour`() = freeSpec {
    "A Genotype Factory" - {
        "should have a list of chromosomes that" - {
            "is empty by default" {
                Genotype.Factory<Int, DummyGene>().chromosomes.isEmpty().shouldBeTrue()
            }

            "can be modified" {
                checkAll(Arb.list(Arb.doubleChromosomeFactory(), 0..25)) { factories ->
                    val factory = Genotype.Factory<Double, DoubleGene>()
                    factory.chromosomes += factories
                    factory.chromosomes shouldBe factories
                }
            }
        }

        "should be able to create a Genotype with the chromosomes added to the factory" {
            checkAll(Arb.list(Arb.doubleChromosomeFactory(), 0..25), Arb.long().map {
                Random(it) to Random(it)
            }) { factories, (rng1, rng2) ->
                val factory = Genotype.Factory<Double, DoubleGene>().apply {
                    chromosomes += factories
                }
                Domain.random = rng1
                val genotype = factory.make()
                Domain.random = rng2
                genotype shouldBe Genotype(factories.map { it.make() })
            }
            Domain.random = Random.Default
        }
    }
}
