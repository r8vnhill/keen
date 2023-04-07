//package cl.ravenhill.keen.operators.crossover
//
//import cl.ravenhill.keen.Core
//import cl.ravenhill.keen.operators.crossover.pointbased.SingleNodeCrossover
//import cl.ravenhill.keen.probability
//import cl.ravenhill.keen.prog.program
//import cl.ravenhill.keen.prog.terminals.Variable
//import cl.ravenhill.keen.prog.terminals.ephemeralConstant
//import cl.ravenhill.keen.prog.terminals.variable
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.choice
//import io.kotest.property.checkAll
//import kotlin.random.Random
//
//
//class SingleNodeCrossoverTest : FreeSpec({
//    "Crossing two trees with a single node should not change the trees" {
//        checkAll(
//            Arb.choice(Arb.variable(), Arb.ephemeralConstant()),
//            Arb.choice(Arb.variable(), Arb.ephemeralConstant()),
//            Arb.probability()
//        ) { a, b, probability ->
//            val originalA = a.deepCopy()
//            val originalB = b.deepCopy()
//            val crossover = SingleNodeCrossover<Double>(probability)
//            crossover.crossoverTrees(a, b)
//            a shouldBe originalA
//            b shouldBe originalB
//        }
//    }
//
//    "Crossing two trees should not change the trees if the probability is 0" {
//        checkAll(Arb.program(), Arb.program()) { a, b ->
//            val originalA = a.staticCopy()
//            val originalB = b.staticCopy()
//            val crossover = SingleNodeCrossover<Double>(0.0)
//            crossover.crossoverTrees(a, b)
//            a shouldBe originalA
//            b shouldBe originalB
//        }
//    }
//
//    "Crossing two trees should change the trees if the probability is 1 when" - {
//        "the trees have depth 1" {
//            Core.random = Random(420)
//            val crossover = SingleNodeCrossover<Double>(1.0)
//            val treeA = add(
//                Variable("x", 0),
//                Variable("y", 1)
//            )
//            val treeB = add(
//                Variable("a", 0),
//                Variable("b", 1)
//            )
//            crossover.crossoverTrees(treeA, treeB)
//            treeA shouldBe add(
//                Variable("a", 0),
//                Variable("y", 1)
//            )
//            treeB shouldBe add(
//                Variable("x", 0),
//                Variable("b", 1)
//            )
//        }
//        "the trees have depth 2" {
//            Core.random = Random(420)
//            val crossover = SingleNodeCrossover<Double>(1.0)
//            val treeA = add(
//                add(
//                    Variable("x", 0),
//                    Variable("y", 1)
//                ),
//                Variable("z", 2)
//            )
//            val treeB = add(
//                add(
//                    Variable("a", 0),
//                    Variable("b", 1)
//                ),
//                Variable("c", 2)
//            )
//            crossover.crossoverTrees(treeA, treeB)
//            treeA shouldBe add(
//                add(
//                    Variable("a", 0),
//                    Variable("b", 1)
//                ),
//                Variable("z", 2)
//            )
//            treeB shouldBe add(
//                add(
//                    Variable("x", 0),
//                    Variable("y", 1)
//                ),
//                Variable("c", 2)
//            )
//        }
//    }
//})