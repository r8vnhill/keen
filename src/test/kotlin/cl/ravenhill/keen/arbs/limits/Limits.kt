package cl.ravenhill.keen.arbs.limits

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.limits.ListenLimit
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.util.listeners.AbstractEvolutionListener
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.int

/**
 * Generates an arbitrary [GenerationCount] instance for property-based testing.
 *
 * This function leverages Kotest's [Arb] (Arbitrary) API to create a generator for [GenerationCount],
 * a termination criterion based on a fixed number of generations in genetic algorithms. It's particularly
 * useful for testing various scenarios where the evolution process needs to be limited to a specific
 * number of generations.
 *
 * @param count An [Arb]<[Int]> generator that specifies the range within which the generation count
 *              should be generated. It defaults to a range of 1 to 100, ensuring that at least one
 *              generation is processed and allowing for testing with a considerable number of generations.
 *
 * @return An [Arb] that generates [GenerationCount] instances, each with a different generation count
 *         determined by the provided [count] generator. This allows for testing the effect of different
 *         generation limits on the evolutionary process.
 */
fun <T, G> Arb.Companion.generationCount(count: Arb<Int> = int(1..100)) where G : Gene<T, G> = arbitrary {
    GenerationCount<T, G>(count.bind())
}

/**
 * Creates an arbitrary generator for [ListenLimit] instances suitable for property-based testing.
 *
 * This generator utilizes Kotest's [Arb] (Arbitrary) API to create [ListenLimit] instances with
 * dynamically generated conditions based on the current generation number. It's especially useful
 * for scenarios where custom termination conditions are needed for testing the behavior of genetic
 * algorithms under various limit scenarios.
 *
 * @param hi An [Arb]<[Int]> generator that specifies the upper bound for generating a condition
 *           trigger. It defaults to a range of 1 to 100.
 *
 * @return An [Arb] that generates [ListenLimit] instances. Each instance uses a randomly generated
 *         upper bound to create a condition that triggers when the generation number, increased by 1,
 *         is a multiple of the bound. This provides a way to test the evolutionary process under
 *         different periodic conditions.
 */
fun <T, G> Arb.Companion.matchLimit(hi: Arb<Int> = int(1..100)) where G : Gene<T, G> = arbitrary {
    val bound = hi.bind()
    ListenLimit(
        object : AbstractEvolutionListener<T, G>() {
            override fun onGenerationFinished(population: Population<T, G>) {
                generation++
            }
        }) { (generation + 1) % bound == 0 }
}

/**
 * Generates an arbitrary [SteadyGenerations] limit for property-based testing.
 *
 * This function creates instances of [SteadyGenerations], which are used as termination criteria in
 * genetic algorithms. The limit is based on the number of consecutive generations without any
 * improvement in fitness. It leverages Kotest's [Arb] (Arbitrary) API for generating the number of
 * steady generations required to trigger the limit.
 *
 * @param steady An [Arb]<[Int]> generator for the number of steady generations.
 *               Defaults to a range from 1 to 100.
 *
 * @return An [Arb] that generates [SteadyGenerations] instances. Each instance represents a limit
 *         that triggers when the specified number of generations pass without improvement in fitness.
 */
fun <T, G> Arb.Companion.steadyGenerations(steady: Arb<Int> = int(1..100)) where G : Gene<T, G> = arbitrary {
    SteadyGenerations<T, G>(steady.bind())
}

/**
 * Provides an arbitrary generator for creating instances of various [Limit] types used in genetic algorithms.
 *
 * This function combines different types of limits into a single generator, allowing for the random selection
 * of a limit type during property-based testing. The limits include generation count, match limits, and steady
 * generations, each serving as a different criterion to halt the evolutionary process.
 *
 * @return An [Arb] that randomly selects and generates one of the following limit types:
 *         - [GenerationCount]: A limit based on the number of generations processed.
 *         - [MatchLimit]: A customizable limit based on a matching condition.
 *         - [SteadyGenerations]: A limit based on the number of steady generations without improvement.
 */
fun <DNA, G> Arb.Companion.limit(): Arb<Limit<DNA, G>> where G : Gene<DNA, G> =
    choice(generationCount(), matchLimit(), steadyGenerations())

