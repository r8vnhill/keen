package cl.ravenhill.keen.prog

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll


class SubSpec : WordSpec({
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
})