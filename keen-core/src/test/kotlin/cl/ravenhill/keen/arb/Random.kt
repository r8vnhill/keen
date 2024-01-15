/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import kotlin.random.Random

/**
 * Encapsulates a seeded [Random] instance for consistent and reproducible random number generation.
 *
 * ## Overview
 * `RandomContext` is a data class that pairs a specific seed value with a [Random] instance. This pairing allows for
 * controlled randomization in scenarios where reproducibility is important, such as in simulations, testing, or
 * any situation where deterministic randomness is required.
 *
 * The seed ensures that the sequence of numbers produced by the [Random] instance is the same each time the
 * program is run with that seed. This predictability is crucial for debugging and for scenarios where consistent
 * behavior is necessary.
 *
 * ## Properties
 * - `seed`: The seed value used to initialize the [Random] instance. A seed is a starting point in the sequence
 *   and determines the subsequent sequence of numbers that the random number generator will produce.
 * - `random`: The [Random] instance initialized with the given `seed`. This instance is used to generate random
 *   numbers or select random elements.
 *
 * ## Usage
 * `RandomContext` is particularly useful in scenarios where the randomness needs to be repeatable. For example,
 * in a game where procedural generation is used, the same seed will always produce the same level. It's also
 * invaluable in testing, where predictable outcomes are necessary.
 *
 * ### Example
 * ```kotlin
 * val randomContext = RandomContext(42L, Random(42L))
 * // Use randomContext.random for random operations
 * ```
 * In this example, a `RandomContext` instance is created with a specific seed. The `random` property can then
 * be used for random operations, and these operations will produce the same results each time the program is run
 * with this seed.
 *
 * @param seed The seed value used for initializing the [Random] instance.
 * @param random The [Random] instance initialized with the specified `seed`.
 */
data class RandomContext(val seed: Long, val random: Random)

/**
 * A shrinker for `RandomContext` instances, used in property-based testing to reduce the complexity of failing test
 * cases.
 *
 * ## Overview
 * `randomShrinker` is a `Shrinker<RandomContext>` which takes a `RandomContext` and generates a sequence of
 * progressively smaller `RandomContext` instances. The shrinking process primarily reduces the seed value, potentially
 * helping to identify simpler failing cases in tests that involve randomization.
 *
 * ## Shrinker Behavior
 * The shrinker works by halving the seed value of the provided `RandomContext` repeatedly. Each step creates a new
 * `RandomContext` with the halved seed, resulting in a different but related random sequence. This process continues
 * until the seed value becomes zero.
 *
 * ## Use in Property-Based Testing
 * In property-based testing, shrinkers are used to simplify failing test cases by finding smaller counterexamples. This
 * `randomShrinker` is particularly useful when a test involving random data fails, as it can provide simpler (smaller seed)
 * contexts that still cause the test to fail, making debugging easier.
 *
 * ### Example
 * Imagine a property-based test that uses `RandomContext` for generating test cases. If the test fails for a complex
 * `RandomContext`, `randomShrinker` can be used to find a simpler context that still reproduces the failure, thus aiding
 * in identifying the root cause.
 *
 * ```kotlin
 * val failingContext = /* A RandomContext that causes a test to fail */
 * val simplerContexts = randomShrinker.shrink(failingContext)
 * // simplerContexts now holds a list of simpler RandomContext instances to investigate
 * ```
 *
 * In this example, the `randomShrinker` is used to obtain a series of simpler `RandomContext` instances from a complex
 * one that caused a test failure. These simpler instances can then be examined to find the minimal test case that still
 * fails, thereby facilitating easier debugging and understanding of the issue.
 *
 * @return A list of `RandomContext` instances with progressively smaller seeds, aiding in the identification of simpler failing cases.
 */
val randomShrinker = Shrinker<RandomContext> { randomContext ->
    generateSequence(randomContext) {
        val newSeed = randomContext.seed / 2
        RandomContext(newSeed, Random(newSeed))
    }.take(1000)
        .toList()
}

/**
 * Generates an arbitrary instance of [Random] for property-based testing.
 *
 * This function creates instances of [Random] with different seeds, making it ideal for scenarios in property-based
 * testing where randomized but reproducible behavior is desired. By varying the seed, this function can produce a
 * range of [Random] instances, each with potentially different sequences of random values.
 *
 * ### Example:
 * Generating a [Random] instance with a specific seed:
 * ```kotlin
 * val randomArb = Arb.random(Arb.constant(1234L)) // Using a constant seed
 * val randomInstance = randomArb.bind() // Random instance with the seed 1234
 * ```
 * In this example, `randomInstance` is a [Random] object initialized with a constant seed, ensuring the same
 * sequence of random numbers in every test run.
 *
 * @param seed An optional [Arb] of type [Long] for generating random seeds. Defaults to [Arb.Companion.long],
 *             which generates arbitrary long values as seeds.
 * @return An [Arb] that generates instances of [Random] initialized with various seeds.
 */
fun Arb.Companion.random(seed: Arb<Long> = long()) = arbitrary {
    Random(seed.bind())
}

/**
 * Factory function to create arbitrary instances of `RandomContext` for property-based testing, with a shrinker
 *
 * ## Overview
 * This function is part of the `Arb.Companion` object and is used to generate arbitrary instances of the
 * `RandomContext` class. It's designed for use in property-based testing scenarios where different `RandomContext`
 * instances are needed to test randomness-dependent functionalities.
 *
 * @param seed An arbitrary long generator to create the seed for the `RandomContext`.
 * @return An arbitrary instance of `RandomContext` with a randomly generated seed.
 * @see RandomContext
 * @see randomShrinker
 */
fun Arb.Companion.randomContext(seed: Arb<Long> = long()) = arbitrary(randomShrinker) {
    val initialSeed = seed.bind()
    RandomContext(initialSeed, Random(initialSeed))
}

/**
 * Creates an arbitrary generator for pairs of [Random] instances with the same seed.
 *
 * This function is part of the [Arb.Companion] object and is designed to generate pairs of `Random` objects
 * initialized with the same seed value. The seed value is provided by the optional arbitrary generator [seed],
 * which defaults to the `long()` ([Arb.Companion.long] generator. This approach ensures that both `Random` instances in
 * the pair will produce the same sequence of random values, which is useful in scenarios requiring reproducible random
 * behavior.
 *
 * ## Usage:
 * Utilize this arbitrary in testing scenarios where you need two `Random` instances with identical behavior,
 * such as in simulations, randomized algorithms, or any case where reproducibility of random sequences is important.
 *
 * ### Example:
 * ```kotlin
 * val rngPairGen = Arb.rngPair()
 * val (rng1, rng2) = rngPairGen.bind() // Generates a pair of Random instances with the same seed
 * // rng1 and rng2 will produce the same sequence of random values
 * ```
 * In this example, `rngPairGen` is an arbitrary that generates a pair of `Random` instances. Both instances
 * are initialized with the same seed value, thus producing identical sequences of random values.
 *
 * @param seed An optional [Arb]<[Long]> to provide seed values for the `Random` instances. Defaults to `long()`
 *   ([Arb.Companion.long]
 * @return An [Arb]<[Pair]<[Random], [Random]>> that generates pairs of `Random` instances with the same seed.
 */
fun Arb.Companion.rngPair(seed: Arb<Long> = long()) = arbitrary {
    seed.bind().let { Random(it) to Random(it) }
}
