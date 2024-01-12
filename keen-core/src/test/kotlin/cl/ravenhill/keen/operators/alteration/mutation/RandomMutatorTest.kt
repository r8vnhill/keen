/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.withEdgecases
import io.kotest.property.checkAll
import kotlin.random.Random

class RandomMutatorTest : FreeSpec({

    "Should have an individual rate property that" - {
        "defaults to [RandomMutator.DEFAULT_INDIVIDUAL_RATE]" {
            checkAll(Arb.probability(), Arb.probability()) { chromosomeRate, geneRate ->
                RandomMutator<Nothing, NothingGene>(
                    chromosomeRate = chromosomeRate,
                    geneRate = geneRate
                ).individualRate shouldBe RandomMutator.DEFAULT_INDIVIDUAL_RATE
            }
        }

        "can be set to a value between 0 and 1" {
            checkAll(
                Arb.probability(),
                Arb.probability(),
                Arb.probability()
            ) { individualRate, chromosomeRate, geneRate ->
                RandomMutator<Nothing, NothingGene>(
                    individualRate,
                    chromosomeRate,
                    geneRate
                ).individualRate shouldBe individualRate
            }
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            checkAll(
                Arb.double().filter { it !in 0.0..1.0 }.withEdgecases(),
                Arb.probability(),
                Arb.probability(),
            ) { individualRate, chromosomeRate, geneRate ->
                shouldThrow<CompositeException> {
                    RandomMutator<Nothing, NothingGene>(
                        individualRate,
                        chromosomeRate,
                        geneRate
                    )
                }.shouldHaveInfringement<MutatorConfigException>(
                    "The individual rate ($individualRate) must be in 0.0..1.0"
                )
            }
        }
    }

    "Should have a chromosome rate property that" - {
        "defaults to [RandomMutator.DEFAULT_CHROMOSOME_RATE]" {
            checkAll(Arb.probability(), Arb.probability()) { individualRate, geneRate ->
                RandomMutator<Nothing, NothingGene>(
                    individualRate,
                    geneRate = geneRate
                ).chromosomeRate shouldBe RandomMutator.DEFAULT_CHROMOSOME_RATE
            }
        }

        "can be set to a value between 0 and 1" {
            checkAll(
                Arb.probability(),
                Arb.probability(),
                Arb.probability()
            ) { individualRate, chromosomeRate, geneRate ->
                RandomMutator<Nothing, NothingGene>(
                    individualRate,
                    chromosomeRate,
                    geneRate
                ).chromosomeRate shouldBe chromosomeRate
            }
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            checkAll(
                Arb.probability(),
                Arb.double().filter { it !in 0.0..1.0 }.withEdgecases(),
                Arb.probability(),
            ) { individualRate, chromosomeRate, geneRate ->
                shouldThrow<CompositeException> {
                    RandomMutator<Nothing, NothingGene>(
                        individualRate,
                        chromosomeRate,
                        geneRate
                    )
                }.shouldHaveInfringement<MutatorConfigException>(
                    "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"
                )
            }
        }
    }

    "Should have a gene rate property that" - {
        "defaults to [RandomMutator.DEFAULT_GENE_RATE]" {
            checkAll(Arb.probability(), Arb.probability()) { individualRate, chromosomeRate ->
                RandomMutator<Nothing, NothingGene>(
                    individualRate,
                    chromosomeRate
                ).geneRate shouldBe RandomMutator.DEFAULT_GENE_RATE
            }
        }

        "can be set to a value between 0 and 1" {
            checkAll(
                Arb.probability(),
                Arb.probability(),
                Arb.probability()
            ) { individualRate, chromosomeRate, geneRate ->
                RandomMutator<Nothing, NothingGene>(
                    individualRate,
                    chromosomeRate,
                    geneRate
                ).geneRate shouldBe geneRate
            }
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            checkAll(
                Arb.probability(),
                Arb.probability(),
                Arb.double().filter { it !in 0.0..1.0 }.withEdgecases(),
            ) { individualRate, chromosomeRate, geneRate ->
                shouldThrow<CompositeException> {
                    RandomMutator<Nothing, NothingGene>(
                        individualRate,
                        chromosomeRate,
                        geneRate
                    )
                }.shouldHaveInfringement<MutatorConfigException>(
                    "The gene rate ($geneRate) must be in 0.0..1.0"
                )
            }
        }
    }

    "Can mutate a gene" {
        val mutator = RandomMutator<Int, IntGene>()
        val gene = IntGene(0)
        Domain.random = Random(420)
        val mutated = mutator.mutateGene(gene)
        Domain.random = Random(420)
        mutated shouldBe gene.mutate()
    }
})
