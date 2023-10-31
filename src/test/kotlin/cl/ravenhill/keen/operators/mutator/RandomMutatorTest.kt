/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
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
import cl.ravenhill.keen.util.nextIntInRange
import cl.ravenhill.real
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

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

            "should always mutate the gene if the gene rate is 1" {
                checkAll(
                    Arb.intGene(),
                    Arb.real(0.0..1.0),
                    Arb.real(0.0..1.0),
                    Arb.long()
                ) { gene, probability, chromosomeRate, seed ->
                    Core.random = Random(seed)
                    val rng = Random(seed).also { it.nextDouble() }
                    val mutator = RandomMutator<Int, IntGene>(
                        probability, chromosomeRate, geneRate = 1.0
                    )
                    val result = mutator.mutateGene(gene)
                    result.mutated shouldBe IntGene(
                        rng.nextIntInRange(gene.range), gene.range
                    )
                    result.mutations shouldBe 1
                }
            }

            "should mutate the gene according to the probability" {
                checkAll(
                    Arb.intGene(),
                    Arb.real(0.0..1.0),
                    Arb.real(0.0..1.0),
                    Arb.real(0.0..1.0),
                    Arb.long()
                ) { gene, probability, chromosomeRate, geneRate, seed ->
                    Core.random = Random(seed)
                    val rng = Random(seed)
                    val mutator = RandomMutator<Int, IntGene>(
                        probability, chromosomeRate, geneRate
                    )
                    val result = mutator.mutateGene(gene)
                    if (rng.nextDouble() < geneRate) {
                        // Expect a mutation
                        result.mutated shouldBe IntGene(
                            rng.nextIntInRange(gene.range), gene.range
                        )
                        result.mutations shouldBe 1
                    } else {
                        // Expect no mutation
                        result.mutated shouldBe gene
                        result.mutations shouldBe 0
                    }
                }
            }
        }
    }
})
