/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.arbs.genetic.geneticMaterial
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll


class MutatorTest : FreeSpec({
    "A [Mutator]" - {}

    "A [MutatorResult]" - {
        "when created" - {
            "without mutations then the number of mutations should be 0" {
                checkAll(Arb.geneticMaterial()) { material ->
                    val result = MutatorResult(material)
                    result.mutated shouldBe material
                    result.mutations shouldBe 0
                }
            }

            "with the specified number of mutations, it should match the given count" {
                checkAll(
                    Arb.geneticMaterial(),
                    Arb.nonNegativeInt()
                ) { material, mutations ->
                    val result = MutatorResult(material, mutations)
                    result.mutated shouldBe material
                    result.mutations shouldBe mutations
                }
            }

            "with a negative number of mutations then an exception should be thrown" {
                checkAll(
                    Arb.geneticMaterial(),
                    Arb.negativeInt()
                ) { material, mutations ->
                    shouldThrow<EnforcementException> {
                        MutatorResult(material, mutations)
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint(
                            "The number of mutations [$mutations] must be non-negative."
                        )
                    )
                }
            }
        }

        "can be mapped" - {
            "to identity" {
                checkAll(Arb.intGene()) { gene ->
                    val result = MutatorResult(gene)
                    result.map { it } shouldBe result
                }
            }

            "to double its value" {
                checkAll(Arb.intGene()) { gene ->
                    val result = MutatorResult(gene)
                    result.map { IntGene(it.dna * 2) } shouldBe MutatorResult(
                        IntGene(gene.dna * 2),
                        result.mutations
                    )
                }
            }
        }
    }
})

