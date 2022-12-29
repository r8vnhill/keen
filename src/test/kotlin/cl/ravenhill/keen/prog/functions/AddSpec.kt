package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.program
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Variable
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.haveSameHashCodeAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll


class AddSpec : WordSpec({
    afterAny {
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
    }
    "Accessing the descendants" should {
        "return two ephemeral constants if it was created without children" {
            val add = Add()
            add.descendants shouldBe listOf(
                EphemeralConstant { 0.0 },
                EphemeralConstant { 0.0 })
        }
        "return the children if it was created with children" {
            val add = add(
                EphemeralConstant { 0.0 },
                add(EphemeralConstant { 0.0 },
                    EphemeralConstant { 0.0 })
            )
            add.descendants shouldBe listOf(
                EphemeralConstant { 0.0 },
                add(EphemeralConstant { 0.0 },
                    EphemeralConstant { 0.0 }),
                EphemeralConstant { 0.0 },
                EphemeralConstant { 0.0 })
        }
    }
    "Copying" When {
        "shallow copying" should {
            "return a new add operation with default ephemeral constants" {
                checkAll(Arb.addition()) { add ->
                    val copy = add.copy()
                    copy shouldNotBe add
                    copy shouldBe Add()
                }
            }
        }
        "deep copying" should {
            "return a new add operation with the addition of two terminals" {
                val add = add(EphemeralConstant { 1.0 }, Variable("x", 0))
                val copy = add.deepCopy()
                copy shouldNotBeSameInstanceAs add
                copy shouldBe add
            }
            "return a new add operation with the addition of two functions" {
                val add = add(Add(), Add())
                val copy = add.deepCopy()
                copy shouldNotBeSameInstanceAs add
                copy shouldBe add
            }
            "return a new add operation with the same children" {
                checkAll(Arb.addition()) { add ->
                    val copy = add.deepCopy() as Add
                    copy shouldNotBeSameInstanceAs add
                    copy shouldBe add
                }
            }
        }
    }
    "Creating an addition without children" should {
        "create an addition with ephemeral constants as children" {
            val add = Add()
            add.children.size shouldBe 2
            add.children[0] shouldBe EphemeralConstant { 0.0 }
            add.children[1] shouldBe EphemeralConstant { 0.0 }
        }
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
            checkAll(Arb.addition()) { add ->
                add.arity shouldBe 2
            }
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
            val flattened = add.flatten()
            flattened.size shouldBe 5
            flattened[0] shouldBe add
            flattened[1] shouldBe add.children[0]
            flattened[2] shouldBe add.children[1]
            flattened[3] shouldBe (add.children[1] as Add).children[0]
            flattened[4] shouldBe (add.children[1] as Add).children[1]
        }
    }
    "Object identity" When {
        "comparing equality" should {
            "be true if the objects are the same" {
                checkAll(Arb.addition()) { add ->
                    add shouldBe add
                }
            }
            "be true if the objects have the same children" {
                checkAll(Arb.addition()) { add ->
                    val other = Add()
                    other[0] = add.children[0]
                    other[1] = add.children[1]
                    add shouldBe other
                }
            }
            "be different if they have different children" {
                val add = add(
                    EphemeralConstant { 1.0 },
                    add(
                        EphemeralConstant { 2.0 },
                        Variable("a", 0)
                    )
                )
                val copy = add.copy()
                add shouldNotBe copy
            }
        }
        "hashing" should {
            "be the same if they have the same children" {
                checkAll(Arb.addition()) { add ->
                    val other = Add()
                    other[0] = add.children[0]
                    other[1] = add.children[1]
                    add shouldHaveSameHashCodeAs other
                }
            }
            "be different if they have different children" {
                val add = add(
                    EphemeralConstant { 1.0 },
                    add(
                        EphemeralConstant { 2.0 },
                        Variable("a", 0)
                    )
                )
                val copy = add.copy()
                add shouldNot haveSameHashCodeAs(copy)
            }
        }
    }
    "Size" should {
        "be 3 if the add has ephemeral constants as children" {
            val add = Add()
            add.size shouldBe 3
        }
        "be 1 plus the size of the children" {
            val add = add(
                EphemeralConstant { 1.0 },
                add(
                    EphemeralConstant { 2.0 },
                    Variable("a", 0)
                )
            )
            add.size shouldBe 5
        }
    }
})

private fun Arb.Companion.addition() = arbitrary {
    add(Arb.program().bind(), Arb.program().bind())
}
