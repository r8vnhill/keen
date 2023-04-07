//package cl.ravenhill.keen.prog.functions
//
//import cl.ravenhill.keen.prog.program
//import cl.ravenhill.keen.prog.terminals.EphemeralConstant
//import io.kotest.core.spec.style.WordSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.shouldNot
//import io.kotest.matchers.shouldNotBe
//import io.kotest.matchers.types.haveSameHashCodeAs
//import io.kotest.matchers.types.shouldHaveSameHashCodeAs
//import io.kotest.matchers.types.shouldNotBeSameInstanceAs
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.arbitrary
//import io.kotest.property.checkAll
//
//
//class GreaterThanSpec : WordSpec({
//    "Arity" should {
//        "be 2" {
//            checkAll(Arb.greaterThan()) { greaterThan ->
//                greaterThan.arity shouldBe 2
//            }
//        }
//    }
//    "Accessing the descendants" should {
//        "return two ephemeral constants if it was created without children" {
//            val greaterThan = GreaterThan()
//            greaterThan.descendants shouldBe listOf(
//                EphemeralConstant { 0.0 },
//                EphemeralConstant { 0.0 })
//        }
//        "return the children if it was created with children" {
//            val greaterThan = greaterThan(
//                EphemeralConstant { 0.0 },
//                greaterThan(
//                    EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 }
//                ))
//            greaterThan.descendants shouldBe listOf(
//                EphemeralConstant { 0.0 },
//                greaterThan(
//                    EphemeralConstant { 0.0 },
//                    EphemeralConstant { 0.0 }),
//                EphemeralConstant { 0.0 },
//                EphemeralConstant { 0.0 })
//        }
//    }
//    "Copying" When {
//        "shallow copying" should {
//            "return a new greater than operation with default ephemeral constants" {
//                val greaterThan = GreaterThan()
//                val copy = greaterThan.copy()
//                copy shouldNotBeSameInstanceAs greaterThan
//                copy.children.size shouldBe 2
//                copy.children[0] shouldBe EphemeralConstant { 0.0 }
//                copy.children[1] shouldBe EphemeralConstant { 0.0 }
//            }
//        }
//        "deep copying" should {
//            "return a new greater than operation with two terminals" {
//                val greaterThan =
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                val copy = greaterThan.deepCopy()
//                copy shouldNotBeSameInstanceAs greaterThan
//                copy shouldBe greaterThan
//            }
//            "return a new greater than operation with the same children" {
//                checkAll(Arb.greaterThan()) { greaterThan ->
//                    val copy = greaterThan.deepCopy() as GreaterThan
//                    copy shouldNotBeSameInstanceAs greaterThan
//                    copy shouldBe greaterThan
//                }
//            }
//        }
//    }
//    "Creating" When {
//        "creating a greater than operation with two terminals" should {
//            "create a greater than operation with the terminals as children" {
//                val greaterThan =
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                greaterThan.children.size shouldBe 2
//                greaterThan.children[0] shouldBe EphemeralConstant { 0.0 }
//                greaterThan.children[1] shouldBe EphemeralConstant { 0.0 }
//            }
//        }
//        "creating a greater than operation with two functions" should {
//            "create a greater than operation with the functions as children" {
//                val greaterThan = greaterThan(Add(), Add())
//                greaterThan.children.size shouldBe 2
//                greaterThan.children[0] shouldBe Add()
//                greaterThan.children[1] shouldBe Add()
//            }
//        }
//    }
//    "Flattening a greater than" should {
//        "return a list with the greater than and its children" {
//            val greaterThan =
//                greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//            val flattened = greaterThan.flatten()
//            flattened.size shouldBe 3
//            flattened[0] shouldBe greaterThan
//            flattened[1] shouldBe greaterThan.children[0]
//            flattened[2] shouldBe greaterThan.children[1]
//        }
//    }
//    "Reducing a greater than operation" should {
//        "return 1.0 if the left child is greater than the right child" {
//            val greaterThan =
//                greaterThan(EphemeralConstant { 1.0 }, EphemeralConstant { 0.0 })
//            greaterThan(arrayOf(0.0)) shouldBe 1.0
//        }
//        "return 0.0 if the left child is less than the right child" {
//            val greaterThan =
//                greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 1.0 })
//            greaterThan(arrayOf(0.0)) shouldBe 0.0
//        }
//        "return 0.0 if the left child is equal to the right child" {
//            val greaterThan =
//                greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//            greaterThan(arrayOf(0.0)) shouldBe 0.0
//        }
//    }
//    "Object identity" When {
//        "equality" should {
//            "be true if the two greater than operations are the same" {
//                checkAll(Arb.greaterThan()) { greaterThan ->
//                    greaterThan shouldBe greaterThan
//                }
//            }
//            "be true if they have the same children" {
//                val greaterThan = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                )
//                val other = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                )
//                greaterThan shouldBe other
//            }
//            "be false if they have different children" {
//                val greaterThan = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                )
//                val other = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 1.0 }, EphemeralConstant { 0.0 })
//                )
//                greaterThan shouldNotBe other
//            }
//        }
//        "hashing" should {
//            "be the same if the two greater than operations are the same" {
//                checkAll(Arb.greaterThan()) { greaterThan ->
//                    greaterThan shouldHaveSameHashCodeAs greaterThan
//                }
//            }
//            "be the same if they have the same children" {
//                val greaterThan = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                )
//                val other = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                )
//                greaterThan shouldHaveSameHashCodeAs other
//            }
//            "be different if they have different children" {
//                val greaterThan = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 })
//                )
//                val other = greaterThan(
//                    greaterThan(EphemeralConstant { 0.0 }, EphemeralConstant { 0.0 }),
//                    greaterThan(EphemeralConstant { 1.0 }, EphemeralConstant { 0.0 })
//                )
//                greaterThan shouldNot haveSameHashCodeAs(other)
//            }
//        }
//    }
//    "Size" When {
//        "accessing the size" should {
//            "return the number of nodes in the greater than operation" {
//                checkAll(Arb.greaterThan()) { greaterThan ->
//                    greaterThan.size shouldBe greaterThan.flatten().size
//                }
//            }
//        }
//    }
//})
//
///**
// * Generates an arbitrary greater than operation.
// */
//fun Arb.Companion.greaterThan() = arbitrary {
//    greaterThan(Arb.program().bind(), Arb.program().bind())
//}