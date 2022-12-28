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

private fun Arb.Companion.keyval(): Arb<Pair<String, Double>> = arbitrary { rs ->
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
