package cl.ravenhill.keen.genetics.chromosomes

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

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
                    val result = factory.invoke(0, Random())
                    result.isLeft() shouldBe true
                    result.left() shouldBeInstanceOf InitializationException::class
                }
            }
        }
    }
})