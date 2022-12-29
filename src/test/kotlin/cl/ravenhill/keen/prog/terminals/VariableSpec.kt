package cl.ravenhill.keen.prog.terminals

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlin.random.Random

class VariableSpec : WordSpec({
    "Arity" should {
        "be 0" {
            checkAll(Arb.variable()) {
                it.arity shouldBe 0
            }
        }
    }
    "Copying" When {
        "shallow copying" should {
            "create a copy with the same name and index" {
                checkCopy(Arb.variable()) { it.copy() as Variable }
            }
        }
        "deep copying" should {
            "create a copy with the same name" {
                checkCopy(Arb.variable()) { it.deepCopy() as Variable }
            }
        }
    }
    "Flattening" should {
        "return a list with itself" {
            checkAll(Arb.variable()) {
                it.flatten() shouldBe listOf(it)
            }
        }
    }
    "Object identity" When {
        "equality" should {
            "be true for the same instance" {
                `check that an object should always be equal to itself`(Arb.variable())
            }
            "be true for two variables with the same name and index" {
                val variable1 = Variable<Double>("x", 0)
                val variable2 = Variable<Double>("x", 0)
                variable1 shouldBe variable2
            }
            "be true when comparing a variable with a copy of itself" {

            }
        }
    }
    "Reducing a variable" should {
        "return the value of the variable" {
            checkAll(
                Arb.list(Arb.keyval(), 1..10),
                Arb.long()
            ) { kwargs, seed ->
                val rng = Random(seed)
                val kv = kwargs.random(rng)
                val variable = Variable<Double>(kv.first, kwargs.indexOf(kv))
                variable(kwargs.map { it.second }.toTypedArray()) shouldBe kv.second
            }
        }
    }
})

private fun Arb.Companion.keyval(): Arb<Pair<String, Double>> = arbitrary {
    val name = string(codepoints = Codepoint.alphanumeric(), range = 1..10).bind()
    val value = double().bind()
    Pair(name, value)
}

/**
 * Constructs an arbitrary variable.
 */
fun Arb.Companion.variable() = arbitrary {
    val sym = Arb.string(1).bind()
    val i = Arb.nonNegativeInt().bind()
    Variable<Double>(sym, i)
}
