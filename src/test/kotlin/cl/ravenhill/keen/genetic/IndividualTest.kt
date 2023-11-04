/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arbs.genetic.individual
import cl.ravenhill.keen.arbs.genetic.intGenotype
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.arbs.datatypes.real
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.doubles.shouldBeNaN
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({
    "A [Individual]" - {
        "when creating a new one" - {
            "without fitness defaults to NaN" {
                checkAll(Arb.intGenotype()) { genotype ->
                    val individual = Individual(genotype)
                    individual.genotype shouldBe genotype
                    individual.fitness.shouldBeNaN()
                }
            }

            "with fitness should have the given fitness" {
                checkAll(Arb.intGenotype(), Arb.real()) { genotype, fitness ->
                    val individual = Individual(genotype, fitness)
                    individual.genotype shouldBe genotype
                    individual.fitness shouldBe fitness
                }
            }
        }

        "verification should" - {
            "return true when" - {
                "the genotype is empty and the fitness is not NaN" {
                    checkAll(Arb.real()) { fitness ->
                        Individual<Int, IntGene>(Genotype(), fitness).verify().shouldBeTrue()
                    }
                }

                "the genotype is valid and the fitness is not NaN" {
                    checkAll(Arb.intGenotype(), Arb.real()) { genotype, fitness ->
                        Individual(genotype, fitness).verify().shouldBeTrue()
                    }
                }
            }

            "return false when" - {
                "the genotype is empty and the fitness is NaN" {
                    Individual<Int, IntGene>(Genotype(), Double.NaN).verify().shouldBeFalse()
                }

                "the genotype is invalid and the fitness is not NaN" {
                    checkAll(Arb.intGenotype(), Arb.real()) { genotype, fitness ->
                        val invalidChromosome =
                            IntChromosome(listOf(IntGene(0, filter = { false })))
                        val gt = Genotype(genotype.chromosomes + listOf(invalidChromosome))
                        Individual(gt, fitness).verify().shouldBeFalse()
                    }
                }
            }
        }

        "can be compared based on fitness" {
            checkAll(
                Arb.individual(Arb.intGenotype()),
                Arb.individual(Arb.intGenotype())
            ) { i1, i2 ->
                i1.compareTo(i2) shouldBe i1.fitness.compareTo(i2.fitness)
            }
        }

        "can check if the genotype has been evaluated when" - {
            "the fitness is NaN" {
                checkAll(Arb.intGenotype()) { genotype ->
                    val individual = Individual(genotype)
                    individual.isEvaluated().shouldBeFalse()
                    individual.isNotEvaluated().shouldBeTrue()
                }
            }

            "the fitness is not NaN" {
                checkAll(Arb.intGenotype(), Arb.real()) { genotype, fitness ->
                    val individual = Individual(genotype, fitness)
                    individual.isEvaluated().shouldBeTrue()
                    individual.isNotEvaluated().shouldBeFalse()
                }
            }
        }

        "can create another one with the given fitness" {
            checkAll(Arb.individual(Arb.intGenotype()), Arb.real()) { individual, fitness ->
                with(individual.withFitness(fitness)) {
                    this.fitness shouldBe fitness
                    this.genotype shouldBe individual.genotype
                }
            }
        }

        "can create another one with the given genotype and fitness" {
            checkAll(Arb.individual(Arb.intGenotype()), Arb.intGenotype(), Arb.real()) { individual, genotype, fitness ->
                with(individual.withGenotype(genotype, fitness)) {
                    this.fitness shouldBe fitness
                    this.genotype shouldBe genotype
                }
            }
        }

        "can be flattened" {
            checkAll(Arb.individual(Arb.intGenotype())) { individual ->
                individual.flatMap() shouldBe individual.genotype.flatMap()
            }
        }
    }
})
