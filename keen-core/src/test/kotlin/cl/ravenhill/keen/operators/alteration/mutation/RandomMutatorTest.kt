/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.genes.intGene
import cl.ravenhill.keen.arb.operators.randomMutator
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
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
