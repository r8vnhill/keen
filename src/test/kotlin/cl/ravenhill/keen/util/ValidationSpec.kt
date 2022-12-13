package cl.ravenhill.keen.util

import cl.ravenhill.keen.InvalidArgumentException
import cl.ravenhill.keen.util.math.isNotNan
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random


class ValidationSpec : WordSpec({
    "Validating a predicate" should {
        "return the value if the predicate is true" {
            checkAll<Int> {
                assume(it % 10 != 0)
                shouldNotThrow<InvalidArgumentException> {
                    validatePredicate({ it % 10 != 0 }) { "" }
                }
            }
        }
        "throw an exception if the predicate is false" {
            checkAll<Int> {
                assume(it % 10 != 0)
                shouldThrow<InvalidArgumentException> {
                    validatePredicate({ it % 10 == 0 }) { "" }
                }
            }
        }
    }
    "Validating a probability" should {
        "return the value if it is between 0 and 1" {
            checkAll(Arb.double(0.0, 1.0)) {
                assume(it.isNotNan() && it.isFinite())
                shouldNotThrow<InvalidArgumentException> {
                    it.validateProbability()
                }
            }
        }
        "throw an exception if it is not between 0 and 1" {
            checkAll<Double> {
                assume(it !in 0.0..1.0)
                shouldThrow<InvalidArgumentException> {
                    it.validateProbability()
                }
            }
        }
    }
    "Validating if a value is in a range" should {
        "return the value if it is in the range" {
            checkAll<Int, Int, Long> { i1, i2, seed ->
                assume(i1 != i2)
                val range = if (i1 < i2) i1..i2 else i2..i1
                val value = range.random(Random(seed))
                value.validateRange(range, "Test") shouldBe value
            }
        }
        "throw an exception if it is not in the range" {
            checkAll<Int, Int, Long> { i1, i2, seed ->
                assume(i1 != i2)
                assume(i1 != Int.MIN_VALUE || i2 != Int.MAX_VALUE)
                val range = if (i1 < i2) i1 to i2 else i2 to i1
                val value = Random(seed).nextIntOutsideOf(range)
                shouldThrow<InvalidArgumentException> {
                    value.validateRange(range, "Test")
                }
            }
        }
    }
    "Validating if a value is at least a minimum" should {
        "return the value if it is at least the minimum" {
            checkAll<Int, Int> { i1, i2 ->
                val (min, value) = if (i1 < i2) i1 to i2 else i2 to i1
                value.validateAtLeast(min, "Test") shouldBe value
            }
        }
        "throw an exception if it is not at least the minimum" {
            checkAll<Int, Int> { i1, i2 ->
                val (min, value) = if (i1 < i2) i1 to i2 else i2 to i1
                shouldThrow<InvalidArgumentException> {
                    min.validateAtLeast(value, "Test")
                }
            }
        }
    }
})