/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.arbs.genetic.intGenotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.NothingChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

class UniformLengthCrossoverTest : FreeSpec({
    "A [Crossover] operator" - {
        "when created" - {
            "without an explicit number of outputs defaults to 2" {
                checkAll(
                    Arb.probability(),
                    Arb.int(2..Int.MAX_VALUE),
                    Arb.positiveInt(10),
                    Arb.boolean(),
                    Arb.probability()
                ) { probability, numIn, numOut, exclusivity, chromosomeRate ->
                    object : AbstractUniformLenghtCrossover<Nothing, NothingGene>(
                        probability,
                        numIn = numIn,
                        exclusivity = exclusivity,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun crossoverChromosomes(
                            chromosomes: List<Chromosome<Nothing, NothingGene>>
                        ) = List(numOut) { NothingChromosome(listOf()) }
                    }.numOut shouldBe 2
                }
            }

            "without an explicit number of inputs defaults to 2" {
                checkAll(
                    Arb.probability(),
                    Arb.positiveInt(10),
                    Arb.boolean(),
                    Arb.probability()
                ) { probability, numOut, exclusivity, chromosomeRate ->
                    object : AbstractUniformLenghtCrossover<Nothing, NothingGene>(
                        probability,
                        numOut = numOut,
                        exclusivity = exclusivity,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun crossoverChromosomes(
                            chromosomes: List<Chromosome<Nothing, NothingGene>>
                        ) = List(numOut) { NothingChromosome(listOf()) }
                    }.numIn shouldBe 2
                }
            }

            "without an explicit exclusivity defaults to false" {
                checkAll(
                    Arb.probability(),
                    Arb.int(2..Int.MAX_VALUE),
                    Arb.positiveInt(10),
                    Arb.probability()
                ) { probability, numIn, numOut, chromosomeRate ->
                    object : AbstractUniformLenghtCrossover<Nothing, NothingGene>(
                        probability,
                        numOut = numOut,
                        numIn = numIn,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun crossoverChromosomes(
                            chromosomes: List<Chromosome<Nothing, NothingGene>>
                        ) = List(numOut) { NothingChromosome(listOf()) }
                    }.exclusivity shouldBe false
                }
            }

            "without a chromosome rate defaults to 1.0" {
                checkAll(
                    Arb.probability(),
                    Arb.int(2..Int.MAX_VALUE),
                    Arb.positiveInt(10),
                    Arb.boolean()
                ) { probability, numIn, numOut, exclusivity ->
                    object : AbstractUniformLenghtCrossover<Nothing, NothingGene>(
                        probability,
                        numOut = numOut,
                        numIn = numIn,
                        exclusivity = exclusivity
                    ) {
                        override fun crossoverChromosomes(
                            chromosomes: List<Chromosome<Nothing, NothingGene>>
                        ) = List(numOut) { NothingChromosome(listOf()) }
                    }.chromosomeRate shouldBe 1.0
                }
            }

            "should throw an exception when" - {
                "the number of inputs is less than 2" {
                    checkAll(
                        Arb.probability(),
                        Arb.int(Int.MIN_VALUE..1),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.boolean(),
                        Arb.probability()
                    ) { probability, numIn, numOut, exclusivity, chromosomeRate ->
                        shouldThrow<CompositeException> {
                            object : AbstractUniformLenghtCrossover<Nothing, NothingGene>(
                                probability,
                                numOut = numOut,
                                numIn = numIn,
                                exclusivity = exclusivity,
                                chromosomeRate = chromosomeRate
                            ) {
                                override fun crossoverChromosomes(
                                    chromosomes: List<Chromosome<Nothing, NothingGene>>
                                ) = List(numOut) { NothingChromosome(listOf()) }
                            }
                        }.shouldHaveInfringement<IntConstraintException>(
                            unfulfilledConstraint("There should be at least 2 inputs to perform a crossover operation")
                        )
                    }
                }

                "the number of outputs is negative" {
                    checkAll(
                        Arb.probability(),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.negativeInt(),
                        Arb.boolean(),
                        Arb.probability()
                    ) { probability, numIn, numOut, exclusivity, chromosomeRate ->
                        shouldThrow<CompositeException> {
                            object : AbstractUniformLenghtCrossover<Nothing, NothingGene>(
                                probability,
                                numOut = numOut,
                                numIn = numIn,
                                exclusivity = exclusivity,
                                chromosomeRate = chromosomeRate
                            ) {
                                override fun crossoverChromosomes(
                                    chromosomes: List<Chromosome<Nothing, NothingGene>>
                                ) = List(numOut) { NothingChromosome(listOf()) }
                            }
                        }.shouldHaveInfringement<IntConstraintException>(
                            unfulfilledConstraint(
                                "The number of outputs should be greater than 0"
                            )
                        )
                    }
                }

                "the chromosome crossover probability is greater than 1" {
                    checkAll(
                        Arb.probability(),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.boolean(),
                        Arb.real(1.0..Double.MAX_VALUE)
                    ) { probability, numIn, numOut, exclusivity, chromosomeRate ->
                        shouldThrow<CompositeException> {
                            object : AbstractUniformLenghtCrossover<Nothing, NothingGene>(
                                probability,
                                numOut = numOut,
                                numIn = numIn,
                                exclusivity = exclusivity,
                                chromosomeRate = chromosomeRate
                            ) {
                                override fun crossoverChromosomes(
                                    chromosomes: List<Chromosome<Nothing, NothingGene>>
                                ) = List(numOut) { NothingChromosome(listOf()) }
                            }
                        }.shouldHaveInfringement<DoubleConstraintException>(
                            "The chromosome crossover probability should be in 0..1"
                        )
                    }
                }
            }
        }

        "when crossing genotypes" - {
            "should return an empty list if the chromosome rate is 0" {
                checkAll(
                    Arb.probability(),
                    Arb.int(2..10),
                    Arb.int(1..10),
                    Arb.boolean(),
                    Arb.int(1..10)
                ) { probability, numIn, numOut, exclusivity, genotypeSize ->
                    val genotypes = List(numIn) {
                        Genotype(List(genotypeSize) { IntChromosome(listOf(IntGene(1), IntGene(2))) })
                    }
                    val operator = DummyCrossover(
                        probability,
                        numOut,
                        numIn,
                        exclusivity,
                        0.0
                    )
                    operator.crossover(genotypes) shouldBe emptyList()
                }
            }

            "should cross all chromosomes if the chromosome rate is 1" {
                checkAll(
                    Arb.probability(),
                    Arb.int(2..10),
                    Arb.int(1..10),
                    Arb.boolean(),
                    Arb.int(1..10)
                ) { probability, numIn, numOut, exclusivity, genotypeSize ->
                    val genotypes = List(numIn) {
                        Genotype(List(genotypeSize) { IntChromosome(listOf(IntGene(1), IntGene(2))) })
                    }
                    val operator = DummyCrossover(
                        probability,
                        numOut,
                        numIn,
                        exclusivity,
                        1.0
                    )
                    operator.crossover(genotypes) shouldHaveSize numOut
                }
            }

            "should throw an exception" - {
                "if the number of inputs doesn't match the required amount" {
                    checkAll(
                        Arb.probability(),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.boolean(),
                        Arb.probability(),
                        Arb.list(Arb.intGenotype()),
                    ) { probability, numIn, numOut, exclusivity, chromosomeRate, genotypes ->
                        assume {
                            genotypes shouldNotHaveSize numIn
                        }
                        val operator = DummyCrossover(
                            probability,
                            numOut,
                            numIn,
                            exclusivity,
                            chromosomeRate
                        )
                        shouldThrow<CompositeException> {
                            operator.crossover(genotypes)
                        }.shouldHaveInfringement<IntConstraintException>(
                            "Input count [${genotypes.size}] must match constructor-specified count [$numIn]."
                        )
                    }
                }

                "if the genotypes have different lengths" {
                    checkAll(
                        Arb.probability(),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.boolean(),
                        Arb.probability(),
                        Arb.list(Arb.intGenotype()),
                    ) { probability, numIn, numOut, exclusivity, chromosomeRate, genotypes ->
                        assume {
                            genotypes.map { it.size }.distinct().size shouldNotBe 1
                        }
                        val operator = DummyCrossover(
                            probability,
                            numOut,
                            numIn,
                            exclusivity,
                            chromosomeRate
                        )
                        shouldThrow<CompositeException> {
                            operator.crossover(genotypes)
                        }.shouldHaveInfringement<IntConstraintException>(
                            "All inputs must have the same genotype length"
                        )
                    }
                }
            }
        }

        "when crossing populations" - {
            "if the probability is 0 should return an [AltererResult] with the same population" - {
                checkAll(
                    Arb.int(2..10),
                    Arb.int(2..10),
                    Arb.boolean(),
                    Arb.probability(),
                ) { numIn, numOut, exclusivity, chromosomeRate ->
                    val operator = DummyCrossover(
                        0.0,
                        numOut,
                        numIn,
                        exclusivity,
                        chromosomeRate
                    )
                    val individuals = listOf(
                        Individual(Genotype(listOf(IntChromosome(listOf(IntGene(1), IntGene(2)))))),
                        Individual(Genotype(listOf(IntChromosome(listOf(IntGene(3), IntGene(4)))))),
                    )
                    operator(individuals, 0) shouldBe AltererResult(individuals)
                }
            }
        }
    }
}) {
    class DummyCrossover(
        probability: Double,
        numOut: Int,
        numIn: Int,
        exclusivity: Boolean,
        chromosomeRate: Double
    ) : AbstractUniformLenghtCrossover<Int, IntGene>(
        probability,
        numOut,
        numIn,
        exclusivity,
        chromosomeRate
    ) {
        override fun crossoverChromosomes(
            chromosomes: List<Chromosome<Int, IntGene>>
        ) = List(numOut) { IntChromosome(listOf(IntGene(1), IntGene(2))) }
    }
}
