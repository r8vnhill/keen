package cl.ravenhill.keen.prog

import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.arb.arbEnvironment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

@OptIn(ExperimentalKeen::class)
class ReducibleTest : FreeSpec({
    "A Reducible expression" - {
        "should contain itself" {
            checkAll(arbEnvironment(Arb.map(Arb.int(), Arb.int())), arbSumReducible()) { env, reducible ->
                reducible.contents shouldBeSameInstanceAs reducible
            }
        }

        "can be invoked with arguments" {
            checkAll(
                arbEnvironment(Arb.map(Arb.int(), Arb.int())),
                arbReducibleAndArguments()
            ) { env, (reducible, args) ->
                reducible(env, *args.toTypedArray()) shouldBe args.sum()
            }
        }
    }
})

@OptIn(ExperimentalKeen::class)
private fun arbSumReducible(
    arity: Arb<Int> = Arb.int(2..10)
): Arb<Reducible<Int>> = arbitrary {
    val boundArity = arity.bind()
    object : Reducible<Int> {
        override fun invoke(environment: Environment<Int>, args: List<Int>) = args.sum()

        override val arity: Int = boundArity
    }
}

@OptIn(ExperimentalKeen::class)
private fun arbReducibleAndArguments(
    arity: Arb<Int> = Arb.int(2..10)
): Arb<Pair<Reducible<Int>, List<Int>>> = arbitrary {
    val boundArity = arity.bind()
    val args = List(boundArity) { Arb.int().bind() }
    arbSumReducible(arity).bind() to args
}
