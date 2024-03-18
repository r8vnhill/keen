/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.invalidProbability
import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.operators.combineCrossover
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

class CombineCrossoverTest : FreeSpec({

    "CombineCrossover" - {
        "fails creation when" - {
            "the gene rate is not in 0.0..1.0" {
                checkAll(
                    Arb.double(),
                    Arb.invalidProbability(),
                    Arb.int(),
                    Arb.boolean()
                ) { chromosomeRate, geneRate, numParents, exclusivity ->
                    shouldThrow<CompositeException> {
                        CombineCrossover<Nothing, NothingGene>(
                            combiner = { genes -> genes.random() },
                            chromosomeRate = chromosomeRate,
                            geneRate = geneRate,
                            numParents = numParents,
                            exclusivity = exclusivity
                        )
                    }.shouldHaveInfringement<CrossoverConfigException>("The gene rate ($geneRate) must be in 0.0..1.0")
                }
            }

            "the number of parents is not positive" {
                checkAll(
                    Arb.double(),
                    Arb.double(),
                    Arb.nonPositiveInt(),
                    Arb.boolean()
                ) { chromosomeRate, geneRate, numParents, exclusivity ->
                    shouldThrow<CompositeException> {
                        CombineCrossover<Nothing, NothingGene>(
                            combiner = { genes -> genes.random() },
                            chromosomeRate = chromosomeRate,
                            geneRate = geneRate,
                            numParents = numParents,
                            exclusivity = exclusivity
                        )
                    }.shouldHaveInfringement<CrossoverConfigException>(
                        "The number of parents ($numParents) must be positive"
                    )
                }
            }
        }

        "combiner property is set by the constructor" {
            checkAll(
                Arb.probability(),
                Arb.probability(),
                Arb.positiveInt(),
                Arb.boolean()
            ) { chromosomeRate, geneRate, numParents, exclusivity ->
                val combiner: (List<NothingGene>) -> NothingGene = { genes -> genes.random() }
                val crossover = CombineCrossover(combiner, chromosomeRate, geneRate, numParents, exclusivity)
                crossover.combiner shouldBe combiner
            }
        }

        "chromosome rate property" - {
            "defaults to CombineCrossover.DEFAULT_CHROMOSOME_RATE" {
                checkAll(
                    Arb.probability(),
                    Arb.positiveInt(),
                    Arb.boolean()
                ) { geneRate, numParents, exclusivity ->
                    CombineCrossover<Nothing, NothingGene>(
                        combiner = { genes -> genes.random() },
                        geneRate = geneRate,
                        numParents = numParents,
                        exclusivity = exclusivity
                    ).chromosomeRate shouldBe CombineCrossover.DEFAULT_CHROMOSOME_RATE
                }
            }

            "is set by the constructor" {
                checkAll(
                    Arb.probability(),
                    Arb.probability(),
                    Arb.positiveInt(),
                    Arb.boolean()
                ) { chromosomeRate, geneRate, numParents, exclusivity ->
                    CombineCrossover<Nothing, NothingGene>(
                        combiner = { genes -> genes.random() },
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate,
                        numParents = numParents,
                        exclusivity = exclusivity
                    ).chromosomeRate shouldBe chromosomeRate
                }
            }

            "fails if it is not in 0.0..1.0" {
                checkAll(
                    Arb.invalidProbability(),
                    Arb.double(),
                    Arb.int(),
                    Arb.boolean()
                ) { chromosomeRate, geneRate, numParents, exclusivity ->
                    shouldThrow<CompositeException> {
                        CombineCrossover<Nothing, NothingGene>(
                            combiner = { genes -> genes.random() },
                            chromosomeRate = chromosomeRate,
                            geneRate = geneRate,
                            numParents = numParents,
                            exclusivity = exclusivity
                        )
                    }.shouldHaveInfringement<CrossoverConfigException>(
                        "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"
                    )
                }
            }
        }

        "when combining chromosomes" - {
            "fails if the number of inputs is different from the number of parents" {
                checkAll(
                    Arb.combineCrossover<Nothing, NothingGene>(),
                    Arb.list(Arb.nothingChromosome(), 1..10)
                ) { crossover, chromosomes ->
                    assume { chromosomes shouldNotHaveSize crossover.numParents }
                    shouldThrow<CompositeException> { crossover.combine(chromosomes) }
                }
            }
        }
    }
})
