//package cl.ravenhill.keen.prog.functions
//
//import cl.ravenhill.keen.prog.program
//import cl.ravenhill.keen.prog.terminals.EphemeralConstant
//import cl.ravenhill.keen.prog.terminals.ephemeralConstant
//import cl.ravenhill.keen.prog.terminals.variable
//import io.kotest.core.spec.style.WordSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.shouldNot
//import io.kotest.matchers.shouldNotBe
//import io.kotest.matchers.types.haveSameHashCodeAs
//import io.kotest.matchers.types.shouldHaveSameHashCodeAs
//import io.kotest.matchers.types.shouldNotBeSameInstanceAs
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.arbitrary
//import io.kotest.property.assume
//import io.kotest.property.checkAll
//
//
//class MulSpec : WordSpec({
//    "Arity" should {
//        "be 2" {
//            checkAll(Arb.multiplication()) { mul ->
//                mul.arity shouldBe 2
//            }
//        }
//    }
//    "Copying" When {
//        "shallow copying" should {
//            "return a new mul operation with default ephemeral constants" {
//                val mul = Mul()
//                val copy = mul.copy()
//                copy shouldBe Mul()
//            }
//        }
//        "deep copying" should {
//            "return a new mul operation with the multiplication of two terminals" {
//                val mul = mul(EphemeralConstant { 1.0 }, EphemeralConstant { 2.0 })
//                val copy = mul.deepCopy()
//                copy shouldBe mul
//            }
//            "return a new mul operation with the multiplication of two functions" {
//                val mul = mul(Mul(), Mul())
//                val copy = mul.deepCopy()
//                copy shouldBe mul
//            }
//            "return a new operation with the same children" {
//                checkAll(Arb.multiplication()) { mul ->
//                    val copy = mul.deepCopy() as Mul
//                    copy shouldNotBeSameInstanceAs mul
//                    copy shouldBe mul
//                }
//            }
//        }
//    }
//    "Creating a mul operation without children" should {
//        "return a mul operation with two ephemeral constants" {
//            val mul = Mul()
//            mul shouldBe mul(
//                EphemeralConstant { 1.0 },
//                EphemeralConstant { 1.0 })
//        }
//    }
//    "Descendants" When {
//        "created without children" should {
//            "return two ephemeral constants" {
//                val mul = Mul()
//                mul.descendants shouldBe listOf(
//                    EphemeralConstant { 1.0 },
//                    EphemeralConstant { 1.0 })
//            }
//        }
//        "created with children" should {
//            "return the children" {
//                val mul = mul(
//                    EphemeralConstant { 0.0 },
//                    mul(EphemeralConstant { 0.0 },
//                        EphemeralConstant { 0.0 })
//                )
//                mul.descendants shouldBe listOf(
//                    EphemeralConstant { 0.0 },
//                    mul(EphemeralConstant { 0.0 },
//                        EphemeralConstant { 0.0 }),
//                    EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 })
//            }
//        }
//    }
//    "Equality" should {
//        "be reflexive" {
//            checkAll(Arb.multiplication()) { mul ->
//                mul shouldBe mul
//            }
//        }
//        "be true for two mul operations with the same children" {
//            checkAll(Arb.multiplication()) { mul ->
//                val other = mul(
//                    mul.children[0].deepCopy(),
//                    mul.children[1].deepCopy()
//                )
//                mul shouldBe other
//            }
//        }
//        "be false for two mul operations with different children" {
//            checkAll(Arb.multiplication(), Arb.multiplication()) { mul1, mul2 ->
//                assume(mul1.children != mul2.children)
//                mul1 shouldNotBe mul2
//            }
//        }
//    }
//    "Hashing" should {
//        "be the same if the children are the same" {
//            checkAll(Arb.multiplication()) { mul ->
//                val other = mul(
//                    mul.children[0].deepCopy(),
//                    mul.children[1].deepCopy()
//                )
//                mul shouldHaveSameHashCodeAs other
//            }
//        }
//        "be different if the children are different" {
//            checkAll(Arb.multiplication(), Arb.multiplication()) { mul1, mul2 ->
//                assume(mul1.children != mul2.children)
//                mul1 shouldNot haveSameHashCodeAs(mul2)
//            }
//        }
//    }
//    "Flattening" should {
//        "return a list with the operation and its children" {
//            val mul = mul(
//                mul(EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 }),
//                EphemeralConstant { 0.0 })
//            val flattened = mul.flatten()
//            flattened.size shouldBe 5
//            flattened shouldBe listOf(
//                mul,
//                mul(EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 }),
//                EphemeralConstant { 0.0 },
//                EphemeralConstant { 0.0 },
//                EphemeralConstant { 0.0 })
//        }
//    }
//    "Reducing" should {
//        "return the multiplication of two constants" {
//            checkAll(Arb.ephemeralConstant(), Arb.ephemeralConstant()) { a, b ->
//                val mul = mul(a, b)
//                mul(arrayOf()) shouldBe a.invoke(arrayOf()) * b.invoke(arrayOf())
//            }
//        }
//        "return the multiplication of two variables" {
//            checkAll(Arb.variable(0), Arb.variable(1)) { a, b ->
//                val mul = mul(a, b)
//                mul(arrayOf(1.0, 2.0)) shouldBe 2.0
//            }
//        }
//    }
//    "Size" should {
//        "be 3 if the children are ephemeral constants" {
//            checkAll(Arb.ephemeralConstant(), Arb.ephemeralConstant()) { a, b ->
//                val mul = mul(a, b)
//                mul.size shouldBe 3
//            }
//        }
//        "be 1 plus the sum of the children's sizes" {
//            checkAll(Arb.multiplication()) { mul ->
//                val size = 1 + mul.children[0].size + mul.children[1].size
//                mul.size shouldBe size
//            }
//        }
//    }
//})
//
//fun Arb.Companion.multiplication() = arbitrary {
//    mul(Arb.program().bind(), Arb.program().bind())
//}
