package cl.ravenhill.keen.prog

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll


class MulSpec : WordSpec({
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
})