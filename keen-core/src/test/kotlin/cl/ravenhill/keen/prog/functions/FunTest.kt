package cl.ravenhill.keen.prog.functions

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.annotations.ExperimentalKeen
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.AstException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

@OptIn(ExperimentalKeen::class)
class FunTest : FreeSpec({

    "When constructing" - {
        "throws an exception if the arity is negative" {
            checkAll(
                Arb.string(),
                Arb.negativeInt()
            ) { name, arity ->
                shouldThrow<CompositeException> {
                    Fun(name, arity) {}
                }.shouldHaveInfringement<AstException>(
                    "The arity ($arity) must be greater than or equal to 0"
                )
            }
        }

        "with valid parameters should initialize correctly" {
            checkAll(
                Arb.string(),
                Arb.nonNegativeInt(),
            ) { name, arity ->
                val fn = Fun(name, arity) {}
                fn.name shouldBe name
                fn.arity shouldBe arity
            }
        }
    }
})
