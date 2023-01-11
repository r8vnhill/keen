package cl.ravenhill.keen.genetic

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll


class PhenotypeSpec : WordSpec({
    "Duplicating" should {
        "create a new phenotype with the same genotype and generation, but a new fitness" {
            checkAll(Arb.phenotype(), Arb.double()) { phenotype, fitness ->
                val newPhenotype = phenotype.withFitness(fitness)
                newPhenotype.genotype shouldBe phenotype.genotype
                newPhenotype.generation shouldBe phenotype.generation
                newPhenotype.fitness shouldBe fitness
            }
        }
    }
    "Flattening" should {
        "return a list with the contents of the genes" {
            checkAll(Arb.phenotype()) { phenotype ->
                val flattened = phenotype.flatten()
                flattened shouldBe phenotype.genotype.flatten()
            }
        }
    }
    "Evaluation" When {
        "checking if the phenotype is evaluated" should {
            "be true if the genotype is evaluated" {
                checkAll(Arb.phenotype()) { phenotype ->
                    phenotype.isEvaluated() shouldBe true
                }
            }
            "be false if the genotype is not evaluated" {
                checkAll(Arb.phenotype()) { phenotype ->
                    phenotype.withFitness(Double.NaN).isEvaluated() shouldBe false
                }
            }
        }
        "checking if a phenotype is not evaluated" should {
            "be true if the genotype is not evaluated" {
                checkAll(Arb.phenotype()) { phenotype ->
                    phenotype.withFitness(Double.NaN).isNotEvaluated() shouldBe true
                }
            }
            "be false if the genotype is evaluated" {
                checkAll(Arb.phenotype()) { phenotype ->
                    phenotype.isNotEvaluated() shouldBe false
                }
            }
        }
    }
})

fun Arb.Companion.phenotype() = arbitrary {
    val genotype = genotype(intChromosomeFactory()).bind()
    val generation = positiveInt().bind()
    val fitness = double().next()
    Phenotype(genotype, generation, fitness)
}