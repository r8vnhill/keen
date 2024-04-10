/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import kotlin.random.Random

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
