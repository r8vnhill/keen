package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigurationException
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class InversionMutatorTest : FreeSpec({
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
    }
})
