package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutationException
import cl.ravenhill.keen.exceptions.MutatorConfigurationException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.random.Random

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

        "should throw an exception if" - {
            "the individual rate is not within the range [0.0, 1.0]" {
                checkAll(
                    arbInvalidProbability(),
                    arbProbability(),
                    arbProbability()
                ) { individualRate, chromosomeRate, swapRate ->
                    shouldThrow<CompositeException> {
                        SwapMutator<Nothing, NothingGene>(
                            individualRate = individualRate,
                            chromosomeRate = chromosomeRate,
                            swapRate = swapRate
                        )
                    }.shouldHaveInfringement<MutatorConfigurationException>(
                        "The individual rate [$individualRate] must be in 0.0..1.0"
                    )
                }
            }

            "the chromosome rate is not within the range [0.0, 1.0]" {
                checkAll(
                    arbProbability(),
                    arbInvalidProbability(),
                    arbProbability()
                ) { individualRate, chromosomeRate, swapRate ->
                    shouldThrow<CompositeException> {
                        SwapMutator<Nothing, NothingGene>(
                            individualRate = individualRate,
                            chromosomeRate = chromosomeRate,
                            swapRate = swapRate
                        )
                    }.shouldHaveInfringement<MutatorConfigurationException>(
                        "The chromosome rate [$chromosomeRate] must be in 0.0..1.0"
                    )
                }
            }

            "the swap rate is not within the range [0.0, 1.0]" {
                checkAll(
                    arbProbability(),
                    arbProbability(),
                    arbInvalidProbability()
                ) { individualRate, chromosomeRate, swapRate ->
                    shouldThrow<CompositeException> {
                        SwapMutator<Nothing, NothingGene>(
                            individualRate = individualRate,
                            chromosomeRate = chromosomeRate,
                            swapRate = swapRate
                        )
                    }.shouldHaveInfringement<MutatorConfigurationException>(
                        "The swap rate [$swapRate] must be in 0.0..1.0"
                    )
                }
            }
        }

        "when mutating a chromosome" - {
            "should throw an exception if the chromosome is empty" {
                checkAll(arbSwapMutator<Int, IntGene>()) { mutator ->
                    shouldThrow<CompositeException> {
                        mutator.mutateChromosome(IntChromosome.empty())
                    }.shouldHaveInfringement<MutationException>(
                        "The chromosome must not be empty"
                    )
                }
            }

            "should not change the chromosome if the swap rate is 0.0" {
                checkAll(
                    arbSwapMutator<Int, IntGene>(swapRate = Arb.constant(0.0)),
                    arbIntChromosome(size = Arb.int(1..100))
                ) { mutator, chromosome ->
                    mutator.mutateChromosome(chromosome) shouldBe chromosome
                }
            }

            "should maintain the same genes in any order" {
                checkAll(
                    arbSwapMutator<Int, IntGene>(),
                    arbIntChromosome(size = Arb.int(1..100))
                ) { mutator, chromosome ->
                    val genes = chromosome.genes
                    val mutated = mutator.mutateChromosome(chromosome).genes
                    mutated shouldContainExactlyInAnyOrder genes
                }
            }

            "should swap genes in the chromosome" - {
                Domain.toStringMode = ToStringMode.SIMPLE
                withData(
                    nameFn = { "${it.inChromosome} -> ${it.outChromosome}" },
                    SwapMutationData(
                        inChromosome = IntChromosome(1, 2, 3, 4, 5),
                        mutator = SwapMutator(swapRate = 1.0),
                        seed = 0L,
                        outChromosome = IntChromosome(4, 5, 3, 2, 1)
                    ),
                    SwapMutationData(
                        inChromosome = IntChromosome(1, 2, 3, 4, 5),
                        mutator = SwapMutator(swapRate = 0.5),
                        seed = 420L,
                        outChromosome = IntChromosome(2, 5, 4, 1, 3)
                    ),
                    SwapMutationData(
                        inChromosome = IntChromosome(1, 2, 3, 4, 5),
                        mutator = SwapMutator(swapRate = 0.25),
                        seed = 69L,
                        outChromosome = IntChromosome(1, 2, 5, 4, 3)
                    )
                ) { swapData ->
                    with (swapData) {
                        Domain.random = Random(seed)
                        mutator.mutateChromosome(inChromosome) shouldBe outChromosome
                    }
                }
            }
        }
    }
})

fun <T, G> arbSwapMutator(
    individualRate: Arb<Double> = arbProbability(),
    chromosomeRate: Arb<Double> = arbProbability(),
    swapRate: Arb<Double> = arbProbability()
) where G : Gene<T, G> = arbitrary {
    SwapMutator<T, G>(
        individualRate = individualRate.bind(),
        chromosomeRate = chromosomeRate.bind(),
        swapRate = swapRate.bind()
    )
}

private data class SwapMutationData<T, G>(
    val inChromosome: Chromosome<T, G>,
    val mutator: SwapMutator<T, G>,
    val seed: Long,
    val outChromosome: Chromosome<T, G>,
) where G : Gene<T, G>
