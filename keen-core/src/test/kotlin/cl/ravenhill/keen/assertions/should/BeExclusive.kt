package cl.ravenhill.keen.assertions.should

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.crossover.Crossover
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a [Matcher] for [Crossover] instances to assert whether they are exclusive.
 * An exclusive crossover implies that each gene from the parent can only be used once in generating offspring.
 * This function provides a simple way to test the exclusivity property of a [Crossover] instance within unit tests.
 *
 * ## Characteristics:
 * - **Assertive**: Enables the assertion of the exclusivity state of a crossover operation, which is crucial for
 *  certain evolutionary algorithm strategies.
 * - **Flexible**: Generic in nature, allowing use with any [Crossover] that involves [Gene] derivatives.
 *
 * ### Example:
 * Testing the exclusivity of a `SinglePointCrossover` operation in a genetic algorithm framework:
 * ```kotlin
 * class SinglePointCrossoverTest : FreeSpec({
 *     "A Single Point Crossover Operator can be constructed" - {
 *         "with default parameters" {
 *             shouldNot(beExclusive(SinglePointCrossover<Int, IntGene>()))
 *         }
 *     }
 * })
 * ```
 * In this example, the `beExclusive` matcher is used to verify that a `SinglePointCrossover` instance is not exclusive,
 * which is aligned with its intended behavior. This is useful for confirming the design and functionality of crossover
 * mechanisms in evolutionary algorithms.
 *
 * @param crossover The [Crossover] instance to be matched against.
 * @return A [Matcher] that checks if the [Crossover] instance is exclusive.
 * @param T The type parameter of the gene values.
 * @param G The type of [Gene] being used, which must conform to [Gene] with its own type parameters.
 */
fun <T, G> beExclusive(crossover: Crossover<T, G>) where G : Gene<T, G> = object : Matcher<Crossover<T, G>> {
    override fun test(value: Crossover<T, G>) = MatcherResult(
        value.exclusivity,
        { "Crossover should be exclusive" },
        { "Crossover should not be exclusive" }
    )
}

/**
 * An extension function for [Crossover] instances that asserts the exclusivity of the crossover process.
 * Exclusivity in this context means that each gene from the parent can only be used once when generating offspring,
 * which is critical for ensuring diversity in genetic algorithms. This function leverages the [beExclusive] matcher
 * to perform this assertion.
 *
 * ## Characteristics:
 * - **Assertive**: This function asserts that the crossover operation adheres to an exclusive gene usage policy.
 * - **Simplicity**: Provides a simple and readable way to assert exclusivity directly on [Crossover] instances.
 *
 * ## Usage:
 * Use this function within test suites to verify that the implementation of a [Crossover] instance correctly enforces
 * exclusivity. It is especially useful in genetic algorithms where crossover mechanics play a critical role in the
 * diversity and success of evolutionary strategies.
 *
 * ### Example:
 * Verifying the exclusivity of a `SinglePointCrossover` operation within a genetic algorithm test suite:
 * ```kotlin
 * class SinglePointCrossoverTest : FreeSpec({
 *     "A Single Point Crossover Operator can be constructed" - {
 *         "with default parameters" {
 *             SinglePointCrossover<Int, IntGene>().shouldBeExclusive() // Assertion fails
 *         }
 *     }
 * })
 * ```
 * This example shows how to use `shouldBeExclusive` to assert that a `SinglePointCrossover` is exclusive. The assertion
 * is straightforward and integrates seamlessly into the test suite, enhancing readability and maintainability.
 *
 * @receiver [Crossover] The [Crossover] instance to be tested for exclusivity.
 * @param T The type parameter of the gene values.
 * @param G The type of [Gene] being used, which must conform to [Gene] with its own type parameters.
 */
fun <T, G> Crossover<T, G>.shouldBeExclusive() where G: Gene<T, G> = should(beExclusive(this))

/**
 * An extension function for [Crossover] instances that asserts the non-exclusivity of the crossover process.
 * Non-exclusivity in this context means that genes from the parents can be used more than once when generating
 * offspring, which might be necessary for certain types of genetic algorithms. This function leverages the
 * [beExclusive] matcher to perform this assertion negatively.
 *
 * ## Characteristics:
 * - **Assertive**: This function asserts that the crossover operation does not adhere to an exclusive gene usage
 *  policy.
 * - **Clarity**: Provides a straightforward method to assert non-exclusivity directly on [Crossover] instances.
 *
 * ## Usage:
 * Employ this function within test suites to verify that the implementation of a [Crossover] instance does not enforce
 * exclusivity. It is particularly useful in scenarios where crossover mechanics are expected to allow reuse of genetic
 * material among offspring, enhancing the diversity of the gene pool in a different way than exclusive crossovers.
 *
 * ### Example:
 * Verifying the non-exclusivity of a `UniformCrossover` operation within a genetic algorithm test suite:
 * ```kotlin
 * class UniformCrossoverTest : FreeSpec({
 *     "A Uniform Crossover Operator can be constructed" - {
 *         "with default parameters" {
 *             UniformCrossover<Int, IntGene>().shouldNotBeExclusive() // Asserts that the crossover is non-exclusive
 *         }
 *     }
 * })
 * ```
 * This example shows how to use `shouldNotBeExclusive` to confirm that a `UniformCrossover` does not impose exclusivity.
 * The assertion clearly delineates expected behavior, aiding in the verification of the crossover's design and
 * functionality.
 *
 * @receiver [Crossover] The [Crossover] instance to be tested for non-exclusivity.
 * @param T The type parameter of the gene values.
 * @param G The type of [Gene] being used, which must conform to [Gene] with its own type parameters.
 */
fun <T, G> Crossover<T, G>.shouldNotBeExclusive() where G: Gene<T, G> = shouldNot(beExclusive(this))
