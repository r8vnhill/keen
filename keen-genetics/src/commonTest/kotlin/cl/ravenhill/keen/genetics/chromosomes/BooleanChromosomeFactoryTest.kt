package cl.ravenhill.keen.genetics.chromosomes

import arrow.core.left
import cl.ravenhill.keen.exceptions.InitializationException
import cl.ravenhill.matchers.shouldBeLeft
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlin.random.Random

class BooleanChromosomeFactoryTest : FreeSpec({
    "A BooleanChromosomeFactory" - {
        "when creating it" - {
            "should start with the default true rate" {
                val factory = BooleanChromosomeFactory()
                factory.trueRate shouldBe BooleanChromosomeFactory.DEFAULT_TRUE_RATE
            }
        }

        "when setting the true rate" - {
            "should update the true rate" {
                checkAll(Arb.double(includeNonFiniteEdgeCases = false)) { rate ->
                    val factory = BooleanChromosomeFactory().apply {
                        trueRate = rate
                    }
                    factory.trueRate shouldBe rate
                }
            }
        }

        "when creating a chromosome" - {
            "should return an InitializationException if the size is invalid" {
                checkAll(Arb.double(includeNonFiniteEdgeCases = false)) { rate ->
                    val factory = BooleanChromosomeFactory().apply {
                        trueRate = rate
                    }
                    val result = factory.invoke()
                    result
                        .shouldBeLeft()
                        .shouldBeInstanceOf<InitializationException>()
                }
            }
        }
    }
})