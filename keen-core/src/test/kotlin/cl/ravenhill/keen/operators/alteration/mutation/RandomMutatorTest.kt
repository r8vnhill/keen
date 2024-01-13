/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.genes.intGene
import cl.ravenhill.keen.arb.operators.randomMutator
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.assertions.operators.`test chromosome rate property`
import cl.ravenhill.keen.assertions.operators.`test gene rate property`
import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlin.random.Random

class RandomMutatorTest : FreeSpec({

    include(`test individual rate property`(
        "RandomMutator.DEFAULT_INDIVIDUAL_RATE",
        RandomMutator.DEFAULT_INDIVIDUAL_RATE,
        { chromosomeRate, geneRate ->
            RandomMutator<Nothing, NothingGene>(
                chromosomeRate = chromosomeRate,
                geneRate = geneRate
            )
        }, ::RandomMutator
    ) { rate ->
        "The individual rate ($rate) must be in 0.0..1.0"
    })

    include(`test chromosome rate property`(
        "RandomMutator.DEFAULT_CHROMOSOME_RATE",
        RandomMutator.DEFAULT_CHROMOSOME_RATE,
        { individualRate, geneRate ->
            RandomMutator<Nothing, NothingGene>(
                individualRate = individualRate,
                geneRate = geneRate
            )
        },
        { chromosomeRate, individualRate, geneRate -> RandomMutator(individualRate, chromosomeRate, geneRate) }
    ) { rate ->
        "The chromosome rate ($rate) must be in 0.0..1.0"
    })

    include(`test gene rate property`(
        "RandomMutator.DEFAULT_GENE_RATE",
        RandomMutator.DEFAULT_GENE_RATE,
        { individualRate, chromosomeRate ->
            RandomMutator<Nothing, NothingGene>(
                individualRate = individualRate,
                chromosomeRate = chromosomeRate
            )
        },
        { geneRate, individualRate, chromosomeRate -> RandomMutator(individualRate, chromosomeRate, geneRate) }
    ) { rate ->
        "The gene rate ($rate) must be in 0.0..1.0"
    })

    "Can mutate a gene" {
        checkAll(
            Arb.intGene(),
            Arb.randomMutator<Int, IntGene>(),
            Arb.long().map { seed -> Random(seed) to Random(seed) }
        ) { intGene, mutator, (r1, r2) ->
            Domain.random = r1
            val mutated = mutator.mutateGene(intGene)
            val expected = r2.nextInt(intGene.range.start, intGene.range.endInclusive)
            mutated shouldBe IntGene(expected, intGene.range)
        }
    }

    "Can mutate a chromosome" - {
        "when the gene rate is 1.0 mutates all genes" {
            checkAll(
                Arb.randomMutator<Int, IntGene>(geneRate = Arb.constant(1.0)),
                Arb.intChromosome(),
                Arb.long().map { seed -> Random(seed) to Random(seed) }
            ) { mutator, intChromosome, (r1, r2) ->
                Domain.random = r1
                val mutated = mutator.mutateChromosome(intChromosome)
                val expected = intChromosome.genes.map {
                    r2.nextDouble() // Advances the random number generator
                    r2.nextInt(it.range.start, it.range.endInclusive)
                }
                mutated.genes.map { it.value } shouldBe expected
            }
        }

        "when the gene rate is 0.0 doesn't mutate any gene" {
            checkAll(
                Arb.randomMutator<Int, IntGene>(geneRate = Arb.constant(0.0)),
                Arb.intChromosome(),
                Arb.random()
            ) { mutator, intChromosome, rng ->
                Domain.random = rng
                val mutated = mutator.mutateChromosome(intChromosome)
                mutated shouldBe intChromosome
            }
        }

        "according to an arbitrary gene rate" {
            checkAll(
                Arb.randomMutator<Int, IntGene>(),
                Arb.intChromosome(),
                Arb.long().map { seed -> Random(seed) to Random(seed) }
            ) { mutator, intChromosome, (r1, r2) ->
                Domain.random = r1
                val mutated = mutator.mutateChromosome(intChromosome)
                val expected = intChromosome.genes.map {
                    if (r2.nextDouble() < mutator.geneRate) {
                        r2.nextInt(it.range.start, it.range.endInclusive)
                    } else {
                        it.value
                    }
                }
                mutated.genes.map { it.value } shouldBe expected
            }
        }
    }
})
