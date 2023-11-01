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
                    SwapMutator<Nothing, NothingGene>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate
                    ).probability shouldBe 0.2
                }
            }

            "without explicit chromosome rate defaults to 0.5" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { probability, geneRate ->
                    SwapMutator<Nothing, NothingGene>(
                        probability,
                        geneRate = geneRate
                    ).chromosomeRate shouldBe 0.5
                }
            }

            "without explicit gene rate defaults to 0.5" {
                checkAll(
                    Arb.probability(),
                    Arb.probability()
                ) { probability, chromosomeRate ->
                    SwapMutator<Nothing, NothingGene>(
                        probability,
                        chromosomeRate = chromosomeRate
                    ).geneRate shouldBe 0.5
                }
            }
        }
    }
})
