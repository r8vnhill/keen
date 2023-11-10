/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.genetic.geneticMaterial
import cl.ravenhill.keen.arbs.genetic.individual
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.arbs.genetic.intGenotype
import cl.ravenhill.keen.arbs.genetic.intPopulation
import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.assertions.operations.mutators.`should enforce valid mutation probability`
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.utils.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeDouble
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveDouble
import io.kotest.property.assume
import io.kotest.property.checkAll

class MutatorTest : FreeSpec({

    "A [Mutator]" - {
        "when created with a probability" - {
            "should store the given value" {
                checkAll(Arb.real(0.0..1.0)) { probability ->
                    DummyMutator<Nothing, NothingGene>(probability).probability
                        .shouldBe(probability)
                }
            }

            "should throw an exception when" - {
                "the chromosome mutation rate is negative" {
                    `should enforce valid mutation probability`(
                        Arb.negativeDouble(),
                        "chromosome mutation probability"
                    ) { probability, chromosomeRate ->
                        DummyMutator<Nothing, NothingGene>(
                            probability,
                            chromosomeRate
                        )
                    }
                }

                "the chromosome mutation rate is greater than 1" {
                    `should enforce valid mutation probability`(
                        Arb.positiveDouble(),
                        "chromosome mutation probability",
                        { assume { it shouldBeGreaterThan 1.0 } }
                    ) { probability, chromosomeRate ->
                        DummyMutator<Nothing, NothingGene>(
                            probability,
                            chromosomeRate
                        )
                    }
                }
            }
        }

        "can mutate a genotype" {
            // TODO: Generalize genotype
            checkAll(
                Arb.intGenotype(),
                Arb.real(0.0..1.0)
            ) { genotype, probability ->
                with(DummyMutator<Int, IntGene>(probability).mutateGenotype(genotype)) {
                    mutated shouldBe genotype
                    mutations shouldBe 0
                }
            }
        }

        "can mutate an individual" {
            checkAll(Arb.individual(Arb.intGenotype())) { individual ->
                with(DummyMutator<Int, IntGene>(0.0).mutateIndividual(individual)) {
                    mutated shouldBe individual
                    mutations shouldBe 0
                }
            }
        }

        "can mutate a population" {
            checkAll(Arb.intPopulation()) { population ->
                with(DummyMutator<Int, IntGene>(1.0)(population, 0)) {
                    this.population shouldBe population
                    this.alterations shouldBe 0
                }
            }
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
                    shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                        MutatorResult(material, mutations)
                    }.shouldHaveInfringement<IntConstraintException>(
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
private class DummyMutator<DNA, G>(override val probability: Double, override val chromosomeRate: Double = 0.5) :
    Mutator<DNA, G> where G : Gene<DNA, G> {

    override fun mutateChromosome(chromosome: Chromosome<DNA, G>) =
        MutatorResult(chromosome)
}
