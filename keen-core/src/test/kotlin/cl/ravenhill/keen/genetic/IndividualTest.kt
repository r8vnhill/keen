/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arb.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({

    "An Individual" - {
        "should have a genotype property that is set according to the constructor" {
            checkAll(Arb.genotype(arbChromosome()), Arb.double()) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.genotype shouldBe genotype
            }
        }

        "should have a fitness property that is set according to the constructor" {
            checkAll(Arb.genotype(arbChromosome()), Arb.double().filterNot { it.isNaN() }) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.fitness shouldBe fitness
            }
        }

        "should have a size property that is equal to the size of the genotype" {
            checkAll(Arb.genotype(arbChromosome()), Arb.double()) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.size shouldBe genotype.size
            }
        }

        "when verifying" - {
            "should return true if the genotype is valid and the fitness is not NaN" {
                checkAll(
                    arbIndividual(
                        Arb.genotype(
                            arbChromosome(isValid = Arb.constant(true)),
                            size = Arb.int(1..10)
                        )
                    )
                ) { individual ->
                    individual.verify() shouldBe true
                }
            }

            "should return false if the genotype is invalid" {
                checkAll(
                    arbIndividual(
                        Arb.genotype(
                            arbChromosome(size = Arb.int(1..10), isValid = Arb.constant(false)),
                            size = Arb.int(1..10)
                        )
                    )
                ) { individual ->
                    individual.verify().shouldBeFalse()
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(arbIndividual(Arb.genotype(arbChromosome()), Arb.constant(Double.NaN))) { individual ->
                    individual.verify() shouldBe false
                }
            }
        }

        "can be flat-mapped" {
            checkAll(arbIndividual(Arb.genotype(arbChromosome()))) { individual ->
                val flatMapped = individual.flatten()
                flatMapped.size shouldBe individual.genotype.sumOf { it.size }
                flatMapped shouldBe individual.genotype.flatten()
            }
        }

        "when checking if the individual is evaluated" - {
            "should return true if the fitness is not NaN" {
                checkAll(
                    arbIndividual(
                        Arb.genotype(arbChromosome()),
                        Arb.double().filterNot { it.isNaN() })
                ) { individual ->
                    individual.isEvaluated() shouldBe true
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(arbIndividual(Arb.genotype(arbChromosome()), Arb.constant(Double.NaN))) { individual ->
                    individual.isEvaluated() shouldBe false
                }
            }
        }
    }

    "A Population of Individuals" - {
        "should have a fitness property that is equal to the list of fitness values of the individuals" {
            val individualArb = arbIndividual(Arb.genotype(arbNothingChromosome()))
            checkAll(arbPopulation(individualArb)) { population ->
                population.fitness shouldBe population.map { it.fitness }
            }
        }
    }
})