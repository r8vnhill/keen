/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.arbs.genetic.geneticMaterial
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
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

    "A [Mutator]" - {
        "when created" - {
        }
    }

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

/**
 * A dummy implementation of the [AbstractMutator] used primarily for testing and demonstration purposes.
 *
 * This mutator does not perform any real mutation operations but simply returns the provided chromosome
 * as it is without modification.
 *
 * @property probability The probability of mutation, inherited from [AbstractMutator]. For `DummyMutator`,
 * it doesn't have any real implication since mutation isn't performed.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
private class DummyMutator(probability: Double) :
    AbstractMutator<Nothing, NothingGene>(probability) {

    override fun mutateChromosome(chromosome: Chromosome<Nothing, NothingGene>) =
        MutatorResult(chromosome)
}
