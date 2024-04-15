package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.freeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class PartialShuffleMutatorTest : FreeSpec({
    include(`operator construction`())

    "When mutating a chromosome" - {
        "should return the same chromosome if the shuffle boundary probability is 0" {
            checkAll(
                arbPartialShuffleMutator(arbProbability(), arbProbability(), Arb.constant(0.0)),
                arbIntChromosome()
            ) { mutator, chromosome ->
                mutator.mutateChromosome(chromosome) shouldBe chromosome
            }
        }
    }
})

private fun `operator construction`() = freeSpec {
    "When constructing" - {
        `throws an exception on invalid rates`()
        `creates a mutator on valid rates`()
    }
}

private suspend fun FreeSpecContainerScope.`throws an exception on invalid rates`() {
    "should throw an exception if the individual rate is not in 0..1" {
        checkAll(
            arbInvalidProbability(),
            Arb.double(),
            Arb.double()
        ) { invalidProbability, chromosomeRate, shuffleBoundary ->
            shouldThrow<CompositeException> {
                PartialShuffleMutator<Int, IntGene>(invalidProbability, chromosomeRate, shuffleBoundary)
            }.shouldHaveInfringement<MutatorConfigException>(
                "Individual mutation rate must be in the range [0, 1]"
            )
        }
    }

    "should throw an exception if the chromosome rate is not in 0..1" {
        checkAll(
            Arb.double(),
            arbInvalidProbability(),
            Arb.double()
        ) { individualRate, invalidProbability, shuffleBoundary ->
            shouldThrow<CompositeException> {
                PartialShuffleMutator<Int, IntGene>(individualRate, invalidProbability, shuffleBoundary)
            }.shouldHaveInfringement<MutatorConfigException>(
                "Chromosome mutation rate must be in the range [0, 1]"
            )
        }
    }

    "should throw an exception if the shuffle boundary probability is not in 0..1" {
        checkAll(
            Arb.double(),
            Arb.double(),
            arbInvalidProbability()
        ) { individualRate, chromosomeRate, invalidProbability ->
            shouldThrow<CompositeException> {
                PartialShuffleMutator<Int, IntGene>(individualRate, chromosomeRate, invalidProbability)
            }.shouldHaveInfringement<MutatorConfigException>(
                "Shuffle boundary probability must be in the range [0, 1]"
            )
        }
    }
}

private suspend fun FreeSpecContainerScope.`creates a mutator on valid rates`() {
    "with default individual and chromosome rates of 1.0" {
        with(PartialShuffleMutator<Int, IntGene>()) {
            individualRate shouldBe PartialShuffleMutator.DEFAULT_INDIVIDUAL_RATE
            chromosomeRate shouldBe PartialShuffleMutator.DEFAULT_CHROMOSOME_RATE
            shuffleBoundaryProbability shouldBe PartialShuffleMutator.DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY
        }
    }

    "with custom individual rate" - {
        checkAll(arbProbability()) { rate ->
            with(PartialShuffleMutator<Int, IntGene>(rate)) {
                individualRate shouldBe rate
                chromosomeRate shouldBe PartialShuffleMutator.DEFAULT_CHROMOSOME_RATE
                shuffleBoundaryProbability shouldBe PartialShuffleMutator.DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY
            }
        }
    }

    "with custom chromosome rate" - {
        checkAll(arbProbability()) { rate ->
            with(PartialShuffleMutator<Int, IntGene>(chromosomeRate = rate)) {
                individualRate shouldBe PartialShuffleMutator.DEFAULT_INDIVIDUAL_RATE
                chromosomeRate shouldBe rate
                shuffleBoundaryProbability shouldBe PartialShuffleMutator.DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY
            }
        }
    }

    "with custom shuffle boundary probability" - {
        checkAll(arbProbability()) { probability ->
            with(PartialShuffleMutator<Int, IntGene>(shuffleBoundaryProbability = probability)) {
                individualRate shouldBe PartialShuffleMutator.DEFAULT_INDIVIDUAL_RATE
                chromosomeRate shouldBe PartialShuffleMutator.DEFAULT_CHROMOSOME_RATE
                shuffleBoundaryProbability shouldBe probability
            }
        }
    }
}

private fun arbPartialShuffleMutator(
    individualRate: Arb<Double>? = arbProbability(),
    chromosomeRate: Arb<Double>? = arbProbability(),
    shuffleBoundaryProbability: Arb<Double>? = arbProbability()
): Arb<PartialShuffleMutator<Int, IntGene>> = arbitrary {
    PartialShuffleMutator(
        individualRate = individualRate?.bind() ?: PartialShuffleMutator.DEFAULT_INDIVIDUAL_RATE,
        chromosomeRate = chromosomeRate?.bind() ?: PartialShuffleMutator.DEFAULT_CHROMOSOME_RATE,
        shuffleBoundaryProbability = shuffleBoundaryProbability?.bind()
            ?: PartialShuffleMutator.DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY
    )
}