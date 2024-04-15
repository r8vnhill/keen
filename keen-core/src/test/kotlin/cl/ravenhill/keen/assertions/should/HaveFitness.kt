package cl.ravenhill.keen.assertions.should

import cl.ravenhill.keen.mixins.FitnessEvaluable
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a [Matcher] for [FitnessEvaluable] instances, aimed at asserting the fitness value. It facilitates the
 * comparison of an entity's fitness against a specified value, enabling the testing of fitness evaluation logic
 * within evolutionary algorithms or similar contexts.
 *
 * @param fitness The expected fitness value for the [FitnessEvaluable] entity.
 * @return A [Matcher] that tests whether a [FitnessEvaluable]'s fitness matches the specified `fitness` value.
 */
fun haveFitness(fitness: Double) = object : Matcher<FitnessEvaluable> {
    override fun test(value: FitnessEvaluable): MatcherResult {
        return MatcherResult(
            value.fitness == fitness,
            { "$value should have fitness $fitness" },
            { "$value should not have fitness $fitness" }
        )
    }
}

/**
 * Provides a domain-specific language (DSL) style assertion for checking the fitness of a [FitnessEvaluable] instance.
 * This infix function allows for more readable tests, where the fitness of an entity can be asserted against a specific
 * value in a clear and expressive manner.
 *
 * ## Usage:
 * Use this function in testing environments where Kotlin infix notation is supported for a more natural language-like
 * assertion. It combines well with test frameworks that allow for flexible assertion styles.
 *
 * ### Example 1: Asserting Fitness in a Kotlin Test
 * ```
 * val individual = GeneticIndividual(genome)
 * individual.evaluateFitness() // Assume this method calculates and sets the fitness of the individual
 * individual shouldHaveFitness 0.95
 * ```
 *
 * @param fitness The expected fitness value to assert against.
 * @receiver [FitnessEvaluable] The instance of the [FitnessEvaluable] that is being tested.
 * @return The result of the assertion, typically integrated into a test framework which handles the assertion result.
 */
infix fun FitnessEvaluable.shouldHaveFitness(fitness: Double) = this should haveFitness(fitness)

/**
 * Defines a DSL (Domain-Specific Language) style assertion for verifying that a [FitnessEvaluable] instance does not
 * have a specified fitness value. This infix function enhances the readability and expressiveness of test assertions,
 * making it clear that the fitness of an entity should not match the given value.
 *
 * ## Usage:
 * Employ this function within test environments that support Kotlin's infix notation, facilitating assertions in a
 * more intuitive and natural language-like manner. It's particularly useful in scenarios where you expect a fitness
 * evaluation process to not yield a certain value, ensuring the robustness and correctness of evolutionary algorithms
 * or fitness evaluation mechanisms.
 *
 * ### Example 1: Asserting Non-Match of Fitness in Tests
 * ```
 * val candidate = CandidateSolution(parameters)
 * candidate.evaluateFitness()  // Assume this method evaluates and sets the fitness of the candidate
 * candidate shouldNotHaveFitness 0.3  // Asserting that the fitness should not be 0.3
 * ```
 *
 * @param fitness The fitness value that the [FitnessEvaluable] instance is asserted not to have.
 * @receiver [FitnessEvaluable] The target instance being asserted against.
 * @return The result of the assertion, typically used within a testing framework that interprets the result.
 */
infix fun FitnessEvaluable.shouldNotHaveFitness(fitness: Double) = this shouldNot haveFitness(fitness)
