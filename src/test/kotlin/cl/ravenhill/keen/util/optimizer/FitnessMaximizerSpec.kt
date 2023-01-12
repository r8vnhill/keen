package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.phenotype
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.assume
import io.kotest.property.checkAll

class FitnessMaximizerSpec : WordSpec({
    lateinit var optimizer: PhenotypeOptimizer<Double>

    beforeAny {
        optimizer = FitnessMaximizer()
    }

    "Comparing two phenotypes" should {
        "return a negative number if the second phenotype is greater than the first" {
            checkAll(Arb.phenotype(), Arb.phenotype()) { phenotype1, phenotype2 ->
                assume(phenotype1.fitness != phenotype2.fitness)

            }
        }
        "return a positive number if the first phenotype is greater than the second" {
            checkAll(Arb.phenotype(), Arb.phenotype()) { phenotype1, phenotype2 ->
                assume(phenotype1.fitness != phenotype2.fitness)
                optimizer.compare(phenotype1, phenotype2) shouldBeGreaterThan 0
            }
        }
        "return 0 if the phenotypes have the same fitness" {
            checkAll(Arb.phenotype(), Arb.phenotype()) { phenotype1, phenotype2 ->
                val phenotype3 = phenotype2.withFitness(phenotype1.fitness)
                optimizer.compare(phenotype1, phenotype3) shouldBe 0
            }
        }
    }
    "Convert to" When {
        "String" should {
            "Return 'FitnessMaximizer'" {
                FitnessMaximizer<Double>().toString() shouldBe "FitnessMaximizer"
            }
        }
    }
})
