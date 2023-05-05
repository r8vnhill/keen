/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.doubles.shouldNotBeNaN
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.math.sqrt

// region : -== SHOULD ASSERTIONS ==-
// region : -== INT ==-
/**
 * Asserts that the integer is within the specified range (inclusive).
 *
 * @param range the range to check against
 *
 * @throws AssertionError if the integer is not within the range
 */
private infix fun Int.shouldBeIn(range: IntToInt) = should(Matcher { value ->
    MatcherResult(
        value in range,
        { "$value should be in $range" },
        { "$value should not be in $range" }
    )
})

/**
 * Asserts that the integer is not within the specified range (inclusive).
 *
 * @param range the range to check against
 *
 * @throws AssertionError if the integer is within the range
 */
private infix fun Int.shouldNotBeIn(range: IntToInt) = should(Matcher { value ->
    MatcherResult(
        value !in range,
        { "$value should not be in $range" },
        { "$value should be in $range" }
    )
})
// endregion INT

// region : -== DOUBLE ==-
/**
 * Asserts that the [Double] is within the specified ``range`` (inclusive).
 */
private infix fun Double.shouldBeIn(range: DoubleToDouble) = should(Matcher { value ->
    MatcherResult(
        value in range,
        { "$value should be in $range" },
        { "$value should not be in $range" }
    )
})

/**
 * Asserts that the [Double] is not within the specified ``range`` (inclusive).
 */
private infix fun Double.shouldNotBeIn(range: DoubleToDouble) = should(Matcher { value ->
    MatcherResult(
        value !in range,
        { "$value should not be in $range" },
        { "$value should be in $range" }
    )
})

/**
 * Asserts that this [Double] value is finite.
 */
private fun Double.shouldBeFinite() = should(Matcher { value ->
    MatcherResult(
        value.isFinite(),
        { "$value should be finite" },
        { "$value should not be finite" }
    )
})

/**
 * Asserts that this Double is equal to the specified Double value ``d``, within a certain tolerance
 * range.
 * Uses the [eq] function to compare the values with a default tolerance of 1e-10.
 */
private infix fun Double.shouldEq(d: Double) = should(Matcher { value ->
    MatcherResult(
        value eq d,
        { "Expected $d but got $value" },
        { "Expected $d to be different from $value" }
    )
})

/**
 * Asserts that this Double is not equal to the specified Double value ``d``, within a certain
 * tolerance range.
 * Uses the [neq] function to compare the values with a default tolerance of 1e-10.
 */
private infix fun Double.shouldNeq(d: Double) = should(Matcher { value ->
    MatcherResult(
        value neq d,
        { "Expected $d to be different from $value" },
        { "Expected $d but got $value" }
    )
})
// endregion DOUBLE
// endregion SHOULD ASSERTIONS

// region : -== ARBITRARY GENERATORS ==-
/**
 * Returns an [Arb] that generates [Triple]<Int, Int, Int> instances, where the three integers are
 * guaranteed to be in ascending order.
 *
 * This is useful for testing functions that expect input in ascending order.
 */
private fun Arb.Companion.orderedIntTriple() = arbitrary {
    val ns = Arb.list(Arb.int(), 3..3).bind().sorted()
    Triple(ns[0], ns[1], ns[2])
}

/**
 * Generates an [Arb]itrary [Pair] of [Int]s, where the first element is a positive integer and the
 * second element is a positive divisor of the first element.
 */
private fun Arb.Companion.intAndDivisor() = arbitrary { rs ->
    val number = Arb.positiveInt().bind()
    val divisor = number
        .divisors(rs)
        .first()
    number to divisor
}

/**
 * Generates an [Arb]itrary [Pair] of [Int]s, where the first element is a positive integer and the
 * second element is a positive non-divisor of the first element.
 */
private fun Arb.Companion.nonDivisiblePair() = arbitrary { rs ->
    val number = Arb.int(3, Int.MAX_VALUE).bind()
    val nonDivisor = number
        .nonDivisors(rs)
        .take(1)
        .first()
    number to nonDivisor
}

/**
 * Returns an [Arb] that generates [Triple]<Double, Double, Double> instances, where the three
 * doubles are guaranteed to be in ascending order.
 * This is useful for testing functions that expect input in ascending order.
 */
private fun Arb.Companion.orderedDoubleTriple() = arbitrary {
    val ns = Arb.list(Arb.double(), 3..3).bind().sorted()
    Triple(ns[0], ns[1], ns[2])
}

/**
 * Returns an [Arb] that generates pairs of doubles with a difference of at most 1e-10.
 * The first element is generated by [Arb.Companion.double] and the second element is obtained by
 * adding a random double value in the range [0.0, 1e-10] to the first element.
 */
private fun Arb.Companion.closePair() = arbitrary {
    val a = Arb.double().next()
    val b = a + Arb.double(0.0..1e-10).next()
    a to b
}

/**
 * Returns an [Arb] that generates pairs of doubles with a difference of at least 1e-10.
 * The first element is generated by [Arb.Companion.double] and the second element is obtained by
 * adding a random double value in the range [1e-10, 1.0] to the first element.
 */
private fun Arb.Companion.farPair() = arbitrary {
    val a = Arb.double().next()
    val maxDiff = Double.MAX_VALUE - a
    val b = if (maxDiff > 1e-10) {
        a + Arb.double(1e-10..maxDiff).next()
    } else {
        Double.MAX_VALUE
    }
    a to b
}
// endregion ARBITRARY GENERATORS

// region : -== AUXILIARY FUNCTIONS ==-
/**
 * Returns a sequence of all divisors of this [Int] instance, shuffled using the given [rs]
 * [RandomSource].
 * The divisors are generated lazily using the Sieve of Eratosthenes algorithm and are not
 * guaranteed to be in any particular order.
 *
 * ## References
 *
 * 1. “Sieve of Eratosthenes.” In Wikipedia, April 18, 2023.
 *    https://en.wikipedia.org/w/index.php?title=Sieve_of_Eratosthenes&oldid=1150503937.
 */
private fun Int.divisors(rs: RandomSource) = sequence {
    // Initialize the loop counter and the maximum value to check
    val n = this@divisors.toDouble()
    // Loop through all numbers between 1 and the square root of n
    for (i in 1 until sqrt(n).toInt() + 1) {
        // If ``i`` is a divisor, yield it and its complement (if it's not a perfect square)
        if (n % i == 0.0) {
            yield(i)
            // If ``i`` is not equal to n/i (the other divisor), add it to the sequence too
            if (i != (n / i).toInt()) yield((n / i).toInt())
        }
    }
}.shuffled(rs.random)

/**
 * Returns a randomly ordered lazy [Sequence] of all the non-divisors of this [Int].
 */
private fun Int.nonDivisors(rs: RandomSource) =
    generateSequence { rs.random.nextInt(this) }
        .filter { it >= 2 && this % it != 0 }
// endregion AUXILIARY FUNCTIONS

class NumbersKtTest : FreeSpec({
    "An integer pair range" - {
        "contains an integer within the range" {
            checkAll(Arb.orderedIntTriple()) { (lo, mid, hi) ->
                mid shouldBeIn (lo to hi)
            }
        }

        "does not contain an integer outside the range" {
            checkAll(Arb.orderedIntTriple()) { (lo, mid, hi) ->
                assume {
                    lo shouldBeLessThan mid
                    mid shouldBeLessThan hi
                }
                lo shouldNotBeIn (mid to hi)
                hi shouldNotBeIn (lo to mid)
            }
        }
    }

    "A [Double] [Pair] range" - {
        "contains a [Double] within the range" {
            checkAll(Arb.orderedDoubleTriple()) { (lo, mid, hi) ->
                assume {
                    lo.shouldNotBeNaN()
                    mid.shouldNotBeNaN()
                    hi.shouldNotBeNaN()
                    lo.shouldBeFinite()
                }
                mid shouldBeIn (lo to hi)
            }
        }

        "does not contain a [Double] outside the range" {
            checkAll(Arb.orderedDoubleTriple()) { (lo, mid, hi) ->
                assume {
                    lo shouldBeLessThan mid
                    mid shouldBeLessThan hi
                }
                lo shouldNotBeIn (mid to hi)
                hi shouldNotBeIn (lo to mid)
            }
        }
    }

    "Rounding a number to the next multiple of another number" - {
        "the number is a multiple of the other number" - {
            "return the same number" {
                checkAll(Arb.intAndDivisor()) { (number, divisor) ->
                    number roundUpToMultipleOf divisor shouldBe number
                }
            }
        }

        "the number is not a multiple of the other number" - {
            "return the next multiple of the other number" {
                checkAll(
                    Arb.nonDivisiblePair()
                ) { (number, nonDivisor) ->
                    number roundUpToMultipleOf nonDivisor shouldBe
                            number + nonDivisor - number % nonDivisor
                }
            }
        }
    }

    "Checking that a number is not a NaN" - {
        "return true if the number is not a NaN" {
            checkAll(Arb.double()) { d ->
                assume(!d.isNaN())
                d.isNotNan() shouldBe true
            }
        }

        "return false if the number is a NaN" {
            val nan = Double.NaN
            nan.isNotNan() shouldBe false
        }
    }

    "[Double] equality should" - {
        "be true for the same number" {
            checkAll(Arb.closePair()) { (a, b) ->
                a shouldEq b
            }
        }

        "be false for two different numbers" {
            checkAll(Arb.farPair()) { (a, b) ->
                a shouldNeq b
            }
        }

        "be false for two NaNs" {
            Double.NaN shouldNeq Double.NaN
        }
    }
})
