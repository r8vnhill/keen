package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.Core
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
    "Copying" When {
        "shallow copying" should {
            "return a new add operation with default ephemeral constants" {
                checkAll(Arb.addition()) { add ->
                    val copy = add.copy()
                    copy shouldNotBe add
                    copy.children.size shouldBe 2
                    copy.children[0] shouldBe EphemeralConstant { 0.0 }
                    copy.children[1] shouldBe EphemeralConstant { 0.0 }
                }
            }
        }
        "deep copying" should {
            "return a new add operation with the same children" {
                checkAll(Arb.addition()) { add ->
                    val copy = add.deepCopy() as Add
                    copy shouldNotBeSameInstanceAs add
                    copy.children.size shouldBe 2
                    copy.children[0] shouldBe add.children[0]
                    copy.children[1] shouldBe add.children[1]
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
            val add = Add()
            add.arity shouldBe 2
        }
    }
    "Flattening an add" should {
        "return a list with the add and its children" {
            checkAll(Arb.addition()) { add ->
                val flattened = add.flatten()
                flattened.size shouldBe 5
                flattened[0] shouldBe add
                flattened[1] shouldBe add.children[0]
                flattened[2] shouldBe add.children[1]
                flattened[3] shouldBe (add.children[1] as Add).children[0]
                flattened[4] shouldBe (add.children[1] as Add).children[1]
            }
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
})

data class AddData(val a: Double, val b: Double)

private fun Arb.Companion.addition() =
    arbitrary { rs ->
        val aVal = rs.random.nextDouble()
        val bVal = rs.random.nextDouble()
        val a = EphemeralConstant { aVal }
        val b = EphemeralConstant { bVal }
        val x = Variable<Double>("x", 0)
        add(a, add(b, x))
    }
