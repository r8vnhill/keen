package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.assertions.`test Gene Mutator gene rate`
import cl.ravenhill.keen.assertions.`test Mutator individual rate property`
import cl.ravenhill.keen.assertions.`test Mutator chromosome rate property`
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec

class RandomMutatorTest : FreeSpec({
    include(
        `test Mutator individual rate property`(
            "RandomMutator.DEFAULT_INDIVIDUAL_RATE" to RandomMutator.DEFAULT_INDIVIDUAL_RATE,
            { chromosomeRate, geneRate ->
                RandomMutator<Nothing, NothingGene>(
                    chromosomeRate = chromosomeRate,
                    geneRate = geneRate
                )
            },
            { individualRate, chromosomeRate, geneRate -> RandomMutator(individualRate, chromosomeRate, geneRate) }
        )
    )

    include(`test Mutator chromosome rate property`(
        "RandomMutator.DEFAULT_CHROMOSOME_RATE" to RandomMutator.DEFAULT_CHROMOSOME_RATE,
        { individualRate, geneRate ->
            RandomMutator<Nothing, NothingGene>(
                individualRate = individualRate,
                geneRate = geneRate
            )
        },
        { individualRate, chromosomeRate, geneRate -> RandomMutator(individualRate, chromosomeRate, geneRate) }
    ))

    include(`test Gene Mutator gene rate`(
        "RandomMutator.DEFAULT_GENE_RATE" to RandomMutator.DEFAULT_GENE_RATE,
        { individualRate, chromosomeRate ->
            RandomMutator<Nothing, NothingGene>(
                individualRate = individualRate,
                chromosomeRate = chromosomeRate
            )
        },
        { individualRate, chromosomeRate, geneRate -> RandomMutator(individualRate, chromosomeRate, geneRate) }
    ))
})
