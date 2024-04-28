package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosome
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class DisplacementMutatorTest : FreeSpec({
    "A Displacement Mutator" - {
        "when constructing" - {
            "should throw an exception if" - {
                "the individual rate is not in the range 0 to 1" {
                    checkAll(
                        arbInvalidProbability(),
                        Arb.double(),
                        Arb.int()
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
                        Arb.int()
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
                        Arb.negativeInt()
                    ) { rate, chRate, displacement ->
                        shouldThrow<CompositeException> {
                            DisplacementMutator<Double, DoubleGene>(rate, chRate, displacement)
                        }.shouldHaveInfringement<MutatorConfigException>("The displacement must be a non-negative integer")
                    }
                }
            }

            "with default values" - {
                "should create a mutator with the default values" {
                    val mutator = DisplacementMutator<Double, DoubleGene>()
                    mutator.individualRate shouldBe DisplacementMutator.DEFAULT_INDIVIDUAL_RATE
                    mutator.chromosomeRate shouldBe DisplacementMutator.DEFAULT_CHROMOSOME_RATE
                    mutator.displacement shouldBe
                            DisplacementMutator.DEFAULT_DISPLACEMENT
                }
            }

            "with custom values" - {
                "should create a mutator with the custom values" {
                    checkAll(
                        arbProbability(),
                        arbProbability(),
                        Arb.positiveInt()
                    ) { rate, chRate, displacementBoundaryProb ->
                        val mutator = DisplacementMutator<Double, DoubleGene>(rate, chRate, displacementBoundaryProb)
                        mutator.individualRate shouldBe rate
                        mutator.chromosomeRate shouldBe chRate
                        mutator.displacement shouldBe displacementBoundaryProb
                    }
                }
            }
        }

        "when mutating" - {
            "should return the same chromosome if the displacement boundary probability is 0" {
                checkAll(
                    arbDisplacementMutator(displacement = Arb.constant(0)),
                    arbDoubleChromosome()
                ) { mutator, chromosome ->
                    val mutated = mutator.mutateChromosome(chromosome)
                    mutated shouldBe chromosome
                }
            }
        }
    }
})

private fun arbDisplacementMutator(
    individualRate: Arb<Double>? = arbProbability(),
    chromosomeRate: Arb<Double>? = arbProbability(),
    displacement: Arb<Int>? = Arb.int()
): Arb<DisplacementMutator<Double, DoubleGene>> = arbitrary {
    DisplacementMutator(
        individualRate = individualRate?.bind() ?: DisplacementMutator.DEFAULT_INDIVIDUAL_RATE,
        chromosomeRate = chromosomeRate?.bind() ?: DisplacementMutator.DEFAULT_CHROMOSOME_RATE,
        displacement = displacement?.bind() ?: DisplacementMutator.DEFAULT_DISPLACEMENT
    )
}