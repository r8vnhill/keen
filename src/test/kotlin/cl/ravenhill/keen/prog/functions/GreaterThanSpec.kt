package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs


class GreaterThanSpec : WordSpec({
    "Creating a greater than without children" should {
        "create a greater than with ephemeral constants as children" {
            val greaterThan = GreaterThan()
            greaterThan.children.size shouldBe 2
            greaterThan.children[0] shouldBe EphemeralConstant { 0.0 }
            greaterThan.children[1] shouldBe EphemeralConstant { 0.0 }
        }
    }
    "Reducing a greater than operation" should {
        "return 1.0 if the left child is greater than the right child" {
            val greaterThan =
                greaterThan(EphemeralConstant { 1.0 }, EphemeralConstant { 0.0 })
            greaterThan(arrayOf(0.0)) shouldBe 1.0
        }
        "return 0.0 if the left child is less than the right child" {
            val greaterThan =
                greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 1.0 })
            greaterThan(arrayOf(0.0)) shouldBe 0.0
        }
        "return 0.0 if the left child is equal to the right child" {
            val greaterThan =
                greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
            greaterThan(arrayOf(0.0)) shouldBe 0.0
        }
    }
    "Greater than arity" should {
        "be 2" {
            val greaterThan = GreaterThan()
            greaterThan.arity shouldBe 2
        }
    }
    "Flattening a greater than" should {
        "return a list with the greater than and its children" {
            val greaterThan =
                greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
            val flattened = greaterThan.flatten()
            flattened.size shouldBe 3
            flattened[0] shouldBe greaterThan
            flattened[1] shouldBe greaterThan.children[0]
            flattened[2] shouldBe greaterThan.children[1]
        }
    }
    "Copying" When {
        "shallow copying" should {
            "return a new greater than operation with default ephemeral constants" {
                val greaterThan = GreaterThan()
                val copy = greaterThan.copy()
                copy shouldNotBeSameInstanceAs greaterThan
                copy.children.size shouldBe 2
                copy.children[0] shouldBe EphemeralConstant { 0.0 }
                copy.children[1] shouldBe EphemeralConstant { 0.0 }
            }
        }
        "deep copying" should {
            "return a new greater than operation with the same children" {
                val greaterThan = GreaterThan()
                val copy = greaterThan.deepCopy() as GreaterThan
                copy shouldNotBeSameInstanceAs greaterThan
                copy.children.size shouldBe 2
                copy.children[0] shouldBe greaterThan.children[0]
                copy.children[1] shouldBe greaterThan.children[1]
            }
        }
    }
    "Copying a greater than" should {
        "return a new greater than with default ephemeral constants" {
            val greaterThan =
                greaterThan(EphemeralConstant { 1.0 }, EphemeralConstant { 0.0 })
            val copy = greaterThan.copy()
            copy.children[0] shouldBe EphemeralConstant { 0.0 }
            copy.children[1] shouldBe EphemeralConstant { 0.0 }
        }
    }
})