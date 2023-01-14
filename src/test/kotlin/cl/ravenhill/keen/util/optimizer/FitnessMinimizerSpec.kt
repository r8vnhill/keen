package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.intChromosomeFactory
import cl.ravenhill.keen.phenotype
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.assume
import io.kotest.property.checkAll


class FitnessMinimizerSpec : WordSpec({
    lateinit var optimizer: PhenotypeOptimizer<Int>

    beforeAny {
        optimizer = FitnessMinimizer()
    }

    "Comparing two phenotypes" should {
        "return a negative number if the first phenotype is greater than the second" {
            checkAll(
                Arb.phenotype(Arb.intChromosomeFactory()),
                Arb.phenotype(Arb.intChromosomeFactory())
            ) { phenotype1, phenotype2 ->
                assume(phenotype1.fitness != phenotype2.fitness)
                val (p1, p2) = if (phenotype1.fitness > phenotype2.fitness)
                    phenotype1 to phenotype2
                else
                    phenotype2 to phenotype1
                optimizer.compare(p1, p2) shouldBeLessThan 0
            }
        }
        "return a positive number if the second phenotype is greater than the first" {
            checkAll(
                Arb.phenotype(Arb.intChromosomeFactory()),
                Arb.phenotype(Arb.intChromosomeFactory())
            ) { phenotype1, phenotype2 ->
                assume(phenotype1.fitness != phenotype2.fitness)
                val (p1, p2) = if (phenotype1.fitness < phenotype2.fitness)
                    phenotype1 to phenotype2
                else
                    phenotype2 to phenotype1
                optimizer.compare(p1, p2) shouldBeGreaterThan 0
            }
        }
        "return 0 if the phenotypes have the same fitness" {
            checkAll(
                Arb.phenotype(Arb.intChromosomeFactory()),
                Arb.phenotype(Arb.intChromosomeFactory())
            ) { phenotype1, phenotype2 ->
                val phenotype3 = phenotype2.withFitness(phenotype1.fitness)
                optimizer.compare(phenotype1, phenotype3) shouldBe 0
            }
        }
    }
    "Convert to" When {
        "String" should {
            "Return 'FitnessMinimizer'" {
                FitnessMinimizer<Int>().toString() shouldBe "FitnessMinimizer"
            }
        }
    }
    "Sorting" should {
        "sort the phenotypes in ascending order" {
            checkAll(
                Arb.phenotype(Arb.intChromosomeFactory()),
                Arb.phenotype(Arb.intChromosomeFactory()),
                Arb.phenotype(Arb.intChromosomeFactory())
            ) { phenotype1, phenotype2, phenotype3 ->
                val (p1, p2, p3) = listOf(
                    phenotype1,
                    phenotype2,
                    phenotype3
                ).sortedBy { it.fitness }
                val sorted = optimizer.sort(listOf(p3, p1, p2))
                sorted shouldBe listOf(p1, p2, p3)
            }
        }
    }
    include(`a comparator can be created from the optimizer`(FitnessMinimizer()))
    include(`invoking should be consistent with compare`(FitnessMinimizer()))
})