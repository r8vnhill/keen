package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
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
                            DisplacementMutator(rate, chRate, displacementBoundaryProb)
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
                            DisplacementMutator(rate, chRate, displacementBoundaryProb)
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
                            DisplacementMutator(rate, chRate, displacementBoundaryProb)
                        }.shouldHaveInfringement<MutatorConfigException>("The displacement boundary probability ($displacementBoundaryProb) must be in 0.0..1.0")
                    }
                }
            }
        }
    }
})