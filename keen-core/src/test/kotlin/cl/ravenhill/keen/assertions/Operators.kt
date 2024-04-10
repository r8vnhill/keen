package cl.ravenhill.keen.assertions

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.mutation.GeneMutator
import cl.ravenhill.keen.operators.alteration.mutation.Mutator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll

/**
 * Tests the individual rate property of a Mutator in a property-based testing style.
 *
 * This function is designed to verify the behavior of the individual rate property in instances of a Mutator. It
 * checks the default value of the property, ensures it can be set within a valid range (0 to 1), and validates that
 * exceptions are thrown for invalid values.
 *
 * ## Test Scenarios:
 * - **Default Value Check**: Verifies that the individual rate defaults to the specified value in `default`.
 * - **Valid Range Check**: Confirms that the individual rate can be set to any value between 0 and 1.
 * - **Invalid Value Handling**: Ensures that an exception is thrown when the individual rate is set outside the
 *   range of 0 to 1.
 *
 * ## Usage:
 * The function should be used as part of a test suite for a genetic algorithm framework to ensure the robustness
 * and correctness of Mutator implementations.
 *
 * ### Example Usage:
 * ```kotlin
 * include(`test individual rate property`<Int, IntGene>(
 *     name = "SomeMutator",
 *     default = "default value" to 0.5,
 *     defaultBuilder = { chromosomeRate, geneRate -> SomeMutator(chromosomeRate, geneRate) },
 *     completeBuilder = { individualRate, chromosomeRate, geneRate ->
 *         SomeMutator(individualRate, chromosomeRate, geneRate)
 *     }
 * ))
 * ```
 * In this example, the function is used to test the individual rate property of `SomeMutator`.
 *
 * @param T The type of data encapsulated by the genes within the chromosomes.
 * @param G The type of gene in the chromosomes, conforming to the [Gene] interface.
 * @param name The name of the Mutator being tested, used for descriptive purposes in the test output.
 * @param default A pair containing a descriptive string and the default value for the individual rate.
 * @param defaultBuilder A function that creates a Mutator instance with default individual rate settings.
 * @param completeBuilder A function that creates a complete Mutator instance with specified individual, chromosome,
 *   and gene rates.
 */
fun <T, G> `test Mutator individual rate property`(
    default: Pair<String, Double>,
    defaultBuilder: (chromosomeRate: Double, geneRate: Double) -> Mutator<T, G>,
    completeBuilder: (individualRate: Double, chromosomeRate: Double, geneRate: Double) -> Mutator<T, G>
) where G : Gene<T, G> = freeSpec {
    "Should have an individual rate property that" - {
        "defaults to ${default.first}" {
            checkAll(arbProbability(), arbProbability()) { chromosomeRate, geneRate ->
                val mutator = defaultBuilder(chromosomeRate, geneRate)
                mutator.individualRate shouldBe default.second
            }
        }

        "can be set to a value between 0 and 1" {
            checkAll(
                arbProbability(),
                arbProbability(),
                arbProbability()
            ) { individualRate, chromosomeRate, geneRate ->
                val mutator = completeBuilder(individualRate, chromosomeRate, geneRate)
                mutator.individualRate shouldBe individualRate
            }
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            checkAll(
                Arb.double().filterNot { it in 0.0..1.0 },
                Arb.double(),
                Arb.double()
            ) { individualRate, chromosomeRate, geneRate ->
                shouldThrow<CompositeException> {
                    completeBuilder(individualRate, chromosomeRate, geneRate)
                }.shouldHaveInfringement<MutatorConfigException>(
                    "The individual rate ($individualRate) must be in 0.0..1.0"
                )
            }
        }
    }
}

/**
 * Tests the chromosome rate property of a Mutator in a property-based testing style.
 *
 * This function is designed to assess the behavior of the chromosome rate property in instances of a Mutator. It
 * verifies the default value of the property, checks that it can be set within the valid range (0 to 1), and validates
 * that exceptions are thrown for values outside this range.
 *
 * ## Test Scenarios:
 * - **Default Value Check**: Ensures that the chromosome rate defaults to the value specified in `default`.
 * - **Valid Range Check**: Confirms that the chromosome rate can be set to any value between 0 and 1.
 * - **Invalid Value Handling**: Verifies that an exception is thrown when the chromosome rate is set to a value outside
 *   the range of 0 to 1.
 *
 * ## Usage:
 * This function should be included as part of a test suite for a genetic algorithm framework to ensure the integrity
 * and correct functionality of Mutator implementations.
 *
 * ### Example Usage:
 * ```kotlin
 * include(`test mutator chromosome rate property`<Int, IntGene>(
 *     name = "CustomMutator",
 *     default = "default value" to 0.5,
 *     defaultBuilder = { individualRate, geneRate -> CustomMutator(individualRate, geneRate) },
 *     completeBuilder = { individualRate, chromosomeRate, geneRate ->
 *         CustomMutator(individualRate, chromosomeRate, geneRate)
 *     }
 * ))
 * ```
 * In this example, the function is used to test the chromosome rate property of a `CustomMutator`.
 *
 * @param T The type of data encapsulated by the genes within the chromosomes.
 * @param G The type of gene in the chromosomes, conforming to the [Gene] interface.
 * @param name The name of the Mutator being tested, used for descriptive purposes in the test output.
 * @param default A pair containing a descriptive string and the default value for the chromosome rate.
 * @param defaultBuilder A function that creates a Mutator instance with default chromosome rate settings.
 * @param completeBuilder A function that creates a complete Mutator instance with specified individual, chromosome,
 *   and gene rates.
 */
fun <T, G> `test Mutator chromosome rate property`(
    default: Pair<String, Double>,
    defaultBuilder: (chromosomeRate: Double, geneRate: Double) -> Mutator<T, G>,
    completeBuilder: (individualRate: Double, chromosomeRate: Double, geneRate: Double) -> Mutator<T, G>
) where G: Gene<T, G> = freeSpec {
    "Should have a chromosome rate property that" - {
        "defaults to ${default.first}" {
            checkAll(arbProbability(), arbProbability()) { individualRate, geneRate ->
                val mutator = defaultBuilder(individualRate, geneRate)
                mutator.chromosomeRate shouldBe default.second
            }
        }

        "can be set to a value between 0 and 1" {
            checkAll(
                arbProbability(),
                arbProbability(),
                arbProbability()
            ) { individualRate, chromosomeRate, geneRate ->
                val mutator = completeBuilder(individualRate, chromosomeRate, geneRate)
                mutator.chromosomeRate shouldBe chromosomeRate
            }
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            checkAll(
                Arb.double(),
                Arb.double().filterNot { it in 0.0..1.0 },
                Arb.double()
            ) { individualRate, chromosomeRate, geneRate ->
                shouldThrow<CompositeException> {
                    completeBuilder(individualRate, chromosomeRate, geneRate)
                }.shouldHaveInfringement<MutatorConfigException>(
                    "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"
                )
            }
        }
    }
}

/**
 * Tests the gene rate property of a GeneMutator in a property-based testing style.
 *
 * This function is specifically designed to validate the behavior of the gene rate property in instances of a
 * GeneMutator. It ensures the default value of the gene rate is set as expected, verifies that it can be set within
 * the valid range of 0 to 1, and confirms that exceptions are thrown for values outside this range.
 *
 * ## Test Scenarios:
 * - **Default Value Check**: Asserts that the gene rate defaults to the value specified in the `default` parameter.
 * - **Valid Range Check**: Ensures that the gene rate can be correctly set to any value within the range of 0 to 1.
 * - **Invalid Value Handling**: Validates that an exception is thrown when the gene rate is set to a value outside
 *   the range of 0 to 1.
 *
 * ## Usage:
 * The function should be included as part of a test suite for a genetic algorithm framework, specifically for testing
 * the robustness and correctness of GeneMutator implementations.
 *
 * ### Example Usage:
 * ```kotlin
 * include(`test Gene Mutator gene rate`<Int, IntGene>(
 *     name = "CustomGeneMutator",
 *     default = "default value" to 0.5,
 *     defaultBuilder = { chromosomeRate, geneRate -> CustomGeneMutator(chromosomeRate, geneRate) },
 *     completeBuilder = { individualRate, chromosomeRate, geneRate ->
 *         CustomGeneMutator(individualRate, chromosomeRate, geneRate)
 *     }
 * ))
 * ```
 * In this example, the function tests the gene rate property of a `CustomGeneMutator`.
 *
 * @param T The type of data encapsulated by the genes within the chromosomes.
 * @param G The type of gene in the chromosomes, conforming to the [Gene] interface.
 * @param name The name of the GeneMutator being tested, used for descriptive purposes in test output.
 * @param default A pair containing a descriptive string and the default value for the gene rate.
 * @param defaultBuilder A function that creates a GeneMutator instance with default gene rate settings.
 * @param completeBuilder A function that creates a complete GeneMutator instance with specified individual,
 *   chromosome, and gene rates.
 */
fun <T, G> `test Gene Mutator gene rate`(
    default: Pair<String, Double>,
    defaultBuilder: (chromosomeRate: Double, geneRate: Double) -> GeneMutator<T, G>,
    completeBuilder: (individualRate: Double, chromosomeRate: Double, geneRate: Double) -> GeneMutator<T, G>
) where G: Gene<T, G> = freeSpec {
    "Should have a gene rate property that" - {
        "defaults to ${default.first}" {
            checkAll(arbProbability(), arbProbability()) { chromosomeRate, geneRate ->
                val mutator = defaultBuilder(chromosomeRate, geneRate)
                mutator.geneRate shouldBe default.second
            }
        }

        "can be set to a value between 0 and 1" {
            checkAll(
                arbProbability(),
                arbProbability(),
                arbProbability()
            ) { individualRate, chromosomeRate, geneRate ->
                val mutator = completeBuilder(individualRate, chromosomeRate, geneRate)
                mutator.geneRate shouldBe geneRate
            }
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            checkAll(
                Arb.double(),
                Arb.double(),
                Arb.double().filterNot { it in 0.0..1.0 }
            ) { individualRate, chromosomeRate, geneRate ->
                shouldThrow<CompositeException> {
                    completeBuilder(individualRate, chromosomeRate, geneRate)
                }.shouldHaveInfringement<MutatorConfigException>(
                    "The gene rate ($geneRate) must be in 0.0..1.0"
                )
            }
        }
    }
}
