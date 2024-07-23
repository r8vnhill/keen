package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.arbRngPair
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.genes.arbDoubleGene
import cl.ravenhill.keen.arb.genetic.genes.arbIntGene
import cl.ravenhill.keen.assertions.`test Gene Mutator gene rate`
import cl.ravenhill.keen.assertions.`test Mutator individual rate property`
import cl.ravenhill.keen.assertions.`test Mutator chromosome rate property`
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.mixins.shouldHaveRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

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

    "Mutating a gene" - {
        "should generate the expected gene according to the random generator for" -{
            "an IntGene" {
                checkAll(
                    arbRandomMutator<Int, IntGene>(),
                    arbIntGene(),
                    arbRngPair()
                ) { mutator, gene, (domainRandom, expectedRandom) ->
                    Domain.random = domainRandom
                    val mutatedGene = mutator.mutateGene(gene)
                    mutatedGene.value shouldBe expectedRandom.nextInt(gene.range.start, gene.range.endInclusive)
                }
            }

            "a DoubleGene" {
                checkAll(
                    arbRandomMutator<Double, DoubleGene>(),
                    arbDoubleGene(),
                    arbRngPair()
                ) { mutator, gene, (domainRandom, expectedRandom) ->
                    Domain.random = domainRandom
                    val mutatedGene = mutator.mutateGene(gene)
                    mutatedGene.value shouldBe expectedRandom.nextDouble(gene.range.start, gene.range.endInclusive)
                }
            }

            // Tests for other gene types are omitted because the mutation strategy used by RandomMutator is defined
            // within each gene type. Consequently, the mutation logic is tested in the test suite of each specific gene
            // type.
        }
    }
})

private fun <T, G> arbRandomMutator(
    individualRate: Arb<Double> = arbProbability(),
    chromosomeRate: Arb<Double> = arbProbability(),
    geneRate: Arb<Double> = arbProbability()
): Arb<RandomMutator<T, G>> where G : Gene<T, G> = arbitrary {
    RandomMutator(
        individualRate = individualRate.bind(),
        chromosomeRate = chromosomeRate.bind(),
        geneRate = geneRate.bind()
    )
}
