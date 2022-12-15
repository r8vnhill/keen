package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.util.math.isNotNan
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

data class BoolChromosomeData(
    val size: Int,
    val truesProbability: Double
)

fun Arb.Companion.boolChromosome(size: Int) = arbitrary {
    val chromosomeSize = Arb.positiveInt(size).bind()
    val truesProbability = Arb.double(0.0, 1.0).bind()
    BoolChromosomeData(chromosomeSize, truesProbability)
}

class BoolChromosomeSpec : WordSpec({
    "Chromosome factory" When {
        "Creating a chromosome with a given size and trues probability" should {
            "Return a chromosome with the given size" {
                checkAll(
                    Arb.positiveInt(100_000),
                    Arb.double(0.0, 1.0)
                ) { size, truesProbability ->
                    assume(truesProbability.isNotNan())
                    assume(truesProbability.isFinite())
                    val chromosome = BoolChromosome.Factory(size, truesProbability).make()
                    chromosome.size shouldBe size
                }
            }
            "Return a chromosome with only true genes if the trues probability is 1.0" {
                checkAll(
                    Arb.positiveInt(100_000)
                ) { size ->
                    val chromosome = BoolChromosome.Factory(size, 1.0).make()
                    chromosome.trues() shouldBe size
                }
            }
            "Return a chromosome with only false genes if the trues probability is 0.0" {
                checkAll(
                    Arb.positiveInt(100_000)
                ) { size ->
                    val chromosome = BoolChromosome.Factory(size, 0.0).make()
                    chromosome.trues() shouldBe 0
                }
            }
        }
    }
    "Verifying" should {
        "Return true" {
            checkChromosome {
                it.verify() shouldBe true
            }
        }
    }
    "Duplicating" should {
        "Return a new chromosome with the same genes" {
            checkChromosome {
                val duplicated = it.duplicate(it.genes)
                duplicated shouldBe it
            }
        }
    }
})

suspend fun checkChromosome(check: suspend (BoolChromosome) -> Unit) {
    checkAll(
        Arb.boolChromosome(100_000)
    ) { data ->
        assume(data.truesProbability.isNotNan())
        assume(data.truesProbability.isFinite())
        val chromosome = BoolChromosome.Factory(data.size, data.truesProbability).make()
        check(chromosome)
    }
}