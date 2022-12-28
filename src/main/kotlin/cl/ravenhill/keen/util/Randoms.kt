package cl.ravenhill.keen.util

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal
import org.apache.commons.lang3.RandomStringUtils
import java.util.stream.IntStream
import kotlin.math.round
import kotlin.random.Random
import kotlin.random.asJavaRandom

/**
 * Generates a random "printable" character.
 */
fun Random.nextChar() =
    RandomStringUtils.random(1, 0, 0, true, true, null, this.asJavaRandom()).first()


/**
 * Returns a stream of pseudorandom int values, each conforming to the given origin (inclusive) and
 * bound (exclusive).
 *
 * @receiver the random instance.
 * @param from the origin (inclusive) of each random value.
 * @param until the bound (exclusive) of each random value.
 * @return a stream of pseudorandom int values.
 */
fun Random.ints(from: Int = 0, until: Int = Int.MAX_VALUE) =
    IntStream.generate { this.nextInt(from, until) }

/**
 * Returns an array with the indices of a subset of the given size.
 *
 * @receiver the random instance.
 * @param from the size of the set.
 * @param pick the number of elements to pick from the set.
 * @return an array with the indices of a subset of the given size.
 */
fun Random.subset(pick: Int, from: Int): IntArray =
    ints(0, from)
        .limit(pick.toLong())
        .sorted()
        .toArray()

/**
 * Returns a sequence of random indexes.
 *
 * @receiver the random number generator.
 * @param pickProbability the probability of picking an index.
 * @param end the end of the range.
 * @param start the start of the range. Defaults to 0.
 */
fun Random.indices(pickProbability: Double, end: Int, start: Int = 0): List<Int> {
    pickProbability.validateProbability()
    val widenedProbability =
        round(Int.MAX_VALUE * pickProbability + Int.MIN_VALUE).toInt()
    return when {
        // If the probability is too low, then no indexes will be picked.
        pickProbability <= 1e-20 -> emptyList()
        // If the probability is too high, then all indexes will be picked.
        pickProbability >= 1 - 1e-20 -> List(end - start) { it + start }
        // Otherwise, pick indexes randomly.
        else -> List(end - start) { start + it }
            .filter { this.nextInt() <= widenedProbability }
    }
}

/**
 * Returns a random integer outside the given range.
 */
fun Random.nextIntOutsideOf(range: Pair<Int, Int>) =
    when {
        nextBoolean() && range.first > Int.MIN_VALUE -> {
            (Int.MIN_VALUE until range.first).random(this)
        }

        range.second < Int.MAX_VALUE -> (range.second + 1..Int.MAX_VALUE).random(this)
        else -> (Int.MIN_VALUE until range.first).random(this)
    }


fun Random.nextDoubleOutsideOf(range: Pair<Double, Double>): Double {
    val (min, max) = range
    return when {
        nextBoolean() && min > Double.MIN_VALUE -> {
            this.nextDouble(Double.MIN_VALUE, min)
        }

        max < Double.MAX_VALUE -> this.nextDouble(max, Double.MAX_VALUE)
        else -> this.nextDouble(Double.MIN_VALUE, min)
    }
}

/**
 * Creates a random program of the given maximum depth, from the given terminals and
 * functions.
 */
fun <T> Random.program(
    maxDepth: Int,
    functions: List<Fun<T>>,
    terminals: List<Terminal<T>>
): Reduceable<T> {
    return when (maxDepth) {
        1 -> terminals.random(this)
        else -> (terminals + functions).random(this).let {
            if (it is Terminal) {
                it.deepCopy()
            } else {
                val f = it.deepCopy() as Fun<T>
                for (i in 0 until it.arity) {
                    f[i] = program(maxDepth - 1, functions, terminals)
                }
                f
            }
        }
    }
}