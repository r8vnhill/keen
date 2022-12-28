package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Variable
import cl.ravenhill.keen.util.program
import cl.ravenhill.keen.util.subset
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll


class IfSpec : WordSpec({
    "Copying" When {
        "shallow copying" should {
            "return a new if operation with default ephemeral constants" {
                checkAll(Arb.ifThenElse()) { ifExpr ->
                    val copy = ifExpr.copy()
                    copy.children.size shouldBe 3
                    copy shouldBe If()
                }
            }
        }
        "deep copying" should {
            "return a new if operation with the same children" {
                checkAll(Arb.ifThenElse()) { ifExpr ->
                    val copy = ifExpr.deepCopy()
                    copy shouldNotBeSameInstanceAs ifExpr
                    copy shouldBe ifExpr
                }
            }
        }
    }
    "Creating a new if without children" should {
        "create an if with ephemeral constants as children" {
            val ifExpr = If()
            ifExpr.children.size shouldBe 3
            ifExpr.children[0] shouldBe EphemeralConstant { 0.0 }
            ifExpr.children[1] shouldBe EphemeralConstant { 0.0 }
            ifExpr.children[2] shouldBe EphemeralConstant { 0.0 }
        }
    }
    "Object identity" When {
        "equality" should {
            "be true if both objects are the same" {
                checkAll(Arb.ifThenElse()) { ifThenElse ->
                    ifThenElse shouldBe ifThenElse
                }
            }
            "be true if both expressions have the same children" {
                checkAll(Arb.ifThenElse()) { ifThenElse ->
                    val copy = ifThenElse.deepCopy()
                    copy shouldBe ifThenElse
                }
            }
        }
    }
})

/**
 * Generates an if expression.
 */
private fun Arb.Companion.ifThenElse() = arbitrary {
    ifThenElse(Arb.program().bind(), Arb.program().bind(), Arb.program().bind())
}

/**
 * Generates a reduceable expression.
 */
private fun Arb.Companion.program() = arbitrary { rs ->
    val terminals = Arb.terminals().bind()
    val functions = Arb.functions().bind()
    rs.random.program(Core.maxProgramDepth - 1, functions, terminals)
}

/**
 * Generates a list of functions
 */
private fun Arb.Companion.functions() = arbitrary {
    val operations = listOf(Add(), GreaterThan(), If())
    Arb.subset(operations).bind()
}

/**
 * Generates a list of terminals randomly picked.
 */
private fun Arb.Companion.terminals(lo: Int = 0, hi: Int = Int.MAX_VALUE) =
    arbitrary {
        // We generate a set of constants
        val constants = List(Arb.positiveInt(20).bind()) {
            Arb.ephemeralConstant().bind()
        }
        // We generate a set of variables
        val variables = List(Arb.positiveInt(20).bind()) {
            Arb.variable().bind()
        }
        // We select a subset of elements from the constants and variables
        Arb.subset(constants + variables).bind()
    }

/**
 * Generates an arbitrary subset of elements from the given list.
 */
private fun <T> Arb.Companion.subset(collection: List<T>) = arbitrary {
    // We generate a random number of elements to pick
    Arb.positiveInt(collection.size).bind().let {
        // We pick a random subset of `it` elements
        collection.subset(it)
    }
}

/**
 * Constructs an arbitrary ephemeral constant.
 */
private fun Arb.Companion.ephemeralConstant() = arbitrary {
    val v = Arb.double().bind()
    EphemeralConstant { v }
}

/**
 * Constructs an arbitrary variable.
 */
private fun Arb.Companion.variable() = arbitrary {
    val sym = Arb.string(1).bind()
    val i = Arb.nonNegativeInt().bind()
    Variable<Double>(sym, i)
}
