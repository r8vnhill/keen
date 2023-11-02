/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.genetic.intChromosome
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.arbs.probability
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.mutator.strategies.InversionMutator
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll
import kotlin.random.Random

class InversionMutatorTest : FreeSpec({
    "An [InversionMutator]" - {
        "when created" - {
            "without explicit mutation probability defaults to 0.5" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { inverseRate, chromosomeRate ->
                    val mutator = InversionMutator<Int, IntGene>(
                        chromosomeRate = chromosomeRate,
                        inversionBoundaryProbability = inverseRate
                    )
                    mutator.probability shouldBe 0.5
                    mutator.chromosomeRate shouldBe chromosomeRate
                    mutator.inversionBoundaryProbability shouldBe inverseRate
                }
            }

            "without explicit chromosome rate defaults to 0.5" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { probability, inverseRate ->
                    val mutator = InversionMutator<Int, IntGene>(
                        probability,
                        inversionBoundaryProbability = inverseRate
                    )
                    mutator.probability shouldBe probability
                    mutator.chromosomeRate shouldBe 0.5
                    mutator.inversionBoundaryProbability shouldBe inverseRate
                }
            }

            "without explicit boundary probability defaults to 0.5" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { probability, chromosomeRate ->
                    val mutator = InversionMutator<Int, IntGene>(
                        probability,
                        chromosomeRate = chromosomeRate
                    )
                    mutator.probability shouldBe probability
                    mutator.chromosomeRate shouldBe chromosomeRate
                    mutator.inversionBoundaryProbability shouldBe 0.5
                }
            }
        }

        "should make no mutations if the boundary probability is 0" {
            checkAll(
                Arb.intChromosome(),
                Arb.probability(),
                Arb.probability()
            ) { chromosome, probability, chromosomeRate ->
                val mutator =
                    InversionMutator<Int, IntGene>(probability, chromosomeRate, 0.0)
                mutator.mutateChromosome(chromosome) shouldBe MutatorResult(chromosome)
            }
        }

        "should make no mutations if the chromosome has only one gene" {
            checkAll(
                Arb.intGene(),
                Arb.probability(),
                Arb.probability(),
                Arb.probability()
            ) { gene, probability, chromosomeRate, inverseRate ->
                val chromosome = IntChromosome(listOf(gene))
                val mutator =
                    InversionMutator<Int, IntGene>(
                        probability,
                        chromosomeRate,
                        inverseRate
                    )
                mutator.mutateChromosome(chromosome) shouldBe MutatorResult(chromosome)
            }
        }

        "should mutate a chromosome according to probability" - {
            withData(
                nameFn = {
                    "with chromosome rate ${it.chromosomeRate} and boundary probability ${it.boundaryProbability}"
                },
                InversionMutationResult(
                    1.0,
                    1.0,
                    IntChromosome(IntGene(0), IntGene(1)),
                    MutatorResult(
                        IntChromosome(IntGene(1), IntGene(0)),
                        1
                    )
                ),
                /*
                 * I = 0, 1, 2
                 * Inversion: 0, 1 -> 1, 0
                 * O = 1, 0, 2
                 */
                InversionMutationResult(
                    0.5,
                    0.5,
                    IntChromosome(IntGene(0), IntGene(1), IntGene(2)),
                    MutatorResult(
                        IntChromosome(IntGene(1), IntGene(0), IntGene(2)),
                        1
                    )
                ),
            ) { (chromosomeRate, boundaryProbability, input, expected) ->
                Core.random = Random(11)
                InversionMutator<Int, IntGene>(
                    1.0, chromosomeRate, boundaryProbability
                ).mutateChromosome(input) shouldBe expected
            }
        }
    }
}) {
    data class InversionMutationResult(
        val chromosomeRate: Double,
        val boundaryProbability: Double,
        val chromosome: IntChromosome,
        val result: MutatorResult<Int, IntGene, IntChromosome>
    )
}
