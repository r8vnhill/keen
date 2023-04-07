//package cl.ravenhill.keen.prog.terminals
//
//import cl.ravenhill.keen.prog.`check that a reduceable should always be created without a parent`
//import cl.ravenhill.keen.prog.functions.Add
//import io.kotest.core.spec.style.WordSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.*
//import io.kotest.property.checkAll
//import kotlin.random.Random
//
//class VariableSpec : WordSpec({
//    "Arity" should {
//        "be 0" {
//            `check that a terminal should always have arity 0`(Arb.variable())
//        }
//    }
//    "Copying" When {
//        "shallow copying" should {
//            "create a copy with the same name and index" {
//                checkCopy(Arb.variable()) { it.copy() as Variable }
//            }
//        }
//        "deep copying" should {
//            "create a copy with the same name" {
//                checkCopy(Arb.variable()) { it.deepCopy() as Variable }
//            }
//        }
//    }
//    "Descendants" When {
//        "created without children" should {
//            "return an empty list" {
//                `check that a terminal should always have an empty list of descendants`(
//                    Arb.variable()
//                )
//            }
//        }
//    }
//    "Flattening" should {
//        "return a list with itself" {
//            `check that a terminal should always flatten to a list with itself`(
//                Arb.variable()
//            )
//        }
//    }
//    "Object identity" When {
//        "equality" should {
//            "be true for the same instance" {
//                `check that an object should always be equal to itself`(Arb.variable())
//            }
//            "be true for two variables with the same name and index" {
//                val variable1 = Variable<Double>("x", 0)
//                val variable2 = Variable<Double>("x", 0)
//                variable1 shouldBe variable2
//            }
//            "be true when comparing a variable with a copy of itself" {
//                `check that an object should always be equal to a copy of itself`(
//                    Arb.variable()
//                )
//            }
//        }
//    }
//    "Parent" When {
//        "creating a variable" should {
//            "be null" {
//                `check that a reduceable should always be created without a parent`(
//                    Arb.variable()
//                )
//            }
//        }
//        "the parent is set" should {
//            "be the set parent" {
//                checkAll(Arb.variable()) { variable ->
//                    val parent = Add()
//                    parent[0] = variable
//                    variable.parent shouldBe parent
//                }
//            }
//        }
//    }
//    "Reducing a variable" should {
//        "return the value of the variable" {
//            checkAll(
//                Arb.list(Arb.keyval(), 1..10),
//                Arb.long()
//            ) { kwargs, seed ->
//                val rng = Random(seed)
//                val kv = kwargs.random(rng)
//                val variable = Variable<Double>(kv.first, kwargs.indexOf(kv))
//                variable.invoke(kwargs.map { it.second }.toTypedArray()) shouldBe kv.second
//            }
//        }
//    }
//    "Size" should {
//        "be 1" {
//            checkAll(Arb.variable()) {
//                it.size shouldBe 1
//            }
//        }
//    }
//})
//
//
//
//
//private fun Arb.Companion.keyval(): Arb<Pair<String, Double>> = arbitrary {
//    val name = string(codepoints = Codepoint.alphanumeric(), range = 1..10).bind()
//    val value = double().bind()
//    Pair(name, value)
//}
//
///**
// * Constructs an arbitrary variable.
// */
//fun Arb.Companion.variable(index: Int = -1) = arbitrary {
//    val sym = Arb.string(1).bind()
//    val i = if (index == -1) Arb.int().bind() else index
//    Variable<Double>(sym, i)
//}
