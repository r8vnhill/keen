package cl.ravenhill.keen.prog

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll


class AddSpec : WordSpec({
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
})