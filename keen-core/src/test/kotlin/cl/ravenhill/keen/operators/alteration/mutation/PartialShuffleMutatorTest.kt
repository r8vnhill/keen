package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.freeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class PartialShuffleMutatorTest : FreeSpec({
    include(`operator construction`())
})

private fun `operator construction`() = freeSpec {
    "When constructing" - {
        `throws an exception on invalid rates`()
        `creates a mutator on valid rates`()
    }
}

private suspend fun FreeSpecContainerScope.`throws an exception on invalid rates`() {
    "should throw an exception if the individual rate is not in 0..1" {
        checkAll(arbInvalidProbability(), Arb.double()) { invalidProbability, chromosomeRate ->
            shouldThrow<CompositeException> {
                PartialShuffleMutator<Int, IntGene>(invalidProbability, chromosomeRate)
            }.shouldHaveInfringement<MutatorConfigException>(
                "Individual mutation rate must be in the range [0, 1]"
            )
        }
    }

    "should throw an exception if the chromosome rate is not in 0..1" {
        checkAll(Arb.double(), arbInvalidProbability()) { individualRate, invalidProbability ->
            shouldThrow<CompositeException> {
                PartialShuffleMutator<Int, IntGene>(individualRate, invalidProbability)
            }.shouldHaveInfringement<MutatorConfigException>(
                "Chromosome mutation rate must be in the range [0, 1]"
            )
        }
    }

}

private suspend fun FreeSpecContainerScope.`creates a mutator on valid rates`() {
    "with default individual and chromosome rates of 1.0" {
        with(PartialShuffleMutator<Int, IntGene>()) {
            individualRate shouldBe 1.0
            chromosomeRate shouldBe 1.0
        }
    }

    "with custom individual rate" - {
        checkAll(arbProbability()) { rate ->
            with(PartialShuffleMutator<Int, IntGene>(rate)) {
                individualRate shouldBe rate
                chromosomeRate shouldBe 1.0
            }
        }
    }

    "with custom chromosome rate" - {
        checkAll(arbProbability()) { rate ->
            with(PartialShuffleMutator<Int, IntGene>(chromosomeRate = rate)) {
                individualRate shouldBe 1.0
                chromosomeRate shouldBe rate
            }
        }
    }
}
