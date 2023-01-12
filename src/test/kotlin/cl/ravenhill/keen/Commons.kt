package cl.ravenhill.keen

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import kotlin.reflect.KClass


/**
 * Matcher that checks if the given object is of the given class.
 */
infix fun Any.shouldBeOfClass(kClass: KClass<*>) = Matcher<Any> { value ->
    MatcherResult(
        value::class == kClass,
        { "$value should be an instance of $kClass" },
        { "$value should not be an instance of $kClass" }
    )
}

/**
 * Generates [Arb]itrary [Pair]s of [Int]s where the first element is less than or equal
 * to the second.
 *
 * __Usage:__
 * ```kotlin
 * checkAll(Arb.orderedIntPair()) { (a, b) ->
 *    a <= b shouldBe true
 *    a + b shouldBe b + a
 *    a * b shouldBe b * a
 *    a - b shouldBe -(b - a)
 *    a / b shouldBe 0
 *    a % b shouldBe a
 *    a * b + a - b / a % b shouldBe a * b
 * }
 * ```
 */
fun Arb.Companion.orderedIntPair(lo: Int = Int.MIN_VALUE, hi: Int = Int.MAX_VALUE) =
    arbitrary {
        val first = int(lo, hi).bind()
        val second = int(lo, hi).bind().let { if (it == first) it + 1 else it }
        if (first < second) first to second else second to first
    }