/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.invalidProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.checkAll

class CombineCrossoverTest : FreeSpec({

    "CombineCrossover" - {
        "when created" - {
            "fails if the chromosome rate is not in 0.0..1.0" {
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

            "fails if the gene rate is not in 0.0..1.0" {
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

            "fails if the number of parents is not positive" {
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
    }
})
