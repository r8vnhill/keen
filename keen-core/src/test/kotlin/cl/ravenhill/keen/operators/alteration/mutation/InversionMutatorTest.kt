/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.assertions.operators.`test chromosome rate property`
import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.assertions.operators.`test rate property`
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec

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
})
