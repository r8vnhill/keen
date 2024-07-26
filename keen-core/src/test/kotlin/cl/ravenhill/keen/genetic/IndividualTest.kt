/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.arb.datatypes.arbNonNaNDouble
import cl.ravenhill.keen.arb.genetic.*
import cl.ravenhill.keen.arb.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.assertions.should.shouldHaveFitness
import cl.ravenhill.keen.fitness
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({

    "An Individual" - {
        "should have a genotype property that is set according to the constructor" {
            checkAll(genotype(), Arb.double()) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.genotype shouldBe genotype
            }
        }

        "should have a fitness property that is set according to the constructor" {
            checkAll(genotype(), arbNonNaNDouble()) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual shouldHaveFitness fitness
            }
        }

        "should have a size property that is equal to the size of the genotype" {
            checkAll(genotype(), Arb.double()) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.size shouldBe genotype.size
            }
        }

        "when verifying" - {
            "should return true if the genotype is valid and the fitness is not NaN" {
                checkAll(arbIndividual(arbValidGenotype())) { individual ->
                    individual.verify() shouldBe true
                }
            }

            "should return false if the genotype is invalid" {
                checkAll(arbIndividual(arbInvalidGenotype())) { individual ->
                    individual.verify().shouldBeFalse()
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(arbIndividual(genotype(), Arb.constant(Double.NaN))) { individual ->
                    individual.verify() shouldBe false
                }
            }
        }

        "can be flat-mapped" {
            checkAll(individual()) { individual ->
                val flatMapped = individual.flatten()
                flatMapped.size shouldBe individual.genotype.sumOf { it.size }
                flatMapped shouldBe individual.genotype.flatten()
            }
        }

        "when checking if the individual is evaluated" - {
            "should return true if the fitness is not NaN" {
                checkAll(arbIndividual(genotype(), arbNonNaNDouble())) { individual ->
                    individual.isEvaluated() shouldBe true
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(arbIndividual(genotype(), Arb.constant(Double.NaN))) { individual ->
                    individual.isEvaluated() shouldBe false
                }
            }
        }
    }

    "A Population of Individuals" - {
        "should have a fitness property that is equal to the list of fitness values of the individuals" {
            checkAll(arbPopulation(individual())) { population ->
                population.fitness shouldBe population.map { it.fitness }
            }
        }
    }
})

private fun genotype() = arbGenotype(arbChromosome())
private fun individual() = arbIndividual(genotype())