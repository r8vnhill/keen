package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigurationException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.checkAll
import kotlin.random.Random

class InversionMutatorTest : FreeSpec({

    var originalToStringMode: ToStringMode = Domain.toStringMode

    beforeEach {
        originalToStringMode = Domain.toStringMode
        Domain.toStringMode = ToStringMode.SIMPLE
    }

    afterEach {
        Domain.toStringMode = originalToStringMode
    }

    "An InversionMutator instance" - {
        "when created with valid rates" - {
            "should use default parameters when none are provided" {
                InversionMutator<Nothing, NothingGene>().apply {
                    individualRate shouldBe InversionMutator.DEFAULT_INDIVIDUAL_RATE
                    chromosomeRate shouldBe InversionMutator.DEFAULT_CHROMOSOME_RATE
                    inversionBoundaryProbability shouldBe InversionMutator.DEFAULT_INVERSION_BOUNDARY_PROBABILITY
                }
            }

            "should have an individual rate property that" - {
                "defaults to DEFAULT_INDIVIDUAL_RATE" {
                    checkAll(arbProbability(), arbProbability()) { chromosomeRate, inversionBoundaryProbability ->
                        InversionMutator<Nothing, NothingGene>(
                            chromosomeRate = chromosomeRate,
                            inversionBoundaryProbability = inversionBoundaryProbability
                        ).also {
                            it.individualRate shouldBe InversionMutator.DEFAULT_INDIVIDUAL_RATE
                            it.chromosomeRate shouldBe chromosomeRate
                            it.inversionBoundaryProbability shouldBe inversionBoundaryProbability
                        }
                    }
                }
            }

            "should have a chromosome rate property that" - {
                "defaults to DEFAULT_CHROMOSOME_RATE" {
                    checkAll(arbProbability(), arbProbability()) { individualRate, inversionBoundaryProbability ->
                        InversionMutator<Nothing, NothingGene>(
                            individualRate = individualRate,
                            inversionBoundaryProbability = inversionBoundaryProbability
                        ).also {
                            it.individualRate shouldBe individualRate
                            it.chromosomeRate shouldBe InversionMutator.DEFAULT_CHROMOSOME_RATE
                            it.inversionBoundaryProbability shouldBe inversionBoundaryProbability
                        }
                    }
                }
            }

            "should have an inversion boundary probability property that" - {
                "defaults to DEFAULT_INVERSION_BOUNDARY_PROBABILITY" {
                    checkAll(arbProbability(), arbProbability()) { individualRate, chromosomeRate ->
                        InversionMutator<Nothing, NothingGene>(
                            individualRate = individualRate,
                            chromosomeRate = chromosomeRate
                        ).also {
                            it.individualRate shouldBe individualRate
                            it.chromosomeRate shouldBe chromosomeRate
                            it.inversionBoundaryProbability shouldBe
                                    InversionMutator.DEFAULT_INVERSION_BOUNDARY_PROBABILITY
                        }
                    }
                }
            }
        }

        "when created with invalid rates" - {
            "should throw an exception if" - {
                "the individual rate is not within the range [0.0, 1.0]" {
                    checkAll(
                        arbInvalidProbability(),
                        arbProbability(),
                        arbProbability()
                    ) { individualRate, chromosomeRate, inversionBoundaryProbability ->
                        shouldThrow<CompositeException> {
                            InversionMutator<Nothing, NothingGene>(
                                individualRate = individualRate,
                                chromosomeRate = chromosomeRate,
                                inversionBoundaryProbability = inversionBoundaryProbability
                            )
                        }.shouldHaveInfringement<MutatorConfigurationException>(
                            "The individual rate ($individualRate) must be in 0.0..1.0"
                        )
                    }
                }

                "the chromosome rate is not within the range [0.0, 1.0]" {
                    checkAll(
                        arbProbability(),
                        arbInvalidProbability(),
                        arbProbability()
                    ) { individualRate, chromosomeRate, inversionBoundaryProbability ->
                        shouldThrow<CompositeException> {
                            InversionMutator<Nothing, NothingGene>(
                                individualRate = individualRate,
                                chromosomeRate = chromosomeRate,
                                inversionBoundaryProbability = inversionBoundaryProbability
                            )
                        }.shouldHaveInfringement<MutatorConfigurationException>(
                            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"
                        )
                    }
                }

                "the inversion boundary probability is not within the range [0.0, 1.0]" {
                    checkAll(
                        arbProbability(),
                        arbProbability(),
                        arbInvalidProbability()
                    ) { individualRate, chromosomeRate, inversionBoundaryProbability ->
                        shouldThrow<CompositeException> {
                            InversionMutator<Nothing, NothingGene>(
                                individualRate = individualRate,
                                chromosomeRate = chromosomeRate,
                                inversionBoundaryProbability = inversionBoundaryProbability
                            )
                        }.shouldHaveInfringement<MutatorConfigurationException>(
                            "Inversion boundary probability ($inversionBoundaryProbability) must be in 0.0..1.0"
                        )
                    }
                }
            }
        }

        "when mutating a chromosome" - {
            "should perform no mutation if the inversion boundary probability is 0.0" {
                checkAll(
                    arbInversionMutator<Int, IntGene>(inversionBoundaryProbability = Arb.constant(0.0)),
                    arbIntChromosome()
                ) { mutator, chromosome ->
                    val mutated = mutator.mutateChromosome(chromosome)
                    mutated shouldBe chromosome
                }
            }

            "should invert the entire chromosome if the inversion boundary probability is 1.0" {
                checkAll(
                    arbInversionMutator<Int, IntGene>(inversionBoundaryProbability = Arb.constant(1.0)),
                    arbIntChromosome()
                ) { mutator, chromosome ->
                    val mutated = mutator.mutateChromosome(chromosome)
                    mutated shouldBe chromosome.reversed()
                }
            }

            "should invert a sub-sequence of the chromosome" - {
                Domain.toStringMode = ToStringMode.SIMPLE
                withData(
                    nameFn = { "${it.inChromosome} -> ${it.outChromosome}" },
                    InversionData(
                        inChromosome = IntChromosome(1, 2, 3, 4, 5),
                        mutator = InversionMutator(inversionBoundaryProbability = 0.5),
                        seed = 0,
                        outChromosome = IntChromosome(1, 2, 3, 5, 4)
                    ),
                    InversionData(
                        inChromosome = IntChromosome(1, 2, 3, 4, 5),
                        mutator = InversionMutator(inversionBoundaryProbability = 0.3),
                        seed = 69,
                        outChromosome = IntChromosome(1, 2, 3, 5, 4)
                    ),
                    InversionData(
                        inChromosome = IntChromosome(1, 2, 3, 4, 5),
                        mutator = InversionMutator(inversionBoundaryProbability = 0.7),
                        seed = 420,
                        outChromosome = IntChromosome(1, 2, 3, 4, 5)
                    )
                ) { inversionData ->
                    with(inversionData) {
                        Domain.random = Random(seed)
                        val mutated = mutator.mutateChromosome(inChromosome)
                        mutated shouldBe outChromosome
                    }
                }
            }
        }
    }
})

fun <T, G> arbInversionMutator(
    individualRate: Arb<Double> = arbProbability(),
    chromosomeRate: Arb<Double> = arbProbability(),
    inversionBoundaryProbability: Arb<Double> = arbProbability()
): Arb<InversionMutator<T, G>> where G : Gene<T, G> = arbitrary {
    InversionMutator(
        individualRate = individualRate.bind(),
        chromosomeRate = chromosomeRate.bind(),
        inversionBoundaryProbability = inversionBoundaryProbability.bind()
    )
}

private data class InversionData<T, G>(
    val inChromosome: Chromosome<T, G>,
    val mutator: InversionMutator<T, G>,
    val seed: Long,
    val outChromosome: Chromosome<T, G>
) where G : Gene<T, G>
