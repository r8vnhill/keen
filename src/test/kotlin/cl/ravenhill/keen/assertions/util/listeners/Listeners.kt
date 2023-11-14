package cl.ravenhill.keen.assertions.util.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.ListenLimit
import cl.ravenhill.keen.util.listeners.EvolutionListener
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

/**
 * Tests the behavior of [ListenLimit] with varying numbers of generations.
 *
 * This function checks whether the [ListenLimit] correctly determines when to stop the evolution process
 * based on a predefined predicate and a varying number of generations. It runs through multiple generations,
 * invoking the limit's listener's `onGenerationFinished` method each time, and then assesses if the limit's
 * stopping condition is met as expected.
 *
 * ## Examples
 * ### Example 1: Testing a simple generation count limit
 * ```
 * val generationLimit = GenerationCount<Int, IntGene>(5)
 * val limitArb = Arb.constant(generationLimit)
 * val predicate = { generations: Int -> this.generation >= generations }
 * `test ListenLimit with varying generations`(limitArb, predicate)
 * ```
 * In this example, the test will check if the [ListenLimit] correctly stops the evolution after 5 generations.
 *
 * @param T The type of the gene's value.
 * @param G The specific type of [Gene].
 * @param limitArb An [Arb] that generates instances of [ListenLimit].
 * @param predicate A lambda function defining the stopping condition based on the generation count.
 */
suspend fun <T, G> `test ListenLimit with varying generations`(
    limitArb: Arb<ListenLimit<T, G>>,
    predicate: EvolutionListener<T, G>.(Int) -> Boolean
) where G : Gene<T, G> {
    checkAll(limitArb, Arb.int(1..100)) { limit, generations ->
        repeat(generations) {
            limit.listener.onGenerationFinished(emptyList())
        }
        limit() shouldBe limit.listener.predicate(generations)
    }
}
