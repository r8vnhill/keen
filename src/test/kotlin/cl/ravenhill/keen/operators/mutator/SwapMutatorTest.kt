/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.genetic.intChromosome
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.arbs.probability
import cl.ravenhill.keen.assertions.operations.mutators.`validate unchanged chromosome with zero mutation rate`
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.mutator.strategies.SwapMutator
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll
import kotlin.random.Random

class SwapMutatorTest : FreeSpec({
    "A [SwapMutator]" - {
        "when created" - {
            "without explicit mutation probability defaults to 0.2" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { geneRate, chromosomeRate ->
                    val mutator = SwapMutator<Nothing, NothingGene>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate
                    )
                    mutator.probability shouldBe 0.2
                    mutator.chromosomeRate shouldBe chromosomeRate
                    mutator.geneRate shouldBe geneRate
                }
            }

            "without explicit chromosome rate defaults to 0.5" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { probability, geneRate ->
                    val mutator = SwapMutator<Nothing, NothingGene>(
                        probability,
                        geneRate = geneRate
                    )
                    mutator.probability shouldBe probability
                    mutator.chromosomeRate shouldBe 0.5
                    mutator.geneRate shouldBe geneRate
                }
            }

            "without explicit gene rate defaults to 0.5" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { probability, chromosomeRate ->
                    val mutator = SwapMutator<Nothing, NothingGene>(
                        probability,
                        chromosomeRate = chromosomeRate
                    )
                    mutator.probability shouldBe probability
                    mutator.chromosomeRate shouldBe chromosomeRate
                    mutator.geneRate shouldBe 0.5
                }
            }
        }

        "when mutating a chromosome" - {
            "should make no mutations if the chromosome rate is 0" {
                `validate unchanged chromosome with zero mutation rate`(
                    Arb.intChromosome()
                ) { probability, geneRate ->
                    SwapMutator(probability, chromosomeRate = 0.0, geneRate = geneRate)
                }
            }

            "should make no mutations if the gene rate is 0" {
                `validate unchanged chromosome with zero mutation rate`(
                    Arb.intChromosome()
                ) { probability, chromosomeRate ->
                    SwapMutator(
                        probability,
                        chromosomeRate = chromosomeRate,
                        geneRate = 0.0
                    )
                }
            }

            "should make no mutations if the chromosome has only one gene" {
                checkAll(
                    Arb.intGene(),
                    Arb.probability(),
                    Arb.probability(),
                    Arb.probability()
                ) { gene, probability, chromosomeRate, geneRate ->
                    val chromosome = IntChromosome(listOf(gene))
                    val mutator =
                        SwapMutator<Int, IntGene>(probability, chromosomeRate, geneRate)
                    val result = mutator.mutateChromosome(chromosome)
                    result.mutated shouldBe chromosome
                    result.mutations shouldBe 0
                }
            }

            "should mutate a gene according to the probability" - {
                withData(
                    nameFn = { "with chromosome rate ${it.chromosomeRate} and gene rate ${it.geneRate}" },
                    SwapMutationResult(
                        1.0,
                        1.0,
                        IntChromosome(listOf(IntGene(0), IntGene(1))),
                        MutatorResult(
                            IntChromosome(listOf(IntGene(0), IntGene(1))), 2
                        )
                    ),
                    /*
                     * I = 0, 1, 2
                     * Swaps = 0 -> 1, 1 -> 0, 2 -> 0
                     * O = 2, 1, 0
                     */
                    SwapMutationResult(
                        0.5,
                        0.5,
                        IntChromosome(
                            listOf(IntGene(0), IntGene(1), IntGene(2))
                        ),
                        MutatorResult(
                            IntChromosome(
                                listOf(
                                    IntGene(2), IntGene(1), IntGene(0)
                                )
                            ),
                            3
                        )
                    ),
                ) { (chromosomeRate, geneRate, input, expected) ->
                    Core.random = Random(11)
                    SwapMutator<Int, IntGene>(1.0, chromosomeRate, geneRate)
                        .mutateChromosome(input) shouldBe expected
                }
            }
        }
    }
}) {

    data class SwapMutationResult(
        val chromosomeRate: Double,
        val geneRate: Double,
        val chromosome: IntChromosome,
        val expected: MutatorResult<Int, IntGene, IntChromosome>
    )
}
