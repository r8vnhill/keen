package cl.ravenhill.keen.prog

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.haveSameHashCodeAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs


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
    }
    "An int value" When {
        "checking equality" should {
            "return true if the values are equal" {
                // TODO: Change with property based testing
                Value(1) shouldBe Value(1)
                Value(2) shouldBe Value(2)
            }
            "return false if the values are not equal" {
                // TODO: Change with property based testing
                Value(1) shouldNotBe Value(2)
                Value(2) shouldNotBe Value(1)
            }
        }
        "hashing" should {
            "return the same hash for equal values" {
                // TODO: Change with property based testing
                Value(1) shouldHaveSameHashCodeAs Value(1)
                Value(2) shouldHaveSameHashCodeAs Value(2)
            }
            "return a different hash for different values" {
                // TODO: Change with property based testing
                Value(1) shouldNot haveSameHashCodeAs(Value(2))
                Value(2) shouldNot haveSameHashCodeAs(Value(1))
            }
            "return a different hash from its underlying value" {
                // TODO: Change with property based testing
                Value(1) shouldNot haveSameHashCodeAs(1)
                Value(2) shouldNot haveSameHashCodeAs(2)
            }
        }
    }
    "A double value" When {
        "checking equality" should {
            "return true if the values are equal" {
                // TODO: Change with property based testing
                Value(1.0) shouldBe Value(1.0)
                Value(2.0) shouldBe Value(2.0)
            }
            "return false if the values are not equal" {
                // TODO: Change with property based testing
                Value(1.0) shouldNotBe Value(2.0)
                Value(2.0) shouldNotBe Value(1.0)
            }
        }
        "hashing" should {
            "return the same hash for equal values" {
                // TODO: Change with property based testing
                Value(1.0) shouldHaveSameHashCodeAs Value(1.0)
                Value(2.0) shouldHaveSameHashCodeAs Value(2.0)
            }
            "return a different hash for different values" {
                // TODO: Change with property based testing
                Value(1.0) shouldNot haveSameHashCodeAs(Value(2.0))
                Value(2.0) shouldNot haveSameHashCodeAs(Value(1.0))
            }
            "return a different hash from its underlying value" {
                // TODO: Change with property based testing
                Value(1.0) shouldNot haveSameHashCodeAs(1.0)
                Value(2.0) shouldNot haveSameHashCodeAs(2.0)
            }
        }
    }
    "A string value" When {
        "checking equality" should {
            "return true if the values are equal" {
                // TODO: Change with property based testing
                Value("1") shouldBe Value("1")
                Value("2") shouldBe Value("2")
            }
            "return false if the values are not equal" {
                // TODO: Change with property based testing
                Value("1") shouldNotBe Value("2")
                Value("2") shouldNotBe Value("1")
            }
        }
        "hashing" should {
            "return the same hash for equal values" {
                // TODO: Change with property based testing
                Value("1") shouldHaveSameHashCodeAs Value("1")
                Value("2") shouldHaveSameHashCodeAs Value("2")
            }
            "return a different hash for different values" {
                // TODO: Change with property based testing
                Value("1") shouldNot haveSameHashCodeAs(Value("2"))
                Value("2") shouldNot haveSameHashCodeAs(Value("1"))
            }
            "return a different hash from its underlying value" {
                // TODO: Change with property based testing
                Value("1") shouldNot haveSameHashCodeAs("1")
                Value("2") shouldNot haveSameHashCodeAs("2")
            }
        }
    }
})
