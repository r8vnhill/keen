package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class DisplacementMutatorTest : FreeSpec({
    "A Displacement Mutator" - {
        "when constructing" - {
            "should throw an exception if" - {
                "the individual rate is not in the range 0 to 1" {
                    checkAll(
                        arbInvalidProbability(),
                        Arb.double(),
                        Arb.double()
                    ) { rate, chRate, displacementBoundaryProb ->
                        shouldThrow<CompositeException> {
                            DisplacementMutator<Double, DoubleGene>(rate, chRate, displacementBoundaryProb)
                        }.shouldHaveInfringement<MutatorConfigException>("The individual rate ($rate) must be in 0.0..1.0")
                    }
                }

                "the chromosome rate is not in the range 0 to 1" {
                    checkAll(
                        Arb.double(),
                        arbInvalidProbability(),
                        Arb.double()
                    ) { rate, chRate, displacementBoundaryProb ->
                        shouldThrow<CompositeException> {
                            DisplacementMutator<Double, DoubleGene>(rate, chRate, displacementBoundaryProb)
                        }.shouldHaveInfringement<MutatorConfigException>("The chromosome rate ($chRate) must be in 0.0..1.0")
                    }
                }

                "the displacement boundary probability is not in the range 0 to 1" {
                    checkAll(
                        Arb.double(),
                        Arb.double(),
                        arbInvalidProbability()
                    ) { rate, chRate, displacementBoundaryProb ->
                        shouldThrow<CompositeException> {
                            DisplacementMutator<Double, DoubleGene>(rate, chRate, displacementBoundaryProb)
                        }.shouldHaveInfringement<MutatorConfigException>("The displacement boundary probability ($displacementBoundaryProb) must be in 0.0..1.0")
                    }
                }
            }

            "with default values" - {
                "should create a mutator with the default values" {
                    val mutator = DisplacementMutator<Double, DoubleGene>()
                    mutator.individualRate shouldBe DisplacementMutator.DEFAULT_INDIVIDUAL_RATE
                    mutator.chromosomeRate shouldBe DisplacementMutator.DEFAULT_CHROMOSOME_RATE
                    mutator.displacementBoundaryProbability shouldBe
                            DisplacementMutator.DEFAULT_DISPLACEMENT_BOUNDARY_PROBABILITY
                }
            }

            "with custom values" - {
                "should create a mutator with the custom values" {
                    checkAll(
                        arbProbability(),
                        arbProbability(),
                        arbProbability()
                    ) { rate, chRate, displacementBoundaryProb ->
                        val mutator = DisplacementMutator<Double, DoubleGene>(rate, chRate, displacementBoundaryProb)
                        mutator.individualRate shouldBe rate
                        mutator.chromosomeRate shouldBe chRate
                        mutator.displacementBoundaryProbability shouldBe displacementBoundaryProb
                    }
                }
            }
        }
    }
})