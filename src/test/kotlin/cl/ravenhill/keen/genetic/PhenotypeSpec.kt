package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.intChromosomeFactory
import cl.ravenhill.keen.phenotype
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll


class PhenotypeSpec : WordSpec({
    "Comparing" should {
        "return a negative if the fitness of the other individual is greater than this" {
            checkAll(Arb.phenotype(Arb.intChromosomeFactory()), Arb.phenotype(Arb.intChromosomeFactory())) { phenotype1, phenotype2 ->
                assume(phenotype1.fitness != phenotype2.fitness)
                val (p1, p2) = if (phenotype1.fitness < phenotype2.fitness)
                    phenotype1 to phenotype2
                else
                    phenotype2 to phenotype1
                p1.compareTo(p2) shouldBe -1
            }
        }
        "return a positive if the fitness of the other individual is less than this" {
            checkAll(Arb.phenotype(Arb.intChromosomeFactory()), Arb.phenotype(Arb.intChromosomeFactory())) { phenotype1, phenotype2 ->
                assume(phenotype1.fitness != phenotype2.fitness)
                val (p1, p2) = if (phenotype1.fitness < phenotype2.fitness)
                    phenotype1 to phenotype2
                else
                    phenotype2 to phenotype1
                p2.compareTo(p1) shouldBe 1
            }
        }
        "return 0 if the fitness of the other individual is equal to this" {
            checkAll(Arb.phenotype(Arb.intChromosomeFactory()), Arb.phenotype(Arb.intChromosomeFactory())) { phenotype1, phenotype2 ->
                val phenotype3 = phenotype2.withFitness(phenotype1.fitness)
                phenotype1.compareTo(phenotype3) shouldBe 0
            }
        }
    }
    "Duplicating" should {
        "create a new phenotype with the same genotype and generation, but a new fitness" {
            checkAll(Arb.phenotype(Arb.intChromosomeFactory()), Arb.double()) { phenotype, fitness ->
                val newPhenotype = phenotype.withFitness(fitness)
                newPhenotype.genotype shouldBe phenotype.genotype
                newPhenotype.generation shouldBe phenotype.generation
                newPhenotype.fitness shouldBe fitness
            }
        }
    }
    "Flattening" should {
        "return a list with the contents of the genes" {
            checkAll(Arb.phenotype(Arb.intChromosomeFactory())) { phenotype ->
                val flattened = phenotype.flatten()
                flattened shouldBe phenotype.genotype.flatten()
            }
        }
    }
    "Evaluation" When {
        "checking if the phenotype is evaluated" should {
            "be true if the genotype is evaluated" {
                checkAll(Arb.phenotype(Arb.intChromosomeFactory())) { phenotype ->
                    phenotype.isEvaluated() shouldBe true
                }
            }
            "be false if the genotype is not evaluated" {
                checkAll(Arb.phenotype(Arb.intChromosomeFactory())) { phenotype ->
                    phenotype.withFitness(Double.NaN).isEvaluated() shouldBe false
                }
            }
        }
        "checking if a phenotype is not evaluated" should {
            "be true if the genotype is not evaluated" {
                checkAll(Arb.phenotype(Arb.intChromosomeFactory())) { phenotype ->
                    phenotype.withFitness(Double.NaN).isNotEvaluated() shouldBe true
                }
            }
            "be false if the genotype is evaluated" {
                checkAll(Arb.phenotype(Arb.intChromosomeFactory())) { phenotype ->
                    phenotype.isNotEvaluated() shouldBe false
                }
            }
        }
    }
})
