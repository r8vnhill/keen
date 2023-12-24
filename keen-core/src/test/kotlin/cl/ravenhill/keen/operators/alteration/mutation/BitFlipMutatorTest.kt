/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainRandomListener
import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.arb.genetic.chromosomes.booleanChromosome
import cl.ravenhill.keen.arb.operators.bitFlipMutator
import cl.ravenhill.keen.arb.rngPair
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.BooleanGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class BitFlipMutatorTest : FreeSpec({

    "A Bit-Flip Mutator instance" - {
        "should have an individual rate property that" - {
            "defaults to [BitFlipMutator.DEFAULT_INDIVIDUAL_RATE]" {
                checkAll(Arb.probability(), Arb.probability()) { chromosomeRate, geneRate ->
                    val mutator = BitFlipMutator<BooleanGene>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate
                    )
                    mutator.individualRate shouldBe BitFlipMutator.DEFAULT_INDIVIDUAL_RATE
                }
            }

            "can be set to a value between 0 and 1" {
                checkAll(
                    Arb.probability(),
                    Arb.probability(),
                    Arb.probability()
                ) { individualRate, chromosomeRate, geneRate ->
                    val mutator = BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    mutator.individualRate shouldBe individualRate
                }
            }

            "should throw an exception if set to a value that's not between 0 and 1" {
                checkAll(
                    Arb.double().filterNot { it in 0.0..1.0 },
                    Arb.double(),
                    Arb.double()
                ) { individualRate, chromosomeRate, geneRate ->
                    shouldThrow<CompositeException> {
                        BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    }.shouldHaveInfringement<MutatorConfigException>(
                        "The individual rate ($individualRate) must be in 0.0..1.0"
                    )
                }
            }
        }

        "should have a chromosome rate property that" - {
            "defaults to [BitFlipMutator.DEFAULT_CHROMOSOME_RATE]" {
                checkAll(Arb.probability(), Arb.probability()) { individualRate, geneRate ->
                    val mutator = BitFlipMutator<BooleanGene>(
                        individualRate = individualRate,
                        geneRate = geneRate
                    )
                    mutator.chromosomeRate shouldBe BitFlipMutator.DEFAULT_CHROMOSOME_RATE
                }
            }

            "can be set to a value between 0 and 1" {
                checkAll(
                    Arb.probability(),
                    Arb.probability(),
                    Arb.probability()
                ) { individualRate, chromosomeRate, geneRate ->
                    val mutator = BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    mutator.chromosomeRate shouldBe chromosomeRate
                }
            }

            "should throw an exception if set to a value that's not between 0 and 1" {
                checkAll(
                    Arb.double(),
                    Arb.double().filterNot { it in 0.0..1.0 },
                    Arb.double()
                ) { individualRate, chromosomeRate, geneRate ->
                    shouldThrow<CompositeException> {
                        BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    }.shouldHaveInfringement<MutatorConfigException>(
                        "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"
                    )
                }
            }
        }

        "should have a gene rate property that" - {
            "defaults to [BitFlipMutator.DEFAULT_GENE_RATE]" {
                checkAll(Arb.probability(), Arb.probability()) { individualRate, chromosomeRate ->
                    val mutator = BitFlipMutator<BooleanGene>(
                        individualRate = individualRate,
                        chromosomeRate = chromosomeRate
                    )
                    mutator.geneRate shouldBe BitFlipMutator.DEFAULT_GENE_RATE
                }
            }

            "can be set to a value between 0 and 1" {
                checkAll(
                    Arb.probability(),
                    Arb.probability(),
                    Arb.probability()
                ) { individualRate, chromosomeRate, geneRate ->
                    val mutator = BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    mutator.geneRate shouldBe geneRate
                }
            }

            "should throw an exception if set to a value that's not between 0 and 1" {
                checkAll(
                    Arb.double(),
                    Arb.double(),
                    Arb.double().filterNot { it in 0.0..1.0 }
                ) { individualRate, chromosomeRate, geneRate ->
                    shouldThrow<CompositeException> {
                        BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    }.shouldHaveInfringement<MutatorConfigException>(
                        "The gene rate ($geneRate) must be in 0.0..1.0"
                    )
                }
            }
        }
    }

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
                mutator.mutateChromosome(chromosome) shouldBe chromosome
            }
        }

        "should mutate all genes if the probability is 1" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetDomainRandomListener)),
                Arb.bitFlipMutator<BooleanGene>(geneRate = Arb.constant(1.0)),
                Arb.booleanChromosome(),
            ) { mutator, chromosome ->
                val mutated = mutator.mutateChromosome(chromosome)
                mutated.forEachIndexed { index, booleanGene ->
                    booleanGene shouldBe !chromosome[index]
                }
            }
        }

        "should mutate genes according to the gene rate" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetDomainRandomListener)),
                Arb.bitFlipMutator<BooleanGene>(),
                Arb.booleanChromosome(),
                Arb.rngPair()
            ) { mutator, chromosome, (rng1, rng2) ->
                Domain.random = rng1
                val mutated = mutator.mutateChromosome(chromosome)
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
