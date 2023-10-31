/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.assertions.operations.mutators.`mutator chromosome rate defaults to one half`
import cl.ravenhill.keen.assertions.operations.mutators.`mutator gene rate defaults to one half`
import cl.ravenhill.keen.assertions.operations.mutators.`mutator with valid parameters`
import cl.ravenhill.keen.assertions.operations.mutators.`throw exception if chromosome rate exceeds 1`
import cl.ravenhill.keen.assertions.operations.mutators.`throw exception on gene rate greater than one`
import cl.ravenhill.keen.assertions.operations.mutators.`throw exception on negative chromosome rate`
import cl.ravenhill.keen.assertions.operations.mutators.`throw exception on negative gene rate`
import cl.ravenhill.keen.assertions.operations.mutators.`validate unchanged gene with zero mutation rate`
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb

class RandomMutatorTest : FreeSpec({
    "A [RandomMutator]" - {
        "when created" - {
            "without a chromosome probability then it defaults to 0.5" {
                `mutator chromosome rate defaults to one half` { probability, geneRate ->
                    RandomMutator<Int, IntGene>(probability, geneRate = geneRate)
                }
            }

            "without a gene rate defaults to 0.5" {
                `mutator gene rate defaults to one half` { probability, chromosomeRate ->
                    RandomMutator<Int, IntGene>(
                        probability,
                        chromosomeRate = chromosomeRate
                    )
                }
            }

            "with valid parameters then it should create a valid mutator" {
                `mutator with valid parameters` { probability, chromosomeRate, geneRate ->
                    RandomMutator<Int, IntGene>(probability, chromosomeRate, geneRate)
                }
            }

            "should throw an exception" - {
                "if the gene rate is negative" {
                    `throw exception on negative gene rate` { probability, rate ->
                        RandomMutator<Int, IntGene>(probability, geneRate = rate)
                    }
                }

                "if the gene rate is greater than 1" {
                    `throw exception on gene rate greater than one` { probability, rate ->
                        RandomMutator<Int, IntGene>(probability, geneRate = rate)
                    }
                }

                "if the chromosome rate is negative" {
                    `throw exception on negative chromosome rate` { probability, rate ->
                        RandomMutator<Int, IntGene>(probability, chromosomeRate = rate)
                    }
                }

                "if the chromosome rate is greater than 1" {
                    `throw exception if chromosome rate exceeds 1` { probability, rate ->
                        RandomMutator<Int, IntGene>(probability, chromosomeRate = rate)
                    }
                }
            }
        }

        "when mutating a gene" - {
            "should make no mutations if the gene rate is 0" {
                `validate unchanged gene with zero mutation rate`(Arb.intGene()) {
                    RandomMutator(1.0, geneRate = 0.0)
                }
            }
        }
    }
})
