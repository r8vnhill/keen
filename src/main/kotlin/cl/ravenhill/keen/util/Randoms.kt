package cl.ravenhill.keen.util

import org.apache.commons.lang3.RandomStringUtils
import kotlin.math.round
import kotlin.random.Random
import kotlin.random.asJavaRandom

/**
 * Generates a random "printable" character.
 */
fun Random.nextChar() =
    RandomStringUtils.random(1, 0, 0, true, true, null, this.asJavaRandom()).first()

/**
 * Returns a sequence of random indexes.
 *
 * @receiver the random number generator.
 * @param pickProbability the probability of picking an index.
 * @param end the end of the range.
 * @param start the start of the range. Defaults to 0.
 */
fun Random.indexes(pickProbability: Double, end: Int, start: Int = 0): Sequence<Int> {
    pickProbability.validateProbability()
    val widenedProbability = round(Int.MAX_VALUE * pickProbability + Int.MIN_VALUE).toInt()
    return when {
        // If the probability is too low, then no indexes will be picked.
        pickProbability <= 1e-20 -> emptySequence()
        // If the probability is too high, then all indexes will be picked.
        pickProbability >= 1 - 1e-20 -> (start until end).asSequence()
        // Otherwise, pick indexes randomly.
        else -> (start until end).asSequence().filter { nextInt() <= widenedProbability }
    }
}

/**
 * Returns a random integer outside the given range.
 */
fun Random.nextIntOutsideOf(range: IntRange) =
    when {
        nextBoolean() && range.first > Int.MIN_VALUE -> (Int.MIN_VALUE until range.first).random(this)
        range.last < Int.MAX_VALUE -> (range.last + 1..Int.MAX_VALUE).random(this)
        else -> (Int.MIN_VALUE until range.first).random(this)
    }