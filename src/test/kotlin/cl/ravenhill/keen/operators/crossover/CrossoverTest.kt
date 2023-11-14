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
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

class CrossoverTest : FreeSpec({
    "A [Crossover] operator" - {
        "when created" - {
            "without an explicit number of outputs defaults to 2" {
                checkAll(
                    Arb.int(2..Int.MAX_VALUE),
                    Arb.positiveInt(10),
                    Arb.boolean(),
                    Arb.probability()
                ) { numIn, numOut, exclusivity, chromosomeRate ->
                    object : AbstractCrossover<Nothing, NothingGene>(
                        numParents = numIn,
                        exclusivity = exclusivity,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun crossoverChromosomes(
                            chromosomes: List<Chromosome<Nothing, NothingGene>>
                        ) = List(numOut) { NothingChromosome(listOf()) }
                    }.numOffspring shouldBe 2
                }
            }

            "without an explicit number of inputs defaults to 2" {
                checkAll(
                    Arb.positiveInt(10),
                    Arb.boolean(),
                    Arb.probability()
                ) { numOut, exclusivity, chromosomeRate ->
                    object : AbstractCrossover<Nothing, NothingGene>(
                        numOffspring = numOut,
                        exclusivity = exclusivity,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun crossoverChromosomes(
                            chromosomes: List<Chromosome<Nothing, NothingGene>>
                        ) = List(numOut) { NothingChromosome(listOf()) }
                    }.numParents shouldBe 2
                }
            }

            "without an explicit exclusivity defaults to false" {
                checkAll(
                    Arb.int(2..Int.MAX_VALUE),
                    Arb.positiveInt(10),
                    Arb.probability()
                ) { numIn, numOut, chromosomeRate ->
                    object : AbstractCrossover<Nothing, NothingGene>(
                        numOffspring = numOut,
                        numParents = numIn,
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
                    Arb.int(2..Int.MAX_VALUE),
                    Arb.positiveInt(10),
                    Arb.boolean()
                ) { numIn, numOut, exclusivity ->
                    object : AbstractCrossover<Nothing, NothingGene>(
                        numOffspring = numOut,
                        numParents = numIn,
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
                        Arb.int(Int.MIN_VALUE..1),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.boolean(),
                        Arb.probability()
                    ) { numIn, numOut, exclusivity, chromosomeRate ->
                        shouldThrow<CompositeException> {
                            object : AbstractCrossover<Nothing, NothingGene>(
                                numOffspring = numOut,
                                numParents = numIn,
                                exclusivity = exclusivity,
                                chromosomeRate = chromosomeRate
                            ) {
                                override fun crossoverChromosomes(
                                    chromosomes: List<Chromosome<Nothing, NothingGene>>
                                ) = List(numOut) { NothingChromosome(listOf()) }
                            }
                        }.shouldHaveInfringement<IntConstraintException>(
                            "There should be at least 2 inputs to perform a crossover operation"
                        )
                    }
                }

                "the number of outputs is negative" {
                    checkAll(
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.negativeInt(),
                        Arb.boolean(),
                        Arb.probability()
                    ) { numIn, numOut, exclusivity, chromosomeRate ->
                        shouldThrow<CompositeException> {
                            object : AbstractCrossover<Nothing, NothingGene>(
                                numOffspring = numOut,
                                numParents = numIn,
                                exclusivity = exclusivity,
                                chromosomeRate = chromosomeRate
                            ) {
                                override fun crossoverChromosomes(
                                    chromosomes: List<Chromosome<Nothing, NothingGene>>
                                ) = List(numOut) { NothingChromosome(listOf()) }
                            }
                        }.shouldHaveInfringement<IntConstraintException>(
                            "The number of outputs should be greater than 0"
                        )
                    }
                }

                "the chromosome crossover probability is greater than 1" {
                    checkAll(
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.boolean(),
                        Arb.real(1.0..Double.MAX_VALUE)
                    ) { numIn, numOut, exclusivity, chromosomeRate ->
                        shouldThrow<CompositeException> {
                            object : AbstractCrossover<Nothing, NothingGene>(
                                numOffspring = numOut,
                                numParents = numIn,
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
                    Arb.int(2..10),
                    Arb.int(1..10),
                    Arb.boolean(),
                    Arb.int(1..10)
                ) { numIn, numOut, exclusivity, genotypeSize ->
                    val genotypes = List(numIn) {
                        Genotype(List(genotypeSize) { IntChromosome(listOf(IntGene(1), IntGene(2))) })
                    }
                    val operator = DummyCrossover(
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
                    Arb.int(2..10),
                    Arb.int(1..10),
                    Arb.boolean(),
                    Arb.int(1..10)
                ) { numIn, numOut, exclusivity, genotypeSize ->
                    val genotypes = List(numIn) {
                        Genotype(List(genotypeSize) { IntChromosome(listOf(IntGene(1), IntGene(2))) })
                    }
                    val operator = DummyCrossover(
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
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.int(2..Int.MAX_VALUE),
                        Arb.boolean(),
                        Arb.probability(),
                        Arb.list(Arb.intGenotype()),
                    ) { numIn, numOut, exclusivity, chromosomeRate, genotypes ->
                        assume {
                            genotypes shouldNotHaveSize numIn
                        }
                        val operator = DummyCrossover(
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
            }
        }

        "when crossing populations" - {
            "should return an [AltererResult] with a population with the same size as the original" {
                checkAll(
                    Arb.int(2..10),
                    Arb.int(2..10),
                    Arb.probability()
                ) { numIn, numOut, chromosomeRate ->
                    val operator = DummyCrossover(
                        numOut,
                        numIn,
                        false,
                        chromosomeRate
                    )
                    val individuals = List(5) {
                        Individual(
                            Genotype(
                                listOf(
                                    IntChromosome(listOf(IntGene(it), IntGene(it + 1))),
                                    IntChromosome(listOf(IntGene(it), IntGene(it + 1)))
                                )
                            )
                        )
                    }
                    with(operator(individuals, 0)) {
                        population shouldHaveSize individuals.size
                    }
                }
            }
        }
    }
}) {
    class DummyCrossover(
        numOut: Int,
        numIn: Int,
        exclusivity: Boolean,
        chromosomeRate: Double
    ) : AbstractCrossover<Int, IntGene>(
        numOut,
        numIn,
        exclusivity,
        chromosomeRate
    ) {
        override fun crossoverChromosomes(
            chromosomes: List<Chromosome<Int, IntGene>>
        ) = List(numOffspring) { IntChromosome(listOf(IntGene(1), IntGene(2))) }
    }
}
