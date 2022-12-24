package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Variable
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll


class AddSpec : WordSpec({
    "Reducing an add" should {
        "return the sum of two ephemeral constants" {
            checkAll<Double, Double> { a, b ->
                val add = Add(0)
                add.left = EphemeralConstant(1) { a }
                add.right = EphemeralConstant(1) { b }
                add(arrayOf(b)) shouldBe a + b
            }
        }
        "return the sum of two variables" {
            checkAll<Double, Double> { a, b ->
                val add = Add(0)
                add.left = Variable("a", 0, 1)
                add.right = Variable("b", 1, 1)
                add(arrayOf(a, b)) shouldBe a + b
            }
        }
        "return the sum of an ephemeral constant and a variable" {
            checkAll<Double, Double> { a, b ->
                val add = Add(0)
                add.left = EphemeralConstant(1) { a }
                add.right = Variable("b", 0, 1)
                add(arrayOf(b)) shouldBe a + b
            }
        }
    }
    "Add arity" should {
        "be 2" {
            val add = Add(0)
            add.arity shouldBe 2
        }
    }
    "Flattening an add" should {
        "return a list with the add and its children" {
            val add = Add(0)
            add.left = EphemeralConstant(1) { 1.0 }
            add.right = Add(1)
            (add.right as Add).left = EphemeralConstant(2) { 2.0 }
            (add.right as Add).right = Variable("a", 0, 2)
            add.flatten() shouldBe listOf(
                add,
                add.left,
                add.right,
                (add.right as Add).left,
                (add.right as Add).right
            )
        }
    }
})