/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

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
})
