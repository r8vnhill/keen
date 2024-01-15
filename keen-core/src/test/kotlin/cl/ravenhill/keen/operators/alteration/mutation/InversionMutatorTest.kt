/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.operators.inversionMutator
import cl.ravenhill.keen.arb.randomContext
import cl.ravenhill.keen.assertions.operators.`test chromosome rate property`
import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.assertions.operators.`test rate property`
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.utils.swap
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class InversionMutatorTest : FreeSpec({

    include(`test individual rate property`(
        "InversionMutator.DEFAULT_INDIVIDUAL_RATE",
        InversionMutator.DEFAULT_INDIVIDUAL_RATE,
        { chromosomeRate, inversionBoundaryProbability ->
            InversionMutator<Nothing, NothingGene>(
                chromosomeRate = chromosomeRate,
                inversionBoundaryProbability = inversionBoundaryProbability
            )
        }, ::InversionMutator
    ) { rate ->
        "The individual rate ($rate) must be in 0.0..1.0"
    })

    include(`test chromosome rate property`(
        "InversionMutator.DEFAULT_CHROMOSOME_RATE",
        InversionMutator.DEFAULT_CHROMOSOME_RATE,
        { individualRate, inversionBoundaryProbability ->
            InversionMutator<Nothing, NothingGene>(
                individualRate = individualRate,
                inversionBoundaryProbability = inversionBoundaryProbability
            )
        },
        { chromosomeRate, individualRate, inversionBoundaryProbability ->
            InversionMutator(individualRate, chromosomeRate, inversionBoundaryProbability)
        }
    ) { rate ->
        "The chromosome rate ($rate) must be in 0.0..1.0"
    })

    "Inversion boundary probability" - {
        `test rate property`(
            "InversionMutator.DEFAULT_INVERSION_BOUNDARY_PROBABILITY",
            InversionMutator.DEFAULT_INVERSION_BOUNDARY_PROBABILITY,
            { individualRate, chromosomeRate ->
                InversionMutator<Nothing, NothingGene>(
                    individualRate = individualRate,
                    chromosomeRate = chromosomeRate
                )
            },
            { inversionBoundaryProbability, individualRate, chromosomeRate ->
                InversionMutator(individualRate, chromosomeRate, inversionBoundaryProbability)
            },
            { rate -> "The inversion boundary probability ($rate) must be in 0.0..1.0" }
        ) { inversionBoundaryProbability }
    }

    "Mutating a chromosome" - {
        "when inversion boundary probability is 0.0" - {
            "should not invert the chromosome" {
                checkAll(
                    Arb.inversionMutator<Int, IntGene>(inversionBoundaryProbability = Arb.constant(0.0)),
                    Arb.intChromosome()
                ) { mutator, chromosome ->
                    val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                    withClue("Number of mutations") { mutations shouldBe 0 }
                    mutated shouldBe chromosome
                }
            }
        }

        "when inversion boundary probability is 1.0" - {
            "should invert the entire chromosome" {
                checkAll(
                    Arb.inversionMutator<Int, IntGene>(inversionBoundaryProbability = Arb.constant(1.0)),
                    Arb.intChromosome()
                ) { mutator, chromosome ->
                    val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                    withClue("Number of mutations") { mutations shouldBe chromosome.genes.size }
                    mutated shouldBe chromosome.reversed()
                }
            }
        }

        "should invert the right amount of genes" {
            checkAll(
                PropTestConfig(minSuccess = 800, maxFailure = 200),
                Arb.inversionMutator<Int, IntGene>(),
                Arb.intChromosome(Arb.int(0..10)),
                Arb.randomContext()
            ) { mutator, chromosome, (_, rng) ->
                Domain.random = rng
                val (_, mutations) = mutator.mutateChromosome(chromosome)
                // FIXME! This is not a correct calculation of the expected number of mutations.
                val expectedMutations = (chromosome.size * mutator.inversionBoundaryProbability).toInt()
                withClue("Number of mutations") {
                    mutations shouldBeIn expectedMutations - 1..expectedMutations + 1
                }
            }
        }

        "is idempotent over two inversions" {
            checkAll(
                Arb.inversionMutator<Int, IntGene>(),
                Arb.intChromosome(Arb.int(0..10)),
                Arb.randomContext()
            ) { mutator, chromosome, (seed, rng) ->
                Domain.toStringMode = ToStringMode.SIMPLE
                Domain.random = rng
                val (mutated1, _) = mutator.mutateChromosome(chromosome)
                Domain.random = Random(seed)
                val mutated2 = mutator.mutateChromosome(mutated1)
                chromosome shouldBe mutated2.subject
            }
        }

        "should invert the expected genes" {
            checkAll(
                Arb.inversionMutator<Int, IntGene>(),
                Arb.intChromosome(Arb.int(0..10)),
                Arb.randomContext()
            ) { mutator, chromosome, (seed, rng) ->
                Domain.toStringMode = ToStringMode.SIMPLE
                Domain.random = rng
                val (mutated, _) = mutator.mutateChromosome(chromosome)
                Domain.random = Random(seed)
                val expected = mutateChromosome(mutator, chromosome)
                mutated shouldBe expected
            }
        }
    }
})

private fun mutateChromosome(
    mutator: InversionMutator<Int, IntGene>,
    chromosome: Chromosome<Int, IntGene>,
): Chromosome<Int, IntGene> {
    val genes = chromosome.genes.toMutableList()
    val (start, end) = mutator.getInversionBoundary(chromosome.indices)
    return chromosome.duplicateWithGenes(invert(genes, start, end))
}

private fun <T, G> invert(genes: List<G>, start: Int, end: Int): List<G> where G : Gene<T, G> {
    val invertedGenes = genes.toMutableList()

    // Iterate over half the range of genes to swap their positions.
    for (i in start until (start + (end - start + 1) / 2)) {
        // Calculate the corresponding index to swap with.
        val j = end - (i - start)
        // Swap the positions of the genes at indices i and j.
        invertedGenes.swap(i, j)
    }

    return invertedGenes
}