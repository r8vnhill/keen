/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.genetic.chromosomes.booleanChromosome
import cl.ravenhill.keen.arb.operators.bitFlipMutator
import cl.ravenhill.keen.arb.rngPair
import cl.ravenhill.keen.assertions.operators.`test chromosome rate property`
import cl.ravenhill.keen.assertions.operators.`test gene rate property`
import cl.ravenhill.keen.assertions.operators.`test individual rate property`
import cl.ravenhill.keen.genetic.genes.BooleanGene
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.constant
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class BitFlipMutatorTest : FreeSpec({
    include(`test individual rate property`(
        "BitFlipMutator.DEFAULT_INDIVIDUAL_RATE",
        BitFlipMutator.DEFAULT_INDIVIDUAL_RATE,
        { chromosomeRate, geneRate ->
            BitFlipMutator<BooleanGene>(
                chromosomeRate = chromosomeRate,
                geneRate = geneRate
            )
        }, ::BitFlipMutator
    ) { rate ->
        "The individual rate ($rate) must be in 0.0..1.0"
    })

    include(`test chromosome rate property`(
        "BitFlipMutator.DEFAULT_CHROMOSOME_RATE",
        BitFlipMutator.DEFAULT_CHROMOSOME_RATE,
        { individualRate, geneRate ->
            BitFlipMutator<BooleanGene>(
                individualRate = individualRate,
                geneRate = geneRate
            )
        },
        { chromosomeRate, individualRate, geneRate -> BitFlipMutator(individualRate, chromosomeRate, geneRate) }
    ) { rate ->
        "The chromosome rate ($rate) must be in 0.0..1.0"
    })

    include(`test gene rate property`(
        "BitFlipMutator.DEFAULT_GENE_RATE",
        BitFlipMutator.DEFAULT_GENE_RATE,
        { individualRate, chromosomeRate ->
            BitFlipMutator<BooleanGene>(
                individualRate = individualRate,
                chromosomeRate = chromosomeRate
            )
        },
        { geneRate, individualRate, chromosomeRate -> BitFlipMutator(individualRate, chromosomeRate, geneRate) }
    ) { rate ->
        "The gene rate ($rate) must be in 0.0..1.0"
    })

    "Can mutate a gene" {
        checkAll(Arb.bitFlipMutator<BooleanGene>()) { mutator ->
            mutator.mutateGene(BooleanGene.True) shouldBe BooleanGene.False
            mutator.mutateGene(BooleanGene.False) shouldBe BooleanGene.True
        }
    }

    "When mutating a chromosome" - {
        "should perform no mutations if the probability is 0" {
            checkAll(
                Arb.bitFlipMutator<BooleanGene>(geneRate = Arb.constant(0.0)),
                Arb.booleanChromosome()
            ) { mutator, chromosome ->
                val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                mutations shouldBe 0
                mutated shouldBe chromosome
            }
        }

        "should mutate all genes if the probability is 1" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetDomainListener)),
                Arb.bitFlipMutator<BooleanGene>(geneRate = Arb.constant(1.0)),
                Arb.booleanChromosome(),
            ) { mutator, chromosome ->
                val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                mutations shouldBe chromosome.size
                mutated.forEachIndexed { index, booleanGene ->
                    booleanGene shouldBe !chromosome[index]
                }
            }
        }

        "should mutate genes according to the gene rate" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetDomainListener)),
                Arb.bitFlipMutator<BooleanGene>(),
                Arb.booleanChromosome(),
                Arb.rngPair()
            ) { mutator, chromosome, (rng1, rng2) ->
                Domain.random = rng1
                val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                mutated.forEachIndexed { index, booleanGene ->
                    if (rng2.nextDouble() > mutator.geneRate) {
                        booleanGene shouldBe chromosome[index]
                    } else {
                        booleanGene shouldBe !chromosome[index]
                    }
                }
            }
        }
    }
})
