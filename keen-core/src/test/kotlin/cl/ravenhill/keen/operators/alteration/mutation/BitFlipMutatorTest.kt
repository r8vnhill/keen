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
import cl.ravenhill.keen.assertions.`test Gene Mutator gene rate`
import cl.ravenhill.keen.assertions.`test Mutator individual rate property`
import cl.ravenhill.keen.assertions.`test Mutator chromosome rate property`
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

    include(
        `test Mutator individual rate property`(
            "BitFlipMutator.DEFAULT_INDIVIDUAL_RATE" to BitFlipMutator.DEFAULT_INDIVIDUAL_RATE,
            { chromosomeRate, geneRate ->
                BitFlipMutator<BooleanGene>(
                    chromosomeRate = chromosomeRate,
                    geneRate = geneRate
                )
            },
            { individualRate, chromosomeRate, geneRate -> BitFlipMutator(individualRate, chromosomeRate, geneRate) }
        )
    )

    include(`test Mutator chromosome rate property`(
        "BitFlipMutator.DEFAULT_CHROMOSOME_RATE" to BitFlipMutator.DEFAULT_CHROMOSOME_RATE,
        { individualRate, geneRate ->
            BitFlipMutator<BooleanGene>(
                individualRate = individualRate,
                geneRate = geneRate
            )
        },
        { individualRate, chromosomeRate, geneRate -> BitFlipMutator(individualRate, chromosomeRate, geneRate) }
    ))

    include(`test Gene Mutator gene rate`(
        "BitFlipMutator.DEFAULT_GENE_RATE" to BitFlipMutator.DEFAULT_GENE_RATE,
        { individualRate, chromosomeRate ->
            BitFlipMutator<BooleanGene>(
                individualRate = individualRate,
                chromosomeRate = chromosomeRate
            )
        },
        { individualRate, chromosomeRate, geneRate -> BitFlipMutator(individualRate, chromosomeRate, geneRate) }
    ))

    "A Bit-Flip Mutator instance" - {
        "can mutate a gene" {
            checkAll(Arb.bitFlipMutator<BooleanGene>()) { mutator ->
                mutator.mutateGene(BooleanGene.True) shouldBe BooleanGene.False
                mutator.mutateGene(BooleanGene.False) shouldBe BooleanGene.True
            }
        }

        "when mutating a chromosome" - {
            "should perform no mutations if the gene rate is set to 0" {
                checkAll(
                    Arb.bitFlipMutator<BooleanGene>(geneRate = Arb.constant(0.0)),
                    Arb.booleanChromosome()
                ) { mutator, chromosome ->
                    mutator.mutateChromosome(chromosome) shouldBe chromosome
                }
            }

            "should mutate all genes if the gene rate is set to 1" {
                checkAll(
                    Arb.bitFlipMutator<BooleanGene>(geneRate = Arb.constant(1.0)),
                    Arb.booleanChromosome()
                ) { mutator, chromosome ->
                    mutator.mutateChromosome(chromosome) shouldBe chromosome.map { !it }
                }
            }

            "should mutate some genes if the gene rate is set to a value between 0 and 1" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.bitFlipMutator<BooleanGene>(),
                    Arb.booleanChromosome(),
                    Arb.rngPair()
                ) { mutator, chromosome, (rng1, rng2) ->
                    Domain.random = rng1
                    val mutated = mutator.mutateChromosome(chromosome)
                    mutated.forEachIndexed { index, gene ->
                        if (rng2.nextDouble() < mutator.geneRate) {
                            gene shouldBe !chromosome[index]
                        } else {
                            gene shouldBe chromosome[index]
                        }
                    }
                }
            }
        }
    }
})
