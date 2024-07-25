package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.arbRngPair
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.genetic.genes.arbDoubleGene
import cl.ravenhill.keen.arb.genetic.genes.arbIntGene
import cl.ravenhill.keen.assertions.`test Gene Mutator gene rate`
import cl.ravenhill.keen.assertions.`test Mutator chromosome rate property`
import cl.ravenhill.keen.assertions.`test Mutator individual rate property`
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
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
        "should generate the expected gene according to the random generator for" - {
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

    "Mutating a chromosome" - {
        "should return the same chromosome if the gene rate is 0" {
            checkAll(
                arbRandomMutator<Int, IntGene>(geneRate = Arb.constant(0.0)),
                arbIntChromosome()
            ) { mutator, chromosome ->
                val mutatedChromosome = mutator.mutateChromosome(chromosome)
                mutatedChromosome shouldBe chromosome
            }
        }

        "should return a chromosome with all genes mutated if the gene rate is 1" {
            checkAll(
                arbRandomMutator<Int, IntGene>(geneRate = Arb.constant(1.0)),
                arbIntChromosome(),
                arbRngPair()
            ) { mutator, chromosome, (domainRandom, expectedRandom) ->
                Domain.random = domainRandom
                val mutatedChromosome = mutator.mutateChromosome(chromosome)
                mutatedChromosome.forEach { gene ->
                    expectedRandom.nextDouble() // Consume the random generator to simulate gene selection
                    gene.value shouldBe expectedRandom.nextInt(gene.range.start, gene.range.endInclusive)
                }
            }
        }

        "should mutate approximately the expected number of genes based on the gene rate" {
            checkAll(
                PropTestConfig(iterations = 1000, maxFailure = 200, minSuccess = 800),
                arbRandomMutator<Int, IntGene>(geneRate = Arb.double(0.0, 1.0)),
                arbIntChromosome(),
                Arb.long()
            ) { mutator, chromosome, seed ->
                Domain.random = Random(seed)
                val mutatedChromosome = mutator.mutateChromosome(chromosome)
                val unchangedGenes =
                    chromosome.zip(mutatedChromosome).count { (original, mutated) -> original == mutated }
                val mutatedGenes = chromosome.size - unchangedGenes
                val expectedMutatedGenes = (chromosome.size * mutator.geneRate).toInt()
                mutatedGenes shouldBeInRange expectedMutatedGenes - 1..expectedMutatedGenes + 1
            }
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
