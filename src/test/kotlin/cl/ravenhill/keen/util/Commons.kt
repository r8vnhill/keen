/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next

/**
 * Asserts that this Double is equal to the specified Double value ``d``, within a certain tolerance
 * range.
 * Uses the [eq] function to compare the values with a default tolerance of 1e-10.
 */
infix fun Double.shouldEq(d: Double) = should(Matcher { value ->
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
infix fun Double.shouldNeq(d: Double) = should(Matcher { value ->
    MatcherResult(
        value neq d,
        { "Expected $d to be different from $value" },
        { "Expected $d but got $value" }
    )
})

/**
 * Asserts that this [Double] value is finite.
 */
fun Double.shouldBeFinite() = should(Matcher { value ->
    MatcherResult(
        value.isFinite(),
        { "$value should be finite" },
        { "$value should not be finite" }
    )
})

/**
 * Returns an arbitrary generator for [Double] values within the given [range], excluding NaN and
 * infinite values.
 */
fun Arb.Companion.real(range: ClosedFloatingPointRange<Double>) = arbitrary {
    double(range).next()
}

/**
 * Returns an [Arb] that generates ordered pairs of values from two other [Arb]s.
 *
 * The generated pair is ordered, so the first element is guaranteed to be less than or equal to the
 * second element.
 * If the two elements are equal, the pair will always contain the same value twice.
 *
 * @param a the first [Arb] to generate values from
 * @param b the second [Arb] to generate values from
 * @return an [Arb] that generates ordered pairs of values from the two given [Arb]s
 */
fun <T : Comparable<T>> Arb.Companion.orderedPair(a: Arb<T>, b: Arb<T>) = arbitrary {
    val i = a.bind()
    val j = b.bind()
    if (i < j) i to j else j to i
}

/**
 * Returns an [Arb] that generates a triple of values of type [T], such that the values are ordered
 * in ascending order.
 * The values are generated from the given [Arb]s for each position in the triple.
 *
 * @param a an [Arb] for generating the first value of type [T]
 * @param b an [Arb] for generating the second value of type [T]
 * @param c an [Arb] for generating the third value of type [T]
 * @return an [Arb] that generates an ordered triple of values of type [T]
 */
fun <T: Comparable<T>> Arb.Companion.orderedTriple(a: Arb<T>, b: Arb<T>, c: Arb<T>) = arbitrary {
    val ns = Arb.list(Arb.choice(a, b, c), 3..3).bind().sorted()
    Triple(ns[0], ns[1], ns[2])
}