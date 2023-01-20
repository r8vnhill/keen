package cl.ravenhill.keen.util.math

import cl.ravenhill.keen.util.logging.Level
import cl.ravenhill.keen.util.logging.logger
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.*
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.positiveInt
import kotlin.math.abs
import kotlin.math.sqrt

class NumbersKtTest : WordSpec({

    "Rounding a number to the next multiple of another number" When {
        "the number is a multiple of the other number" should {
            "return the same number" {
                checkAll(Arb.intAndDivisor()) { (number, divisor) ->
                    number roundUpToMultipleOf divisor shouldBe number
                }
            }
        }
        "the number is not a multiple of the other number" should {
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

    "Checking that a number is not a NaN" should {
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

    "Checking if two Doubles are EQUAL" should {
        "return TRUE if the difference between the two numbers is less than 1e-10" {
            checkAll(Arb.double(), Arb.double(-1e-10, 1e-10)) { n, eps ->
                assume(n.isFinite() && eps.isFinite())
                n eq n + eps shouldBe true
                n neq n + eps shouldBe false
            }
        }
        "return FALSE if the difference between the two numbers is greater than 1e-10" {
            checkAll(
                Arb.double(-1e10, 1e10),
                Arb.double(-1e10, 1e10)
            ) { n, displacement ->
                assume(abs(displacement) > 1e-10)
                n eq n + displacement shouldBe false
                n neq n + displacement shouldBe true
            }
        }
    }
})

val l = logger("test") {
    level = Level.Trace()
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

private fun Arb.Companion.nonDivisiblePair() = arbitrary { rs ->
    val number = Arb.int(3, Int.MAX_VALUE).bind()
    val nonDivisor = number
        .nonDivisors(rs)
        .take(1)
        .first()
    number to nonDivisor
}

/**
 * Returns a randomly ordered lazy [Sequence] of all the divisors of this [Int].
 */
private fun Int.divisors(rs: RandomSource) = sequence {
    var i = 1
    val n = this@divisors.toDouble()
    while (i <= sqrt(n)) {
        if (n % i == 0.0) {
            yield(i)
            if (i != (n / i).toInt()) yield((n / i).toInt())
        }
        i++
    }
}.shuffled(rs.random)

private fun Int.nonDivisors(rs: RandomSource) =
    generateSequence { rs.random.nextInt(this) }
        .filter { it >= 2 && this % it != 0 }
