/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.operators.alteration.mutation.SwapMutator
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

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
})
