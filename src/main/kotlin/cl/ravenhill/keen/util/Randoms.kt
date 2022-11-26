package cl.ravenhill.keen.util

import org.apache.commons.lang3.RandomStringUtils
import java.util.Random
import kotlin.math.round

/**
 * Generates a random "printable" character.
 */
fun Random.nextChar() =
    RandomStringUtils.random(1, 0, 0, true, true, null, this).first()

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