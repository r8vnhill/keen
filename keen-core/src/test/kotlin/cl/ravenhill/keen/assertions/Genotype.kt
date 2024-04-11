/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.arbRngPair
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.arbInvalidGenotype
import cl.ravenhill.keen.arb.genetic.arbValidGenotype
import cl.ravenhill.keen.arb.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosomeFactory
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.*
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.*
import io.kotest.property.assume
import io.kotest.property.checkAll

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
            checkAll(Arb.list(arbChromosome())) { chromosomes ->
                Genotype(chromosomes).chromosomes shouldBe chromosomes
            }
        }

        "with chromosomes as varargs then the genotype should have the same chromosomes" {
            checkAll(Arb.list(arbChromosome())) { chromosomes ->
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
                checkAll(arbValidGenotype()) { genotype ->
                    genotype.verify().shouldBeTrue()
                }
            }
        }

        "should return false if" - {
            "any chromosome is invalid" {
                checkAll(arbInvalidGenotype()) { genotype ->
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
fun `test Genotype behavior`() = freeSpec {
    "A Genotype" - {
        "should have a size property that" - {
            "is equal to the number of chromosomes" {
                checkAll(arbGenotype(arbChromosome(size = Arb.int(0..25)))) { genotype ->
                    genotype shouldHaveSize genotype.chromosomes.size
                }
            }
        }

        "when accessing a chromosome by index" - {
            "should return the chromosome at the given index" {
                checkAll(genotype()) { genotype ->
                    genotype.forEachIndexed { index, chromosome ->
                        genotype[index] shouldBe chromosome
                    }
                }
            }

            "should throw an exception if the index is out of bounds" {
                checkAll(genotypeAndInvalidIndex()) { (genotype, index) ->
                    shouldThrow<CompositeException> {
                        genotype[index]
                    }.shouldHaveInfringement<InvalidIndexException>(
                        "The index [$index] must be in the range [0, ${genotype.size})"
                    )
                }
            }
        }
    }
}

/**
 * Executes a suite of tests on the `Genotype.Factory` class to ensure its correct functionality within Keen.
 *
 * ## Test Scenarios:
 * - **Empty Chromosome List**: A `Genotype.Factory` should start with no chromosomes, ensuring no unintended genetic
 * information is present.
 * - **Modifiable Chromosome List**: The factory must allow adding new chromosome factories, enabling customization of
 * the genetic structure.
 * - **Correct Genotype Creation**: Upon invoking `make()`, the factory should produce a `Genotype` with all added
 * chromosomes, reflecting accurate genetic assembly.
 */
@OptIn(ExperimentalKotest::class)
fun `test Genotype Factory behaviour`() = freeSpec {
    "A Genotype Factory" - {
        "should have a list of chromosomes that" - {
            "is empty by default" {
                Genotype.Factory<Int, DummyGene>().chromosomes.isEmpty().shouldBeTrue()
            }

            "can be modified" {
                checkAll(chromosomeFactories()) { factories ->
                    val factory = Genotype.Factory<Double, DoubleGene>()
                    factory.chromosomes += factories
                    factory.chromosomes shouldBe factories
                }
            }
        }

        "should be able to create a Genotype with the chromosomes added to the factory" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetDomainListener)),
                chromosomeFactories(),
                arbRngPair()
            ) { factories, (rng1, rng2) ->
                val factory = Genotype.Factory<Double, DoubleGene>().apply {
                    chromosomes += factories
                }
                Domain.random = rng1
                val genotype = factory.make()
                Domain.random = rng2
                genotype shouldBe Genotype(factories.map { it.make() })
            }
        }
    }
}

/**
 * Executes tests to verify the `isEmpty` method of the `Genotype` class, ensuring it accurately reflects the presence
 * or absence of genes.
 *
 * ## Test Scenarios:
 * - **Empty Genotype**: Confirms that a `Genotype` initialized with an empty list of chromosomes is properly considered
 * empty.
 * - **Non-Empty Genotype**: Asserts that a `Genotype` containing one or more chromosomes is not deemed empty,
 * reflecting the presence of genetic data.
 */
fun `test Genotype emptiness`() = freeSpec {
    "When testing if a Genotype is empty it" - {
        "should be empty if the genes list is empty" {
            Genotype<Nothing, NothingGene>(emptyList()).isEmpty().shouldBeTrue()
        }

        "should not be empty if the genes list is not empty" {
            checkAll(Arb.list(arbNothingChromosome(), 1..10)) { chromosomes ->
                Genotype(chromosomes).isEmpty().shouldBeFalse()
            }
        }
    }
}

/**
 * Conducts comprehensive tests to assess the `contains` method functionality within the `Genotype` class, ensuring it
 * accurately identifies the presence of specific chromosomes or a collection thereof.
 *
 * ## Test Scenarios:
 * - **Single Chromosome Presence**: Checks that `contains` truthfully reports the presence of a chromosome within the
 * genotype.
 * - **Single Chromosome Absence**: Validates that `contains` correctly indicates when a chromosome is not part of the
 * genotype.
 * - **Multiple Chromosomes Presence**: Ensures that `containsAll` confirms the inclusion of an entire list of
 * chromosomes in the genotype.
 * - **Partial Chromosome List Presence**: Affirms that `containsAll` accurately detects when not all chromosomes from a
 * list are in the genotype.
 */
@OptIn(ExperimentalKotest::class)
fun `test Genotype contains`() = freeSpec {
    "When checking if a Genotype contains" - {
        "a chromosome" - {
            "should return true if the chromosome is in the genotype" {
                checkAll(intGenotype()) { genotype ->
                    genotype.forEach { chromosome ->
                        chromosome shouldBeIn genotype
                    }
                }
            }

            "should return false if the chromosome is not in the genotype" {
                checkAll(
                    PropTestConfig(maxDiscardPercentage = 30),
                    intGenotype(),
                    arbIntChromosome()
                ) { genotype, chromosome ->
                    assume { chromosome shouldNotBeIn genotype.chromosomes }
                    chromosome shouldNotBeIn genotype
                }
            }
        }

        "all chromosomes in a list" - {
            "should return true if all chromosomes are in the genotype" {
                checkAll(intGenotype()) { genotype ->
                    genotype shouldContainAll genotype.chromosomes
                }
            }

            "should return false if any chromosome is not in the genotype" {
                checkAll(
                    intGenotype(),
                    arbIntChromosome(size = Arb.int(1..5))
                ) { genotype, chromosome ->
                    assume { chromosome shouldNotBeIn genotype.chromosomes }
                    genotype shouldNotContainAll genotype.chromosomes + chromosome
                }
            }
        }
    }
}

private fun genotype(): Arb<Genotype<Int, DummyGene>> = arbGenotype(arbChromosome())

private fun genotypeAndInvalidIndex(): Arb<Pair<Genotype<Int, DummyGene>, Int>> =
    arbGenotype(arbChromosome())
        .flatMap { genotype ->
            val index = Arb.int(genotype.size..genotype.size + 10)
            index.map { genotype to it }
        }

private fun chromosomeFactories(): Arb<List<Chromosome.Factory<Double, DoubleGene>>> =
    Arb.list(arbDoubleChromosomeFactory(), 0..25)

private fun intGenotype(): Arb<Genotype<Int, IntGene>> = arbGenotype(arbIntChromosome())
