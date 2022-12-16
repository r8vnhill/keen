package cl.ravenhill.keen.prog

import cl.ravenhill.keen.util.math.isNotNan
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.haveSameHashCodeAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.assume
import io.kotest.property.checkAll


class ValueSpec : WordSpec({
    "A boolean value" When {
        "checking equality" should {
            "return true if the values are equal" {
                Value(true) shouldBe Value(true)
                Value(false) shouldBe Value(false)
            }
            "return false if the values are not equal" {
                Value(true) shouldNotBe Value(false)
                Value(false) shouldNotBe Value(true)
            }
        }
        "hashing" should {
            "return the same hash for equal values" {
                Value(true) shouldHaveSameHashCodeAs Value(true)
                Value(false) shouldHaveSameHashCodeAs Value(false)
            }
            "return a different hash for different values" {
                Value(true) shouldNot haveSameHashCodeAs(Value(false))
                Value(false) shouldNot haveSameHashCodeAs(Value(true))
            }
            "return a different hash from its underlying value" {
                Value(true) shouldNot haveSameHashCodeAs(true)
                Value(false) shouldNot haveSameHashCodeAs(false)
            }
        }
        "reducing" should {
            "return the value" {
                Value(true).reduce() shouldBe true
                Value(false).reduce() shouldBe false
            }
        }
    }
    "An int value" When {
        "checking equality" should {
            "return true if the values are equal" {
                checkAll<Int> { value ->
                    Value(value) shouldBe Value(value)
                }
            }
            "return false if the values are not equal" {
                checkAll<Int, Int> { value1, value2 ->
                    assume(value1 != value2)
                    Value(value1) shouldNotBe Value(value2)
                }
            }
        }
        "hashing" should {
            "return the same hash for equal values" {
                checkAll<Int> { value ->
                    Value(value) shouldHaveSameHashCodeAs Value(value)
                }
            }
            "return a different hash for different values" {
                checkAll<Int, Int> { value1, value2 ->
                    assume(value1 != value2)
                    Value(value1) shouldNot haveSameHashCodeAs(Value(value2))
                }
            }
            "return a different hash from its underlying value" {
                checkAll<Int> { value ->
                    Value(value) shouldNot haveSameHashCodeAs(value)
                }
            }
        }
        "reducing" should {
            "return the value" {
                checkAll<Int> { value ->
                    Value(value).reduce() shouldBe value
                }
            }
        }
    }
    "A double value" When {
        "checking equality" should {
            "return true if the values are equal" {
                checkAll<Double> { value ->
                    assume(value.isFinite())
                    assume(value.isNotNan())
                    Value(value) shouldBe Value(value)
                }
            }
            "return false if the values are not equal" {
                checkAll<Double, Double> { value1, value2 ->
                    assume(value1 != value2)
                    Value(value1) shouldNotBe Value(value2)
                }
            }
        }
        "hashing" should {
            "return the same hash for equal values" {
                checkAll<Double> { value ->
                    assume(value.isFinite())
                    assume(value.isNotNan())
                    Value(value) shouldHaveSameHashCodeAs Value(value)
                }
            }
            "return a different hash for different values" {
                checkAll<Double, Double> { value1, value2 ->
                    assume(value1 != value2)
                    Value(value1) shouldNot haveSameHashCodeAs(Value(value2))
                }
            }
            "return a different hash from its underlying value" {
                checkAll<Double> { value ->
                    assume(value.isFinite())
                    assume(value.isNotNan())
                    Value(value) shouldNot haveSameHashCodeAs(value)
                }
            }
        }
        "reducing" should {
            "return the value if it is finite and not NaN" {
                checkAll<Double> { value ->
                    assume(value.isNotNan())
                    Value(value).reduce() shouldBe value
                }
            }
            "return the value if it is NaN" {
                Value(Double.NaN).reduce().isNaN() shouldBe true
            }
        }
    }
    "A string value" When {
        "checking equality" should {
            "return true if the values are equal" {
                checkAll<String> { value ->
                    Value(value) shouldBe Value(value)
                }
            }
            "return false if the values are not equal" {
                checkAll<String, String> { value1, value2 ->
                    assume(value1 != value2)
                    Value(value1) shouldNotBe Value(value2)
                }
            }
        }
        "hashing" should {
            "return the same hash for equal values" {
                checkAll<String> { value ->
                    Value(value) shouldHaveSameHashCodeAs Value(value)
                }
            }
            "return a different hash for different values" {
                checkAll<String, String> { value1, value2 ->
                    assume(value1 != value2)
                    Value(value1) shouldNot haveSameHashCodeAs(Value(value2))
                }
            }
            "return a different hash from its underlying value" {
                checkAll<String> { value ->
                    Value(value) shouldNot haveSameHashCodeAs(value)
                }
            }
        }
        "reducing" should {
            "return the value" {
                checkAll<String> { value ->
                    Value(value).reduce() shouldBe value
                }
            }
        }
    }
})
