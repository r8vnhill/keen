/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.operators.baseCrossover
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverInvocationException
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class CrossoverTest : FreeSpec({

    "Crossover" - {
        "when crossing genotypes" - {
            "fails if arguments doesn't match number of parents" {
                checkAll(
                    Arb.baseCrossover<Nothing, NothingGene>(),
                    Arb.list(Arb.genotype(Arb.nothingChromosome()))
                ) { crossover, genotypes ->
                    assume {
                        genotypes shouldNotHaveSize crossover.numParents
                    }
                    withClue("Number of parents: ${crossover.numParents}; Number of genotypes: ${genotypes.size}") {
                        shouldThrow<CompositeException> {
                            crossover.crossover(genotypes)
                        }.shouldHaveInfringement<CrossoverInvocationException>(
                            "The number of genotypes (${genotypes.size}) doesn't match the number of parents" +
                                  " (${crossover.numParents})"
                        )
                    }
                }
            }

            "crosses the expected number of chromosomes" {
                checkAll(
                    Arb.baseCrossover<Nothing, NothingGene>(),
                    Arb.list(Arb.genotype(Arb.nothingChromosome()))
                ) { crossover, genotypes ->

                }
            }
        }
    }
})
