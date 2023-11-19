/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.util.listeners

import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.ListenLimit
import cl.ravenhill.keen.limits.ListenLimitTest
import cl.ravenhill.keen.util.listeners.EvolutionListener
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

/**
 * Tests [ListenLimit] instances with varying numbers of generations to verify their behavior.
 *
 * This function facilitates the testing of different [ListenLimit] configurations by simulating
 * the evolutionary process for a specified number of generations. It checks if the limit's
 * condition is met accurately at each generation.
 *
 *
 * ## Usage Scenario in Testing
 *
 * ### Example: Testing ListenLimit with Varying Generations
 * This function is used in [ListenLimitTest] to check the behavior of [ListenLimit] instances.
 * It verifies whether the limit's condition, defined by the predicate, accurately determines
 * if the limit is met for different numbers of generations.
 *
 * In the test suite, the function is invoked with specific configurations of [ListenLimit] and
 * a predicate to assert the expected behavior. The test ensures that the limit's condition is
 * consistently evaluated across multiple generations and confirms that the limit behaves as expected.
 *
 * ```kotlin
 * class ListenLimitTest : FreeSpec({
 *     "A [ListenLimit]" - {
 *         // Test cases using `test ListenLimit with varying generations`
 *         "when invoked" - {
 *             "accurately determines if the limit condition is met" {
 *                 `test ListenLimit with varying generations`({ Arb.listenLimit<Int, IntGene>(it) }) { count ->
 *                     generation % count == 0
 *                 }
 *             }
 *         }
 *     }
 * })
 * ```
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param arbFactory A factory function that produces an [Arb] of [ListenLimit] instances.
 *                   The factory takes an integer representing the number of generations
 *                   and returns a corresponding [ListenLimit].
 * @param predicate A predicate function that takes an [EvolutionListener] and a count of generations.
 *                  It defines the condition upon which the limit is expected to be met.
 *
 * @throws AssertionError if the limit's condition does not behave as expected according to the predicate.
 */
suspend fun <T, G> `test ListenLimit with varying generations`(
    arbFactory: (Int) -> Arb<ListenLimit<T, G>>,
    predicate: EvolutionListener<T, G>.(count: Int) -> Boolean
) where G : Gene<T, G> {
    checkAll(
        Arb.int(1..100).compose { arbFactory(it) }, Arb.int(1..100)
    ) { (count, limit), generations ->
        limit.listener.onGenerationStarted(emptyList())
        repeat(generations) {
            limit.listener.onGenerationFinished(emptyList())
        }
        limit() shouldBe limit.listener.predicate(count)
    }
}
