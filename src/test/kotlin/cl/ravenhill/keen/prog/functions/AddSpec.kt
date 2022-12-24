package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll


class AddSpec : WordSpec({
    "Reducing an add" should {
        "return the sum of the two arguments" {
            checkAll<Double, Double> { a, b ->
                val add = Add()
                add.left = EphemeralConstant { a }
                add.right = EphemeralConstant { b }
                add(arrayOf()) shouldBe a + b
            }
        }
    }
    "Add arity" should {
        "be 2" {
            val add = Add()
            add.arity shouldBe 2
        }
    }
})