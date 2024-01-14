/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.operators.swapMutator
import cl.ravenhill.keen.assertions.operators.`test chromosome rate property`
import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

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
                    Arb.long()
                ) { mutator, chromosome, seed ->
                    Domain.random = Random(seed)
                    val mutated = mutator.mutateChromosome(chromosome)
                    mutated shouldBe chromosome
                }
            }
        }
    }
})
