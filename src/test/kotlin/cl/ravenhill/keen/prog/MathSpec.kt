package cl.ravenhill.keen.prog

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import kotlin.math.pow


class MathSpec : WordSpec({
    "Addition" When {
        "reducing" should {
            "return the sum of the inputs" {
                checkAll<Double, Double> { a, b ->
                    val add = Add()
                    add.left = Value(a)
                    add.right = Value(b)
                    add.reduce() shouldBe a + b
                }
            }
        }
    }
    "Subtraction" When {
        "reducing" should {
            "return the difference of the inputs" {
                checkAll<Double, Double> { a, b ->
                    val sub = Sub()
                    sub.left = Value(a)
                    sub.right = Value(b)
                    sub.reduce() shouldBe a - b
                }
            }
        }
    }
    "Multiplication" When {
        "reducing" should {
            "return the product of the inputs" {
                checkAll<Double, Double> { a, b ->
                    val mul = Mul()
                    mul.left = Value(a)
                    mul.right = Value(b)
                    mul.reduce() shouldBe a * b
                }
            }
        }
    }
    "Division" When {
        "reducing" should {
            "return the quotient of the inputs" {
                checkAll<Double, Double> { a, b ->
                    val div = Div()
                    div.left = Value(a)
                    div.right = Value(b)
                    div.reduce() shouldBe a / b
                }
            }
        }
    }
    "Modulo" When {
        "reducing" should {
            "return the remainder of the inputs" {
                checkAll<Double, Double> { a, b ->
                    val mod = Mod()
                    mod.left = Value(a)
                    mod.right = Value(b)
                    mod.reduce() shouldBe a % b
                }
            }
        }
    }
    "Power" When {
        "reducing" should {
            "return the power of the inputs" {
                checkAll<Double, Double> { a, b ->
                    val pow = Pow()
                    pow.left = Value(a)
                    pow.right = Value(b)
                    pow.reduce() shouldBe a.pow(b)
                }
            }
        }
    }
})