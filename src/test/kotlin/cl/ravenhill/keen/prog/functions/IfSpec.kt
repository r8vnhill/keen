//package cl.ravenhill.keen.prog.functions
//
//import cl.ravenhill.keen.prog.program
//import cl.ravenhill.keen.prog.terminals.EphemeralConstant
//import cl.ravenhill.keen.prog.terminals.Variable
//import io.kotest.core.spec.style.WordSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.types.shouldNotBeSameInstanceAs
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.arbitrary
//import io.kotest.property.checkAll
//
//
//class IfSpec : WordSpec({
//    "Arity" should {
//        "be 3" {
//            checkAll(Arb.ifThenElse()) { ifExpr ->
//                ifExpr.arity shouldBe 3
//            }
//        }
//    }
//    "Descendants" When {
//        "created without children" should {
//            "return three ephemeral constants" {
//                val ifExpr = If()
//                ifExpr.descendants shouldBe listOf(
//                    EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 })
//            }
//        }
//        "created with children" should {
//            "return the children" {
//                val ifExpr = ifThenElse(
//                    EphemeralConstant { 0.0 },
//                    greaterThan(
//                        EphemeralConstant { 0.0 },
//                        EphemeralConstant { 0.0 }),
//                    EphemeralConstant { 0.0 })
//                ifExpr.descendants shouldBe listOf(
//                    EphemeralConstant { 0.0 },
//                    greaterThan(
//                        EphemeralConstant { 0.0 },
//                        EphemeralConstant { 0.0 }),
//                    EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 })
//            }
//        }
//    }
//    "Copying" When {
//        "shallow copying" should {
//            "return a new if operation with default ephemeral constants" {
//                checkAll(Arb.ifThenElse()) { ifExpr ->
//                    val copy = ifExpr.copy()
//                    copy shouldBe If()
//                }
//            }
//        }
//        "deep copying" should {
//            "return a new if operation with a greater than, a variable and an ephemeral constant" {
//                val ifExpr = ifThenElse(GreaterThan(), Variable("x", 0), EphemeralConstant { 1.0 })
//                val copy = ifExpr.deepCopy()
//                copy shouldNotBeSameInstanceAs ifExpr
//                copy shouldBe ifExpr
//            }
//            "return a new if operation with the same children" {
//                checkAll(Arb.ifThenElse()) { ifExpr ->
//                    val copy = ifExpr.deepCopy()
//                    copy shouldNotBeSameInstanceAs ifExpr
//                    copy shouldBe ifExpr
//                }
//            }
//        }
//    }
//    "Creating a new if without children" should {
//        "create an if with ephemeral constants as children" {
//            val ifExpr = If()
//            ifExpr.children.size shouldBe 3
//            ifExpr.children[0] shouldBe EphemeralConstant { 0.0 }
//            ifExpr.children[1] shouldBe EphemeralConstant { 0.0 }
//            ifExpr.children[2] shouldBe EphemeralConstant { 0.0 }
//        }
//    }
//    "Object identity" When {
//        "equality" should {
//            "be true if both objects are the same" {
//                checkAll(Arb.ifThenElse()) { ifThenElse ->
//                    ifThenElse shouldBe ifThenElse
//                }
//            }
//            "be true if both expressions have the same children" {
//                checkAll(Arb.ifThenElse()) { ifThenElse ->
//                    val copy = ifThenElse.deepCopy()
//                    copy.children shouldBe ifThenElse.children
//                }
//            }
//        }
//    }
//    "Size" When {
//        "created with default ephemeral constants" should {
//            "return 4" {
//                val ifExpr = If()
//                ifExpr.size shouldBe 4
//            }
//        }
//        "created with children" should {
//            "return the sum of the children sizes plus one" {
//                val ifExpr1 = ifThenElse(
//                    EphemeralConstant { 0.0 },
//                    greaterThan(
//                        EphemeralConstant { 0.0 },
//                        EphemeralConstant { 0.0 }),
//                    EphemeralConstant { 0.0 })
//                ifExpr1.size shouldBe 6
//                // Consistency check
//                checkAll(Arb.ifThenElse()) { ifExpr ->
//                    ifExpr.size shouldBe ifExpr.children.sumOf { it.size } + 1
//                }
//            }
//        }
//    }
//})
//
///**
// * Generates an if expression.
// */
//fun Arb.Companion.ifThenElse() = arbitrary {
//    ifThenElse(Arb.program().bind(), Arb.program().bind(), Arb.program().bind())
//}
