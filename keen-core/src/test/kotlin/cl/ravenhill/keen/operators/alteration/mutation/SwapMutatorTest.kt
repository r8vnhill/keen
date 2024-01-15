/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.operators.swapMutator
import cl.ravenhill.keen.arb.randomContext
import cl.ravenhill.keen.assertions.operators.`test chromosome rate property`
import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.assertions.should.shouldBeInRange
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.utils.indices
import cl.ravenhill.keen.utils.swap
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class SwapMutatorTest : FreeSpec({

    include(`test individual rate property`(
        "SwapMutator.DEFAULT_INDIVIDUAL_RATE",
        SwapMutator.DEFAULT_INDIVIDUAL_RATE,
        { chromosomeRate, swapRate ->
            SwapMutator<Nothing, NothingGene>(
                chromosomeRate = chromosomeRate,
                swapRate = swapRate
            )
        }, ::SwapMutator
    ) { rate ->
        "The individual rate ($rate) must be in 0.0..1.0"
    })

    include(`test chromosome rate property`(
        "SwapMutator.DEFAULT_CHROMOSOME_RATE",
        SwapMutator.DEFAULT_CHROMOSOME_RATE,
        { individualRate, swapRate ->
            SwapMutator<Nothing, NothingGene>(
                individualRate = individualRate,
                swapRate = swapRate
            )
        },
        { chromosomeRate, individualRate, swapRate -> SwapMutator(individualRate, chromosomeRate, swapRate) }
    ) { rate ->
        "The chromosome rate ($rate) must be in 0.0..1.0"
    })

    "Mutating a chromosome" - {
        "when swap rate is 0.0" - {
            "should not alter the chromosome" {
                checkAll(
                    Arb.swapMutator<Int, IntGene>(swapRate = Arb.constant(0.0)),
                    Arb.intChromosome(size = Arb.int(1..5)),
                    Arb.randomContext()
                ) { mutator, chromosome, (_, rng) ->
                    Domain.random = rng
                    val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                    mutations shouldBe 0
                    mutated shouldBe chromosome
                }
            }

            "should swap all genes if the swap rate is 1.0" {
                checkAll(
                    Arb.swapMutator<Int, IntGene>(swapRate = Arb.constant(1.0)),
                    Arb.intChromosome(size = Arb.int(1..5)),
                    Arb.randomContext()
                ) { mutator, chromosome, (_, rng) ->
                    Domain.random = rng
                    val (_, mutations) = mutator.mutateChromosome(chromosome)
                    mutations shouldBe chromosome.genes.size
                }
            }

            "should swap the right amount of genes" {
                checkAll(
                    // We set the max failure rate to 200 because, so we expect 80% of the tests to pass. This is due
                    // to the fact that the swap is performed stochastically, so it is possible that the number of
                    // mutations is slightly higher or lower than the expected value.
                    PropTestConfig(maxFailure = 200, minSuccess = 800),
                    Arb.swapMutator<Int, IntGene>(),
                    Arb.intChromosome(size = Arb.int(1..10)),
                    Arb.randomContext()
                ) { mutator, chromosome, (_, rng) ->
                    Domain.random = rng
                    val (_, mutations) = mutator.mutateChromosome(chromosome)
                    val expectedMutations = (mutator.swapRate * chromosome.genes.size).toInt()
                    mutations shouldBeInRange (expectedMutations - 1)..(expectedMutations + 1)
                }
            }

            "should swap the right genes" {
                checkAll(
                    Arb.swapMutator<Int, IntGene>(),
                    Arb.intChromosome(size = Arb.int(1..10)),
                    Arb.randomContext()
                ) { mutator, chromosome, (seed, rng) ->
                    Domain.random = rng
                    val (mutated, _) = mutator.mutateChromosome(chromosome)
                    Domain.random = Random(seed)
                    val expected = mutateChromosome(mutator, chromosome)
                    mutated shouldBe expected.subject
                }
            }
        }
    }
})

/**
 * Recursively mutates a chromosome based on swap mutations.
 *
 * This function applies swap mutations to a given chromosome using a recursive approach. It iterates through a list
 * of indices and swaps the genes at these indices. The function ensures that each swap results in a mutation and
 * counts the total number of mutations applied.
 *
 * @param mutator The SwapMutator instance providing the mutation logic.
 * @param chromosome The chromosome to be mutated.
 * @param indices A list of indices for genes within the chromosome that are candidates for swapping.
 * @param currentIndex The current index in the recursive process, initially set to 0.
 * @param mutations The count of mutations applied so far, initially set to 0.
 * @return A ChromosomeMutationResult containing the mutated chromosome and the total number of mutations.
 */
private fun <T, G> mutateChromosome(
    mutator: SwapMutator<T, G>,
    chromosome: Chromosome<T, G>,
    indices: List<Int> = Domain.random.indices(mutator.swapRate, chromosome.genes.size),
    currentIndex: Int = 0,
    mutations: Int = 0,
): ChromosomeMutationResult<T, G> where G : Gene<T, G> {

    tailrec fun swapGenes(
        genes: MutableList<G>,
        currentIndex: Int,
        mutations: Int,
    ): ChromosomeMutationResult<T, G> = when {
        currentIndex >= indices.size -> {
            ChromosomeMutationResult(chromosome.duplicateWithGenes(genes), mutations)
        }

        else -> {
            val swapIndex = Domain.random.nextInt(genes.size)
            if (swapIndex != indices[currentIndex]) {
                genes.swap(indices[currentIndex], swapIndex)
                swapGenes(genes, currentIndex + 1, mutations + 1)
            } else {
                swapGenes(genes, currentIndex + 1, mutations)
            }
        }
    }
    return swapGenes(chromosome.toMutableList(), currentIndex, mutations)
}
