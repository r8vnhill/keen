package cl.ravenhill.keen.prog.terminals

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlin.random.Random


class VariableSpec : WordSpec({
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
    "Variable arity" should {
        "be 0" {
            val variable = Variable<Double>("x", 0)
            variable.arity shouldBe 0
        }
    }
})

private fun Arb.Companion.keyval(): Arb<Pair<String, Double>> = arbitrary { rs ->
    val name = string(codepoints = Codepoint.alphanumeric(), range = 1..10).bind()
    val value = double().bind()
    Pair(name, value)
}
