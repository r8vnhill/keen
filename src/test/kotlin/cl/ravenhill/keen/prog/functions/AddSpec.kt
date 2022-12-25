package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Variable
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll


class AddSpec : WordSpec({
    afterAny {
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
    }
    "Reducing an add" should {
        "return the sum of two ephemeral constants" {
            checkAll<Double, Double> { a, b ->
                val add = add(EphemeralConstant { a }, EphemeralConstant { b })
                add(arrayOf(b)) shouldBe a + b
            }
        }
        "return the sum of two variables" {
            checkAll<Double, Double> { a, b ->
                val add = add(Variable("a", 0), Variable("b", 1))
                add(arrayOf(a, b)) shouldBe a + b
            }
        }
        "return the sum of an ephemeral constant and a variable" {
            checkAll<Double, Double> { a, b ->
                val add = add(EphemeralConstant { a }, Variable("b", 0))
                add(arrayOf(b)) shouldBe a + b
            }
        }
    }
    "Add arity" should {
        "be 2" {
            val add = Add()
            add.arity shouldBe 2
        }
    }
    "Flattening an add" should {
        "return a list with the add and its children" {
            val add = add(
                EphemeralConstant { 1.0 },
                add(
                    EphemeralConstant { 2.0 },
                    Variable("a", 0)
                )
            )
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