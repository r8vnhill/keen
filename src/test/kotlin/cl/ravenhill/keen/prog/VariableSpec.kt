package cl.ravenhill.keen.prog

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class VariableSpec : WordSpec({
    "A variable referencing a value" When {
        "reducing" should {
            "return the value" {
                checkAll(
                    Arb.string(1..10, Codepoint.alphanumeric()),
                    Arb.nonNegativeInt(),
                    Arb.int()
                ) { name, index, value ->
                    val variable = Variable<Int>(name, index)
                    variable.value = Value(value)
                    variable.reduce() shouldBe value
                }
            }
        }
    }
    "A variable referencing another variable" When {
        "reducing" should {
            "return the value of the referenced variable" {
                checkAll(
                    Arb.string(1..10, Codepoint.alphanumeric()),
                    Arb.positiveInt(),
                    Arb.int()
                ) { name, index, value ->
                    val variable = Variable<Int>(name, index)
                    val referencedVariable = Variable<Int>("referenced", 0)
                    referencedVariable.value = Value(value)
                    variable.value = referencedVariable
                    variable.reduce() shouldBe value
                }
            }
        }
    }
})