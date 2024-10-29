package cl.ravenhill.keen.genetics.genes

import cl.ravenhill.utils.arbRandomPair
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class DoubleGeneTest : FreeSpec({
    "A DoubleGene" - {
        "when creating it" - {
            "with the primary constructor" - {
                "should have the correct value" {
                    checkAll(Arb.double(includeNonFiniteEdgeCases = false)) { value ->
                        val gene = DoubleGene(value)
                        gene.value shouldBe value
                    }
                }
            }

            "with the pure function" - {
                "should have the correct value" {
                    checkAll(Arb.double(includeNonFiniteEdgeCases = false)) { value ->
                        val gene = DoubleGene.pure(value)
                        gene.value shouldBe value
                    }
                }
            }
        }

        "when copying with a new value" - {
            "should create a new gene with the provided value" {
                checkAll(arbDoubleGene(), Arb.double(includeNonFiniteEdgeCases = false)) { gene, value ->
                    gene.copyWithValue(value) shouldBe DoubleGene(value)
                }
            }
        }

        "when generating a random value" - {
            "should generate a random double with a given random generator" {
                checkAll(arbDoubleGene(), arbRandomPair()) { gene, (r1, r2) ->
                    gene.generator(r1) shouldBe r2.nextDouble()
                }
            }
        }
    }
})

private fun arbDoubleGene(arbDouble: Arb<Double> = Arb.double(includeNonFiniteEdgeCases = false)) = arbDouble.map {
    DoubleGene(it)
}