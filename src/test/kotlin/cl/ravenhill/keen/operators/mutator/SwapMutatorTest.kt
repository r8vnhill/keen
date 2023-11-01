/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.arbs.probability
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.operators.mutator.strategies.SwapMutator
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

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
    }
})
