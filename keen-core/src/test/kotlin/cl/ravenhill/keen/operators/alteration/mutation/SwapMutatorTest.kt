package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class SwapMutatorTest : FreeSpec({
    "A Swap Mutator instance" - {
        "should use default parameters when none are provided" {
            SwapMutator<Nothing, NothingGene>().apply {
                individualRate shouldBe SwapMutator.DEFAULT_INDIVIDUAL_RATE
                chromosomeRate shouldBe SwapMutator.DEFAULT_CHROMOSOME_RATE
                swapRate shouldBe SwapMutator.DEFAULT_SWAP_RATE
            }
        }

        "should have an individual rate property that" - {
            "defaults to DEFAULT_INDIVIDUAL_RATE" {
                checkAll(arbProbability()) { individualRate ->
                    SwapMutator<Nothing, NothingGene>(individualRate = individualRate).let {
                        it.individualRate shouldBe individualRate
                        it.chromosomeRate shouldBe SwapMutator.DEFAULT_CHROMOSOME_RATE
                        it.swapRate shouldBe SwapMutator.DEFAULT_SWAP_RATE
                    }
                }
            }
        }

        "should have a chromosome rate property that" - {
            "defaults to DEFAULT_CHROMOSOME_RATE" {
                checkAll(arbProbability()) { chromosomeRate ->
                    SwapMutator<Nothing, NothingGene>(chromosomeRate = chromosomeRate).let {
                        it.individualRate shouldBe SwapMutator.DEFAULT_INDIVIDUAL_RATE
                        it.chromosomeRate shouldBe chromosomeRate
                        it.swapRate shouldBe SwapMutator.DEFAULT_SWAP_RATE
                    }
                }
            }
        }

        "should have a swap rate property that" - {
            "defaults to DEFAULT_SWAP_RATE" {
                checkAll(arbProbability()) { swapRate ->
                    SwapMutator<Nothing, NothingGene>(swapRate = swapRate).let {
                        it.individualRate shouldBe SwapMutator.DEFAULT_INDIVIDUAL_RATE
                        it.chromosomeRate shouldBe SwapMutator.DEFAULT_CHROMOSOME_RATE
                        it.swapRate shouldBe swapRate
                    }
                }
            }
        }
    }
})