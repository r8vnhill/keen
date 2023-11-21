/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import kotlin.random.Random

/**
 * Generates an arbitrary instance of [Random] using a seed provided by [seed].
 *
 * This function is useful for creating instances of [Random] with reproducible sequences of random
 * numbers. By using a seeded generator, tests and simulations that require randomization can be
 * replicated exactly across different runs, which is essential for debugging and verifying behaviors
 * in deterministic environments.
 *
 * @param seed An [Arb]<[Long]> that generates the seed value for the [Random] instance.
 *             If no specific seed is provided, a default generator for [Long] is used.
 *
 * ## Example Usage:
 * ```
 * // Creating a Random instance with a specific seed
 * val seedArb = Arb.long(0L..100L)
 * val randomArb = Arb.random(seedArb)
 * val randomInstance = randomArb.bind() // Instance of Random with a fixed seed
 *
 * // Using the Random instance for generating reproducible random numbers
 * val randomNumber = randomInstance.nextInt()
 * ```
 *
 * @return An [Arb]<[Random]> that generates instances of [Random] with seeds derived from the given [seed].
 */
fun Arb.Companion.random(seed: Arb<Long> = long()) = arbitrary { Random(seed.bind()) }
