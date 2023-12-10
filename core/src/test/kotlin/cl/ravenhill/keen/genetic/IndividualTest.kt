/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arb.genetic.chromosomes.chromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.individual
import io.kotest.assertions.fail
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
            checkAll(Arb.genotype(Arb.chromosome()), Arb.double()) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.genotype shouldBe genotype
            }
        }

        "should have a fitness property that is set according to the constructor" {
            checkAll(Arb.genotype(Arb.chromosome()), Arb.double().filterNot { it.isNaN() }) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.fitness shouldBe fitness
            }
        }

        "should have a size property that is equal to the size of the genotype" {
            checkAll(Arb.genotype(Arb.chromosome()), Arb.double()) { genotype, fitness ->
                val individual = Individual(genotype, fitness)
                individual.size shouldBe genotype.size
            }
        }

        "when verifying" - {
            "should return true if the genotype is valid and the fitness is not NaN" {
                checkAll(
                    Arb.individual(
                        Arb.genotype(
                            Arb.chromosome(isValid = Arb.constant(true)),
                            size = Arb.int(1..10)
                        )
                    )
                ) { individual ->
                    individual.verify() shouldBe true
                }
            }

            "should return false if the genotype is invalid" {
                checkAll(
                    Arb.individual(
                        Arb.genotype(
                            Arb.chromosome(size = Arb.int(1..10), isValid = Arb.constant(false)),
                            size = Arb.int(1..10)
                        )
                    )
                ) { individual ->
                    individual.verify().shouldBeFalse()
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(Arb.individual(Arb.genotype(Arb.chromosome()), Arb.constant(Double.NaN))) { individual ->
                    individual.verify() shouldBe false
                }
            }
        }

        "can be flat-mapped" {
            checkAll(Arb.individual(Arb.genotype(Arb.chromosome()))) { individual ->
                val flatMapped = individual.flatten()
                flatMapped.size shouldBe individual.genotype.sumOf { it.size }
                flatMapped shouldBe individual.genotype.flatten()
            }
        }

        "when checking if the individual is evaluated" - {
            "should return true if the fitness is not NaN" {
                checkAll(
                    Arb.individual(
                        Arb.genotype(Arb.chromosome()),
                        Arb.double().filterNot { it.isNaN() })
                ) { individual ->
                    individual.isEvaluated() shouldBe true
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(Arb.individual(Arb.genotype(Arb.chromosome()), Arb.constant(Double.NaN))) { individual ->
                    individual.isEvaluated() shouldBe false
                }
            }
        }

        "can be converted to a simple string" {
            fail("Not implemented")
        }

        "can be converted to a string" {
            fail("Not implemented")
        }

        "can be converted to a detailed string" {
            fail("Not implemented")
        }

        "when checking equality" - {
            "should be reflexive" {
                fail("Not implemented")
            }

            "should be symmetric" {
                fail("Not implemented")
            }

            "should be transitive" {
                fail("Not implemented")
            }
        }

        "when checking hash codes" - {
            "should be consistent with equals" {
                fail("Not implemented")
            }

            "should be different for non-equal individuals" {
                fail("Not implemented")
            }
        }
    }
})